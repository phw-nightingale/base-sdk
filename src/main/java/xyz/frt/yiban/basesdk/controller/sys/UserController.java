package xyz.frt.yiban.basesdk.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.frt.yiban.basesdk.common.JsonResult;
import xyz.frt.yiban.basesdk.common.Page;
import xyz.frt.yiban.basesdk.controller.BaseController;
import xyz.frt.yiban.basesdk.entity.sys.User;
import xyz.frt.yiban.basesdk.service.BaseService;
import xyz.frt.yiban.basesdk.service.sys.UserService;

@RestController
public class UserController extends BaseController<User> {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected BaseService<User> getService() {
        return userService;
    }

    @GetMapping("/users")
    public JsonResult findUsers(User user, Page page) {
        return findItems(user, page);
    }

    @GetMapping("/users/{id}")
    public JsonResult findUserByPrimaryKey(@PathVariable Integer id) {
        return findItemByPrimaryKey(id);
    }

    @PostMapping("/sign-in")
    public JsonResult signIn(User user) {
        return userService.signIn(user);
    }

    @GetMapping("/sign-out")
    public JsonResult signOut() {
        return userService.signOut();
    }

    @PostMapping("/sign-up")
    public JsonResult signUp(User user) {
        return userService.signUp(user);
    }

}
