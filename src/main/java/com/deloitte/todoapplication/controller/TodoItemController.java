package com.deloitte.todoapplication.controller;

import com.deloitte.todoapplication.dao.TodoItemDao;
import com.deloitte.todoapplication.dao.UserDao;
import com.deloitte.todoapplication.domain.ResponseResult;
import com.deloitte.todoapplication.pojo.TodoItem;
import com.deloitte.todoapplication.pojo.User;
import com.deloitte.todoapplication.service.TodoItemService;
import com.deloitte.todoapplication.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneId;


@RestController
public class TodoItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoItemController.class);

    @Autowired
    private TodoItemDao todoItemDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TodoItemService todoItemService;

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            String token = httpServletRequest.getHeader("token");
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            User user = userDao.findByUserId(Long.valueOf(userId));
            modelAndView.addObject("todoItems", todoItemDao.findAllByUserId(Long.valueOf(userId), Sort.by(Sort.Direction.DESC, "modifiedDate")));
            modelAndView.addObject("username", user.getUsername());
            modelAndView.addObject("today", Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @PostMapping("/addTodo")
    public ResponseResult addTodoItem(@RequestBody @Valid TodoItem todoItem, HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("token");
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            if (userId != null && !userId.isEmpty()) {
                todoItemService.addTodo(todoItem, userId);
                return new ResponseResult(HttpStatus.OK.value(), "Add Successfully!");
            }else {
                return new ResponseResult(HttpStatus.PRECONDITION_FAILED.value(), "UserId is null");
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(HttpStatus.EXPECTATION_FAILED.value(), "Failed to add");
        }
    }

    @PostMapping("/updateTodo")
    public ResponseResult updateTodoItem(@RequestBody @Valid TodoItem todoItem) {
        try {
            todoItemService.updateTodo(todoItem);
            return new ResponseResult(HttpStatus.OK.value(), "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(HttpStatus.EXPECTATION_FAILED.value(), "Failed to update");
        }
    }

    @PostMapping("/deleteTodo")
    public ResponseResult deleteTodoItem(@RequestBody TodoItem item, HttpServletRequest httpServletRequest) {
        /*TodoItem todoItem = todoItemDao
                .findById(item.getId())
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id : " + item.getId() + " not found"));
        todoItemDao.delete(todoItem);*/
        try {
            String token = httpServletRequest.getHeader("token");
            todoItemDao.delete(item);
            return new ResponseResult(HttpStatus.OK.value(), "Delete todo successfully");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(HttpStatus.EXPECTATION_FAILED.value(), "Failed to delete");
        }
    }
}
