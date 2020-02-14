package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.TodoInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;

import java.util.List;

public interface ToDoService {
    List<TodoModel> findAll();

    TodoModel findById(Long id);

    TodoModel save(TodoInput todoInput);

    TodoModel update(Long id, TodoInput todoInput);

    void deleteToDoById(Long id);
}
