package com.bravo.johny.controller;

import com.bravo.johny.dto.LoginResponse;
import com.bravo.johny.dto.User;
import com.bravo.johny.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/userlogin")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping
    public LoginResponse authenticateUser(@RequestBody User user) {

        boolean authenticationStatus = userService.authenticateUser(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthenticated(authenticationStatus);

        return loginResponse;
    }

}
