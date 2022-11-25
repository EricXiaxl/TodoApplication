package com.deloitte.todoapplication.controller;

import com.alibaba.fastjson.JSONObject;
import com.deloitte.todoapplication.TodoApplication;
import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.pojo.User;
import com.deloitte.todoapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@Slf4j
@AutoConfigureMockMvc
class UserControllerTest {

    @Resource
    MockMvc mockMvc;
    @Resource
    UserService userService;

    @Test
    void login() throws Exception {
        User userInfo = new User("test1","pwd123");
        String responseString = mockMvc.perform(
                post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(userInfo))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token", notNullValue()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString);
    }

    @Test
    @Transactional
    @Rollback()
    void register() throws Exception{
        User user1 = new User("test001", "pwd123", "test001@gmail.com","0892377001");
        String responseString1 = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(user1))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString1);

        //Test phone is null
        User user2 = new User("test002", "pwd123");
        user2.setEmail("test002@gmail.com");
        String responseString2 = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(user2))
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString2);

        //Test email is null
        User user3 = new User("test003", "pwd123");
        user2.setPhone("0892377003");
        String responseString3 = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(user3))
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString3);
    }

    @Test
    void userLogout() throws Exception{
        User user = new User("test1","pwd123");
        ResponseResult result = userService.login(user);
        HashMap<String, String> hashMap = (HashMap<String, String>) result.getData();
        String token = hashMap.get("token");

        String responseString = mockMvc.perform(
                post("/userLogout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token",token)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString);
    }
}