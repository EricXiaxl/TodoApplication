package com.deloitte.todoapplication.service.impl;

import com.deloitte.todoapplication.dao.TodoItemDao;
import com.deloitte.todoapplication.pojo.TodoItem;
import com.deloitte.todoapplication.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TodoItemServiceImpl implements TodoItemService {

    @Autowired
    TodoItemDao todoItemDao;

    @Override
    public void addTodo(TodoItem todoItem, String userId) {
        todoItem.setUserId(Long.valueOf(userId));
        todoItem.setCreatedDate(Instant.now());
        todoItem.setModifiedDate(Instant.now());
        todoItemDao.save(todoItem);
    }

    @Override
    public void updateTodo(TodoItem todoItem) {
        todoItem.setModifiedDate(Instant.now());
        todoItemDao.save(todoItem);
    }
}
