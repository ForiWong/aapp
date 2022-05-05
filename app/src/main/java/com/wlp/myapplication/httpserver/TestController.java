package com.wlp.myapplication.httpserver;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
class TestController {

    @GetMapping(path = "/login")
    String login(@RequestParam("a") String account,
               @RequestParam("p") String password) {
        if("123".equals(account) && "123".equals(password)) {
            return "Login successful.";
        } else {
            return "Login failed.";
        }
    }

}