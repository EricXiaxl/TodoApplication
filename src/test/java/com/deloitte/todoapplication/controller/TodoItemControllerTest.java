package com.deloitte.todoapplication.controller;

import com.alibaba.fastjson.JSONObject;
import com.deloitte.todoapplication.TodoApplication;
import com.deloitte.todoapplication.dao.TodoItemDao;
import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.pojo.TodoItem;
import com.deloitte.todoapplication.pojo.User;
import com.deloitte.todoapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Transactional
@Slf4j
class TodoItemControllerTest {

    @Resource
    MockMvc mockMvc;
    @Autowired
    private TodoItemDao todoItemDao;
    @Autowired
    private UserService userService;

    private static String token = "";
    private static String userId = "";

    @BeforeEach
    public void init() throws Exception{
        User user = new User("test1","pwd123");
        ResponseResult result = userService.login(user);
        HashMap<String, String> hashMap = (HashMap<String, String>) result.getData();
        token = hashMap.get("token");
        userId = hashMap.get("userId");
    }

    @Test
    void getTodoList() throws Exception{
        List<TodoItem> list = todoItemDao.findAllByUserId(Long.valueOf(userId), Sort.by(Sort.Direction.DESC, "modifiedDate"));
        assertNotNull(list);
    }

    @Test
    @Transactional
    @Rollback()
    void addTodoItem() throws Exception{
        TodoItem todoItem1 = new TodoItem("Test Add Todo Item");

        String responseString1 = mockMvc.perform(
                post("/addTodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(todoItem1))
                        .header("token",token)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString1);

        //Test: Description is null
        TodoItem todoItem2 = new TodoItem();
        String responseString2 = mockMvc.perform(
                post("/addTodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(todoItem2))
                        .header("token",token)
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString2);
    }

    @Test
    @Transactional
    @Rollback()
    void updateTodoItem() throws Exception{
        TodoItem todoItem1 = new TodoItem("Test Add Todo Item");
        String responseString1 = mockMvc.perform(
                post("/updateTodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(todoItem1))
                        .header("token",token)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString1);

        //Test: Description is null
        TodoItem todoItem2 = new TodoItem();
        String responseString2 = mockMvc.perform(
                post("/updateTodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(todoItem2))
                        .header("token",token)
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString2);

        //Test: TodoItem id is null
        TodoItem todoItem3 = new TodoItem();
        todoItem3.setDescription("Test todoItem id is null");
        String responseString3 = mockMvc.perform(
                post("/updateTodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(todoItem2))
                        .header("token",token)
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result= " + responseString3);

        //Test: TodoItem is not exist
        TodoItem todoItem4 = new TodoItem();
        todoItem4.setDescription("Test TodoItem not exist");
        todoItem4.setId((long) 5555555);
        String responseString4 = mockMvc.perform(
                post("/updateTodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(todoItem2))
                        .header("token",token)
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------Response Json Result = " + responseString4);
    }

}