package com.deloitte.todoapplication.service.impl;

import com.deloitte.todoapplication.dao.UserDao;
import com.deloitte.todoapplication.domain.LoginUser;
import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.pojo.User;
import com.deloitte.todoapplication.service.UserService;
import com.deloitte.todoapplication.util.CacheSingletonUtil;
import com.deloitte.todoapplication.util.JwtUtil;
import com.deloitte.todoapplication.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult addUser(User user) {
        //todo Verify whether the email is repeat

        //todo Verify whether the phone number is repeat

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
        return new ResponseResult(HttpStatus.OK.value(),"Registration success");
    }

    @Override
    public ResponseResult login(User user) {
        //1. Use the AuthenticationManager in spring security for user authentication
        //Encapsulate the username and password into a UsernamePasswordAuthenticationToken object
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //Let the authenticate method in authenticationManager help us authenticate
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //2. If the authentication fails, throw the corresponding prompt
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }

        //3. If the authentication is successful, use userId to generate a jwt token, jwt is stored in ResponseResult and return
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();

        String jwt = JwtUtil.createJWT(userid);

        //4. userId is used as a key, and the complete user information is stored in redis
        //authenticate is stored in redis
        // (Due to environmental constraints in this coding task, redis cannot be configured from the outside, so consider using java cache)
//        redisCache.setCacheObject("login:"+userid,loginUser);
        CacheSingletonUtil.getInstance().addCacheData("login:" + userid, loginUser);

        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        map.put("userId", userid);

        return new ResponseResult(200,"Login Successfully",map);
    }

    @Override
    public ResponseResult logout() {
        //Get the user id in the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //Delete the value in redis
        // (Due to environmental restrictions in this coding task, redis cannot be configured from the outside, so consider using java cache)
//        redisCache.deleteObject("login:"+userid);
        CacheSingletonUtil.getInstance().removeCacheData("login:"+userid);
        return new ResponseResult(HttpStatus.OK.value(),"Logout Successfully");
    }
}
