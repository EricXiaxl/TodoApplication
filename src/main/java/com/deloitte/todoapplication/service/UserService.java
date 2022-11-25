package com.deloitte.todoapplication.service;

import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.pojo.User;

public interface UserService {
    ResponseResult addUser(User user);

    ResponseResult login(User user);

    ResponseResult logout();
}
