package com.deloitte.todoapplication.controller;

import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.pojo.User;
import com.deloitte.todoapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        try {
            LOGGER.info("Login successfully");
            return userService.login(user);
        }catch (Exception e){
            LOGGER.error("Login throws an exception");
            e.printStackTrace();
            return new ResponseResult(HttpStatus.EXPECTATION_FAILED.value(), "Failed to login");
        }
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid User user) {
        try{
            LOGGER.info("Register successfully");
            return userService.addUser(user);
        }catch (Exception e){
            LOGGER.error("Register throws an exception");
            e.printStackTrace();
            return new ResponseResult(201,"Failed to register");
        }
    }

    @PostMapping("/userLogout")
    public ResponseResult userLogout(){
        try {
            LOGGER.info("Logout successfully");
            return userService.logout();
        } catch (Exception e){
            LOGGER.error("Logout throws an exception");
            e.printStackTrace();
            return new ResponseResult(HttpStatus.EXPECTATION_FAILED.value(), "Failed to logout");
        }
    }
}
