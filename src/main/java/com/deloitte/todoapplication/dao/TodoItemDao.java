package com.deloitte.todoapplication.dao;

import com.deloitte.todoapplication.pojo.TodoItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoItemDao extends JpaRepository<TodoItem, Long> {

    @Query("select t from TodoItem t where t.userId = ?1")
    List<TodoItem> findAllByUserId(Long userId, Sort sort);
}
