package com.deloitte.todoapplication.controller;

import com.deloitte.todoapplication.dao.TodoItemDao;
import com.deloitte.todoapplication.dao.UserDao;
import com.deloitte.todoapplication.pojo.TodoItem;
import com.deloitte.todoapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RouteController {

    @Autowired
    TodoItemDao todoItemDao;
    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    //Jump to login page
    @GetMapping({"/","/loginPage"})
    public String loginPage() {
        return "login";
    }

    //Jump to the registration page
    @GetMapping("/registerPage")
    public String registerPage(){
        return "register";
    }

    @GetMapping("/indexPage")
    public String index() {
        return "index";
    }

    @GetMapping("/createTodoPage")
    public String createTodoPage(TodoItem todoItem) {
        return "add-todo-item";
    }

    @GetMapping("/showUpdateForm/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        TodoItem todoItem = todoItemDao
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id : " + id + " not found"));

        model.addAttribute("todo", todoItem);
        return "update-todo-item";
    }
}
