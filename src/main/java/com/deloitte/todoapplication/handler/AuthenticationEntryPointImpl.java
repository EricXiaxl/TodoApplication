package com.deloitte.todoapplication.handler;

import com.alibaba.fastjson.JSON;
import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Exceptions that occur during the authentication process are encapsulated into AuthenticationException
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "Authentication failed, please log in again");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse,json);
    }
}
