package xyz.frt.yiban.basesdk.service;

import cn.yiban.open.Authorize;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.frt.yiban.basesdk.common.AppConst;
import xyz.frt.yiban.basesdk.common.JsonResult;
import xyz.frt.yiban.basesdk.entity.sys.User;
import xyz.frt.yiban.basesdk.service.sys.UserService;
import xyz.frt.yiban.basesdk.util.BaseUtils;

@Slf4j
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${yiban.appid}")
    private String APP_ID;

    @Value("${yiban.appsec}")
    private String APP_SEC;

    @Value("${yiban.backurl}")
    private String BAK_URL;

    private final UserService userService;

    /**
     * 接口服务器
     */
    private static final String HOST = "https://openapi.yiban.cn";

    /**
     * OAuth授权接口
     */
    private static final String URI_AUTHORIZE = "/oauth/authorize"; //请求用户授权获取令牌code
    private static final String URI_ACCESS_TOKEN = "/oauth/access_token";   //获取已授权用户的access_token
    private static final String URI_TOKEN_INFO = "/oauth/token_info";   //查询用户access_token的相关授权信息
    private static final String URI_REVOKE_TOKEN = "/oauth/revoke_token";   //帮助开发者主动取消用户的授权
    private static final String URI_RESET_TOKEN = "/oauth/reset_token"; //帮助开发者维持离线状态下开发者的有效授权

    /**
     * 用户接口
     */
    private static final String URI_ME = "/user/me";    //获取当前用户基本信息
    private static final String URI_OTHER = "/user/other";  //获取指定用户基本信息
    private static final String URI_REAL_ME = "/user/real_me";  //获取当前用户实名信息
    private static final String URI_VERIFY_ME = "/user/verify_me";  //获取当前用户校方认证信息
    private static final String URI_IS_REAL = "/user/is_real";  //指定用户是否实名认证
    private static final String URI_IS_VERIFY = "/user/is_verify";  //当前用户是否校方认证
    private static final String URI_CHECK_VERIFY = "/user/check_verify";    //当前用户完成校方认证

    @Autowired
    public AuthorizeServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * 其他常用接口
     */

    @Override
    public JsonResult authorize(String code) {
        if (BaseUtils.isNullOrEmpty(code)) {
            return JsonResult.error("获取易班用户失败");
        }
        // 拼接请求URL
        StringBuilder url = new StringBuilder()
                .append(HOST)
                .append(URI_ACCESS_TOKEN)
                .append("?client_id=")
                .append(APP_ID)
                .append("&client_secret=")
                .append(APP_SEC)
                .append("&code=")
                .append(code)
                .append("&redirect_uri=")
                .append(BAK_URL);
        // 利用易班SDK提供的工具获取token
        Authorize authorize = new Authorize(APP_ID, APP_SEC);
        String result = authorize.querytoken(code, BAK_URL);
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (jsonObject.containsKey(AppConst.KEY_YB_STATUS) && jsonObject.getString(AppConst.KEY_YB_STATUS).equals("error")) {
            return JsonResult.error(result);
        }

        // 如果是首次登录的用户则新增用户信息
        Integer userId = jsonObject.getInt(AppConst.KEY_YB_USR_ID);
        String accessToken = jsonObject.getString(AppConst.KEY_YB_ACS_TOKEN);
        long expires = jsonObject.getLong(AppConst.KEY_YB_EXP);

        // 此处如果该应用只能用易班账户登录，那么userId这个字段就直接存到user表的ID字段去，
        // 如果自己注册账号，后续在关系到易班账号，那么可以重新在user表中添加一个yb_usr_id字段来存储userId字段。
        // 此处以第一种情况为例
        User user = userService.selectByPrimaryKey(userId);
        if (BaseUtils.isNullOrEmpty(user)) {
            user = new User();
            user.setId(userId);
            user.setToken(accessToken);
            user.setStr1(expires + ""); // 此处以str1字段为过期时间
            // 其余信息可以使用该用户的易班信息来保存
            // 比如用户名和签名等
            // ......
            userService.insert(user);
        } else {
            // 更新令牌以及过期时间
            user.setToken(accessToken);
            user.setStr1(expires + "");
            userService.updateByPrimaryKey(user);
        }

        return JsonResult.success("授权成功", result);
    }

}
