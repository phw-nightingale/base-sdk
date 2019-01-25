package xyz.frt.yiban.basesdk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.frt.yiban.basesdk.common.JsonResult;
import xyz.frt.yiban.basesdk.service.AuthorizeService;

@Slf4j
@RestController
public class AuthorizeController {

    private final AuthorizeService authorizeService;

    @Autowired
    public AuthorizeController(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;
    }

    @GetMapping("/authorize")
    public JsonResult authorize(@RequestParam String code) {
        return authorizeService.authorize(code);
    }


}
