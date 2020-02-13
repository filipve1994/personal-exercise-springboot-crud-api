package com.filipve1994.personalexercisespringbootcrudapi.web.restcontrollers;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.UserInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.User;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.filipve1994.personalexercisespringbootcrudapi.common.utils.ResponseUtil.acceptedResponse;
import static com.filipve1994.personalexercisespringbootcrudapi.common.utils.ResponseUtil.createdResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
//@RequestMapping(value = "/api/user", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequestMapping(value = "/api/user")
// @AllArgsConstructor
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity findAll() {
        logger.info("FindAll method used.");
        return ResponseEntity.ok(userService.findAll());
    }

//    @GetMapping(value = "/paging", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @GetMapping(value = "/paging")
    public ResponseEntity findAllWithPaging(
            @Valid @Positive(message = "Page number should be a positive number") @RequestParam(required = false, defaultValue = "1") int page,
            @Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = "10") int size) {
        HttpHeaders headers = new HttpHeaders();
        Page<User> users = userService.findAll(PageRequest.of(page, size));
        headers.add("X-Users-Total", Long.toString(users.getTotalElements()));
        logger.info("findAllWithPaging method used.");
//        return ResponseEntity.ok(users.getContent(), headers);
        return new ResponseEntity(users.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity findById(@PathVariable Long id) {
        logger.info("findById method used.");
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody UserInput userInput) {
        logger.info("create method used.");
        return createdResponse(userService.save(userInput));
    }

//    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
//    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody TodoModel todoModel) {
//        logger.info("update method used.");
//        return ResponseEntity.ok(toDoService.update(id, todoModel));
//    }
//
//    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
//    public ResponseEntity deleteById(@PathVariable Long id) {
//        logger.info("deleteById method used.");
//        toDoService.deleteToDoById(id);
//
////        return ResponseEntity.accepted().build();
//        return acceptedResponse();
//    }


}
