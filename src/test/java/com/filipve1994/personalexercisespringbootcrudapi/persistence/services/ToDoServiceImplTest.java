package com.filipve1994.personalexercisespringbootcrudapi.persistence.services;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.repositories.ToDoRepository;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


/**
 * https://www.freecodecamp.org/news/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772/
 */
@ExtendWith(MockitoExtension.class)
// @DataJpaTest
class ToDoServiceImplTest {

    @Mock
    private ToDoRepository toDoRepository;

//    @Mock
//    @InjectMocks
//    private ToDoService toDoService = new ToDoServiceImpl(toDoRepository);

    @Mock
    private ToDoService toDoService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        TodoModel todoModel1 = new TodoModel();
        todoModel1.setId(1L);
        todoModel1.setName("Todo1");

        TodoModel todoModel2 = new TodoModel();
        todoModel2.setId(2L);
        todoModel2.setName("Todo2");

//        when(toDoRepository.save(any(TodoModel.class))).thenReturn(new TodoModel());
//        when(toDoRepository.findAll()).thenReturn(Lists.newArrayList(
//                todoModel1
//        ));

        when(toDoService.findAll()).thenReturn(Lists.newArrayList(
                todoModel1
        ));

//        given(toDoService.findAll()).willReturn(Lists.newArrayList(
//                todoModel1
//                //todoModel2
//        ));

        // List<TodoModel> todoModels = toDoService.findAll();

        List<TodoModel> todoModels = toDoService.findAll();

        TodoModel lastTodo = todoModels.get(todoModels.size() - 1);

        assertEquals(todoModel1.getName(), lastTodo.getName());
        assertEquals(todoModel1.getId(), lastTodo.getId());


    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteToDoById() {
    }
}