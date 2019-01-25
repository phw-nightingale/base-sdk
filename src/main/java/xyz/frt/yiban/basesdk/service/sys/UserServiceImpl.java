package xyz.frt.yiban.basesdk.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.frt.yiban.basesdk.common.AppConst;
import xyz.frt.yiban.basesdk.common.AppContext;
import xyz.frt.yiban.basesdk.common.JsonResult;
import xyz.frt.yiban.basesdk.dao.BaseMapper;
import xyz.frt.yiban.basesdk.dao.sys.UserMapper;
import xyz.frt.yiban.basesdk.entity.sys.User;
import xyz.frt.yiban.basesdk.service.BaseServiceImpl;
import xyz.frt.yiban.basesdk.util.BaseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public BaseMapper<User> getMapper() {
        return userMapper;
    }

    @Override
    public JsonResult selectUserByToken(String token) {
        User exa = new User();
        exa.setToken(token);
        List<User> userList = selectItems(exa);
        if (BaseUtils.isNullOrEmpty(userList)) {
            return JsonResult.error();
        }
        return JsonResult.success("request ok", userList.get(0));
    }

    @Override
    public JsonResult signIn(User user) {
        // 使用易班登录，此方法不再需要实现
        return JsonResult.success("login success");
    }

    @Override
    public JsonResult signOut() {
        HttpServletRequest request = AppContext.getRequest();
        request.removeAttribute(AppConst.KEY_CURRENT_USER);
        return JsonResult.success("logout success");
    }

    @Override
    public JsonResult signUp(User user) {
        // 此方法不再需要实现
        return JsonResult.success("sign up success");
    }
}
