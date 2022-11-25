package com.deloitte.todoapplication.filter;

import com.deloitte.todoapplication.domain.LoginUser;
import com.deloitte.todoapplication.util.CacheSingletonUtil;
import com.deloitte.todoapplication.util.JwtUtil;
import com.deloitte.todoapplication.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //1. Get the token from the request header
        String token = httpServletRequest.getHeader("token");

        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //2. parse the token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token is illegal");
        }


        //3. Obtain user information from redis
        // (due to environmental constraints in this evaluation task, redis cannot be configured externally, so consider using java cache)
        String redisKey = "login:" + userid;
//        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        LoginUser loginUser = (LoginUser) CacheSingletonUtil.getInstance().getCacheData(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("User not logged in");
        }

        //4. stored in SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
