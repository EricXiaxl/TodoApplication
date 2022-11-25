package com.deloitte.todoapplication.service.impl;


import com.deloitte.todoapplication.dao.UserDao;
import com.deloitte.todoapplication.domain.LoginUser;
import com.deloitte.todoapplication.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    /*@Autowired
    private MenuMapper menuMapper;*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1. Query user information
        User user = userDao.findByUsername(username);
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("Username or password is incorrect");
        }

        //2. To query user permissions, first temporarily write the permission data as a fixed value
        List<String> authoritieslist = new ArrayList<>(Arrays.asList("FREE","PRO"));
//        List<String> authoritieslist = menuMapper.selectPermsByUserId(user.getId());


        //3. Encapsulated into userdtails object
        return new LoginUser(user,authoritieslist);
    }
}
