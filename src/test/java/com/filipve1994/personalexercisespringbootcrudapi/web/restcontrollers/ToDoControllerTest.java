package com.filipve1994.personalexercisespringbootcrudapi.web.restcontrollers;

import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.TodoInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.UserInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.TodoModel;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.ToDoService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static com.filipve1994.personalexercisespringbootcrudapi.common.utils.JsonUtil.convertToJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
 * https://medium.com/@sheikarbaz5/spring-boot-with-tdd-test-driven-development-part-i-be1b90da51e
 */
@WebMvcTest(controllers = ToDoController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoService toDoService;

    TodoModel todoModel1;
    TodoModel todoModel2;
    List<TodoModel> todoModels;


    @BeforeEach
    void setUp() {

        todoModel1 = new TodoModel();
        todoModel1.setId(1L);
        todoModel1.setName("Todo1");
        todoModel1.setCompleted(true);

        todoModel2 = new TodoModel();
        todoModel2.setId(2L);
        todoModel2.setName("Todo2");
        todoModel2.setCompleted(true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void serviceInitializedCorrectly() {
        assertThat(this.toDoService).isNotNull();
    }


    @Test
    void findAll() throws Exception {

        todoModels = new ArrayList<>();
        todoModels.add(todoModel1);
        todoModels.add(todoModel2);

        // Mock fake data to use for comparison

        given(toDoService.findAll()).willReturn(todoModels);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/todos")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].name").value("Todo1"))
                .andExpect(jsonPath("$.[0].name").isString())
                .andExpect(jsonPath("$.[0].completed").isBoolean())
                .andExpect(jsonPath("$.[0].completed").value(true))

                ;

        resultActions.andDo(
                document("todos/find-all",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
//                        PayloadDocumentation.responseFields(
//                                PayloadDocumentation.fieldWithPath("[].id").description(""),
//                                PayloadDocumentation.fieldWithPath("[].name").description(""),
//                                PayloadDocumentation.fieldWithPath("[].completed").description("")
//                        )
                        todoCollection()
                        //todoInput(),
                )
        );
    }

    @Test
    void findById() throws Exception {
        todoModels = new ArrayList<>();
        todoModels.add(todoModel1);
        todoModels.add(todoModel2);

        // Mock fake data to use for comparison

        given(toDoService.findById(1L)).willReturn(todoModel1);

        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/todos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

        resultActions
                .andExpect(jsonPath("$.id").value(1));

        resultActions.andDo(
                document("todos/find-by-id",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
//                        PayloadDocumentation.responseFields(
//                                PayloadDocumentation.fieldWithPath(".id").description(""),
//                                PayloadDocumentation.fieldWithPath(".name").description(""),
//                                PayloadDocumentation.fieldWithPath(".completed").description("")
//                        ),
                        //todoInput(),
                        todoModel()
                )
        );
    }

    @Test
    void create() throws Exception {

        todoModels = new ArrayList<>();
        todoModels.add(todoModel1);
        todoModels.add(todoModel2);

        TodoModel todoModelNew = new TodoModel();
        todoModelNew.setId(3L);
        todoModelNew.setName("Todo3");
        todoModelNew.setCompleted(true);

        TodoInput todoInput = new TodoInput();
        todoInput.setName("Todo3");
        todoInput.setCompleted(true);

        // Mock fake data to use for comparison

        given(toDoService.save(any(TodoInput.class))).willReturn(todoModelNew);

        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/todos")
                        .content(convertToJsonString(todoInput))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        resultActions.andDo(
                document("todos/create",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
//                        PayloadDocumentation.responseFields(
//                                PayloadDocumentation.fieldWithPath(".id").description(""),
//                                PayloadDocumentation.fieldWithPath(".name").description(""),
//                                PayloadDocumentation.fieldWithPath(".completed").description("")
//                        ),
                        todoInput(),
                        todoModel()
                )
        );

        ArgumentCaptor<TodoInput> captor = ArgumentCaptor.forClass(TodoInput.class);
        verify(toDoService).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Todo3");
        assertThat(captor.getValue().getCompleted()).isEqualTo(true);
        assertThat(captor.getValue().getCompleted()).isTrue();
    }

    @Test
    void update() throws Exception {

        TodoModel todoModelUpdated = new TodoModel();
        todoModelUpdated.setId(3L);
        todoModelUpdated.setName("Todo3Updated");
        todoModelUpdated.setCompleted(true);

        TodoInput todoInput = new TodoInput();
        todoInput.setName("Todo3Updated");
        todoInput.setCompleted(true);

        // Mock fake data to use for comparison

        given(toDoService.update(anyLong(), any(TodoInput.class))).willReturn(todoModelUpdated);

        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/todos/{id}", 3)
                        .content(convertToJsonString(todoInput))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Todo3Updated"));

        resultActions.andDo(
                document("todos/update",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
//                        PayloadDocumentation.responseFields(
//                                PayloadDocumentation.fieldWithPath(".id").description(""),
//                                PayloadDocumentation.fieldWithPath(".name").description(""),
//                                PayloadDocumentation.fieldWithPath(".completed").description("")
//                        ),
                        todoInput(),
                        todoModel()
                )
        );
    }

    @Test
    void deleteById() throws Exception {

        todoModels = new ArrayList<>();
        todoModels.add(todoModel1);
        todoModels.add(todoModel2);

        TodoModel todoModelUpdated = new TodoModel();
        todoModelUpdated.setId(3L);
        todoModelUpdated.setName("Todo3Updated");
        todoModelUpdated.setCompleted(true);

        // Mock fake data to use for comparison

        // given(toDoService.deleteToDoById(anyLong()));

        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/todos/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                //.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.id").value(3))
//                .andExpect(jsonPath("$.name").value("Todo3Updated"))
                ;

        resultActions.andDo(
                document("todos/deletebyid",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
//                        PayloadDocumentation.responseFields(
//                                PayloadDocumentation.fieldWithPath("[].id").description(""),
//                                PayloadDocumentation.fieldWithPath("[].name").description("")
//                        )
                )
        );
    }

    private ResponseFieldsSnippet todoModel() {
        return PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath(".id").description(""),
                PayloadDocumentation.fieldWithPath(".name").description(""),
                PayloadDocumentation.fieldWithPath(".completed").description("")
        );
    }

    private ResponseFieldsSnippet todoCollection() {
        return PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("[].id").description(""),
                PayloadDocumentation.fieldWithPath("[].name").description(""),
                PayloadDocumentation.fieldWithPath("[].completed").description("")
        );
    }

    private RequestFieldsSnippet todoInput() {
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(TodoInput.class);
        return PayloadDocumentation.requestFields(
                PayloadDocumentation.fieldWithPath("name").description("The name of the todo")
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("name"))),
                PayloadDocumentation.fieldWithPath("completed").description("is the todo entry completed or not?")
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("completed")))
        );
    }
}