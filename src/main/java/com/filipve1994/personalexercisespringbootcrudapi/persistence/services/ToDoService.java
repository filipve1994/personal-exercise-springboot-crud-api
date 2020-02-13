package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;

import java.util.List;

public interface ToDoService {
    abstract List<TodoModel> findAll();

    TodoModel findById(Long id);

    TodoModel save(TodoModel todoModel);

    TodoModel update(Long id, TodoModel todoModel);

    void deleteToDoById(Long id);
}
