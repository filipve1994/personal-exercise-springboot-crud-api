package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.errorhandling.exceptions.ToDoModelNotFoundException;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.TodoInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {

    private static final Logger logger = LoggerFactory.getLogger(ToDoServiceImpl.class);

    private final ToDoRepository toDoRepository;

    public ToDoServiceImpl(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public List<TodoModel> findAll() {
        return toDoRepository.findAll();
    }

    @Override
    public TodoModel findById(Long id) {
        return toDoRepository.findById(id).orElseThrow(() -> new ToDoModelNotFoundException("Todo with id : " + id + " does not exist."));
    }

    @Override
    public TodoModel save(TodoInput todoInput) {
        TodoModel todoModelToSave = new TodoModel();
        todoModelToSave.setName(todoInput.getName());
        todoModelToSave.setCompleted(todoInput.getCompleted());

        return toDoRepository.save(todoModelToSave);
    }

    @Override
    public TodoModel update(Long id, TodoInput todoInput) {

        if (!toDoRepository.findById(id).isPresent()) {
            logger.error("Id " + id + " is not existed.");
            throw new ToDoModelNotFoundException("Todo with id : " + id + " does not exist.");
            // BAD_REQUEST
        }

        TodoModel todoModelFromDB = toDoRepository.findById(id).get();
        todoModelFromDB.setName(todoInput.getName());
        todoModelFromDB.setCompleted(todoInput.getCompleted());

        toDoRepository.save(todoModelFromDB);

        return todoModelFromDB;
    }

    @Override
    public void deleteToDoById(Long id) {

        Optional<TodoModel> todoFromDb = toDoRepository.findById(id);

        if (!todoFromDb.isPresent()) {
            logger.error("Id " + id + " is not existed.");
            throw new ToDoModelNotFoundException("Todo with id : " + id + " does not exist.");
            // BAD_REQUEST
        }

        toDoRepository.deleteById(id);

    }

}
