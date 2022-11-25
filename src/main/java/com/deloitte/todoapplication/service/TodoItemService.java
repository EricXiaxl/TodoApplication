package com.deloitte.todoapplication.service;

import com.deloitte.todoapplication.pojo.TodoItem;

public interface TodoItemService {
    void addTodo(TodoItem todoItem, String userId);

    void updateTodo(TodoItem todoItem);
}
