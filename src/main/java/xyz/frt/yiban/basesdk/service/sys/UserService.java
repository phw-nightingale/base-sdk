package xyz.frt.yiban.basesdk.service.sys;

import xyz.frt.yiban.basesdk.common.JsonResult;
import xyz.frt.yiban.basesdk.entity.sys.User;
import xyz.frt.yiban.basesdk.service.BaseService;

public interface UserService extends BaseService<User> {

    JsonResult selectUserByToken(String token);

    JsonResult signIn(User user);

    JsonResult signOut();

    JsonResult signUp(User user);

}
