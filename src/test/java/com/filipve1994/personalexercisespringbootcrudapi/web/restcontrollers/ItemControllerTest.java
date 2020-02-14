package com.filipve1994.personalexercisespringbootcrudapi.web.restcontrollers;

import com.filipve1994.personalexercisespringbootcrudapi.common.utils.JsonUtil;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.ItemInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.UserInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.Item;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.User;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.ItemService;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.ToDoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * https://www.onlinetutorialspoint.com/spring-boot/spring-boot-mockmvc-junit-test-example.html
 * https://dimitr.im/testing-your-rest-controllers-and-clients-with-spring
 * https://dimitr.im/unit-testing-mockito-assertj
 */
@WebMvcTest(controllers = ItemController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {

        List<Item> items = Arrays.asList(new Item(1, "iPhoneX", "Mobiles"));
        Mockito.when(itemService.findAllItems()).thenReturn(items);

        mockMvc.perform(
                get("/api/items/all")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("iPhoneX")))
                .andExpect(jsonPath("$.[0].category", is("Mobiles")))
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                                //,
                                //userCollection()
                        )
                );
    }

    @Test
    void findById() throws Exception {
        Item item = new Item(1, "iPhoneX", "Mobiles");
        Mockito.when(itemService.findById(1)).thenReturn(item);

        mockMvc.perform(
                get("/api/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("iPhoneX")))
                .andExpect(jsonPath("$.category", is("Mobiles")))
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                                //,
                                //userCollection()
                        )
                );

        Mockito.verify(itemService).findById(1);

    }

    @Test
    void create() throws Exception {

        Item item = new Item(1, "iPhoneX", "Mobiles");
//        Mockito.when(itemService.addItem(item)).thenReturn(item);

        ItemInput itemInput = new ItemInput("iPhoneX", "Mobiles");
        Mockito.when(itemService.addItem(itemInput)).thenReturn(item);

        mockMvc.perform(
                post("/api/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.convertToJsonString(item))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhoneX"))
                .andExpect(jsonPath("$.category").value("Mobiles"))
                .andDo(print())
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                                //,
                                //userCollection()
                        )
                );

        ArgumentCaptor<ItemInput> anyInput = ArgumentCaptor.forClass(ItemInput.class);
        verify(itemService).addItem(anyInput.capture());
        assertThat(anyInput.getValue().getName()).isEqualTo("iPhoneX");
        assertThat(anyInput.getValue().getCategory()).isEqualTo("Mobiles");
    }

    @Test
    void update() throws Exception {

        Item itemUpdated = new Item(1, "iPhoneX", "Mobiles");

        ItemInput itemInput = new ItemInput("iPhoneX", "Mobiles");

        Mockito.when(itemService.updateItem(anyInt(), any(ItemInput.class))).thenReturn(itemUpdated);

        mockMvc.perform(
                put("/api/items/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.convertToJsonString(itemInput))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhoneX"))
                .andExpect(jsonPath("$.category").value("Mobiles"))
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                                //,
                                //userCollection()
                        )
                );

        ArgumentCaptor<ItemInput> anyInput = ArgumentCaptor.forClass(ItemInput.class);
        verify(itemService).updateItem(anyInt(), anyInput.capture());
        assertThat(anyInput.getValue().getName()).isEqualTo("iPhoneX");
        assertThat(anyInput.getValue().getCategory()).isEqualTo("Mobiles");
    }

    @Test
    void deleteById() throws Exception {

        mockMvc.perform(
                delete("/api/items/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}"
                        )
                );
    }
}