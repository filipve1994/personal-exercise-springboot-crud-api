package com.filipve1994.personalexercisespringbootcrudapi.web.restcontrollers;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.ToDoService;
import io.swagger.annotations.Api;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.filipve1994.personalexercisespringbootcrudapi.common.utils.ResponseUtil.acceptedResponse;
import static com.filipve1994.personalexercisespringbootcrudapi.common.utils.ResponseUtil.createdResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "/api/todos")
// @Api(tags = "")
public class ToDoController {

    private static final Logger logger = LoggerFactory.getLogger(ToDoController.class);

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TodoModel>> findAll() {
        logger.info("FindAll method used.");
        return ResponseEntity.ok(toDoService.findAll());
    }

    @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoModel> findById(@PathVariable Long id) {
        logger.info("findById method used.");
        return ResponseEntity.ok(toDoService.findById(id));
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoModel> create(@Valid @RequestBody TodoModel todoModel) {
        logger.info("create method used.");
//        return new ResponseEntity<TodoModel>(toDoService.save(todoModel), HttpStatus.CREATED);
        return createdResponse(toDoService.save(todoModel));
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody TodoModel todoModel) {
        logger.info("update method used.");
        return ResponseEntity.ok(toDoService.update(id, todoModel));
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity deleteById(@PathVariable Long id) {
        logger.info("deleteById method used.");
        toDoService.deleteToDoById(id);

//        return ResponseEntity.accepted().build();
        return acceptedResponse();
    }

}
