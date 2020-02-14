package com.filipve1994.personalexercisespringbootcrudapi.web.restcontrollers;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.ItemInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.Item;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.ItemService;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * https://www.onlinetutorialspoint.com/spring-boot/spring-boot-exception-handling-rest-service-crud-operations.html
 *
 */
@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "/api/items")
// @Api(tags = "")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "/all", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity findAll() {
        logger.info("FindAll method used.");
        return ResponseEntity.ok(itemService.findAllItems());
    }

    @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity findById(@PathVariable int id) {
        logger.info("findById method used.");
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody ItemInput itemInput) {
        logger.info("create method used.");
        return createdResponse(itemService.addItem(itemInput));
    }

    @PutMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable int id, @Valid @RequestBody ItemInput itemInput) {
        logger.info("update method used.");
        return ResponseEntity.ok(itemService.updateItem(id, itemInput));
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity deleteById(@PathVariable int id) {
        logger.info("deleteById method used.");
        itemService.deleteItemById(id);
        return acceptedResponse();
    }

}
