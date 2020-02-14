package com.filipve1994.personalexercisespringbootcrudapi.web.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.filipve1994.personalexercisespringbootcrudapi.common.utils.JsonUtil;
import com.filipve1994.personalexercisespringbootcrudapi.errorhandling.exceptions.UserNotFoundException;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.dto.UserInput;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.models.User;
import com.filipve1994.personalexercisespringbootcrudapi.persistence.services.UserService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        // https://reflectoring.io/configuring-localdate-serialization-spring-boot/
        // https://codeboje.de/jackson-java-8-datetime-handling/
        // https://howtoprogram.xyz/2017/12/30/serialize-java-8-localdate-jackson/
        // https://stackoverflow.com/questions/28802544/java-8-localdate-jackson-format
        //
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void findAllNoPagingShouldReturnListOfUsers() throws Exception {
        when(userService.findAll()).thenReturn(Lists.newArrayList(
                new User(1L, "Doe", null, "John", LocalDate.of(2010, 1, 1), 0),
                new User(2L, "Doe", "Foo", "Jane", LocalDate.of(1999, 12, 31), 2))
        );

        mockMvc.perform(
                get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].lastName", is("Doe")))
                .andExpect(jsonPath("$.[0].middleName", nullValue()))
                .andExpect(jsonPath("$.[0].firstName", is("John")))
                .andExpect(jsonPath("$.[0].dateOfBirth", is("2010-01-01")))
                .andExpect(jsonPath("$.[0].siblings", is(0)))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].lastName", is("Doe")))
                .andExpect(jsonPath("$.[1].middleName", is("Foo")))
                .andExpect(jsonPath("$.[1].firstName", is("Jane")))
                .andExpect(jsonPath("$.[1].dateOfBirth", is("1999-12-31")))
                .andExpect(jsonPath("$.[1].siblings", is(2)))
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                userCollection()
                        )
                );
    }

    @Test
    public void findAllShouldReturnListOfUsers() throws Exception {
        when(userService.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Lists.newArrayList(
                new User(1L, "Doe", null, "John", LocalDate.of(2010, 1, 1), 0),
                new User(2L, "Doe", "Foo", "Jane", LocalDate.of(1999, 12, 31), 2))));

        mockMvc.perform(
                get("/api/user/paging?page=2&size=5")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].lastName", is("Doe")))
                .andExpect(jsonPath("$.[0].middleName", nullValue()))
                .andExpect(jsonPath("$.[0].firstName", is("John")))
                .andExpect(jsonPath("$.[0].dateOfBirth", is("2010-01-01")))
                .andExpect(jsonPath("$.[0].siblings", is(0)))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].lastName", is("Doe")))
                .andExpect(jsonPath("$.[1].middleName", is("Foo")))
                .andExpect(jsonPath("$.[1].firstName", is("Jane")))
                .andExpect(jsonPath("$.[1].dateOfBirth", is("1999-12-31")))
                .andExpect(jsonPath("$.[1].siblings", is(2)))
                .andExpect(header().longValue("X-Users-Total", 2L))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pageParameters(), userCollection(), pageHeaders()));
        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(userService).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    public void findAllShouldDefaultToPageOneAndSizeTen() throws Exception {
        when(userService.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Lists.newArrayList(
                new User(1L, "Doe", null, "John", LocalDate.of(2010, 1, 1), 0),
                new User(2L, "Doe", "Foo", "Jane", LocalDate.of(1999, 12, 31), 2))));

        mockMvc.perform(
                get("/api/user/paging")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].lastName", is("Doe")))
                .andExpect(jsonPath("$.[0].middleName", nullValue()))
                .andExpect(jsonPath("$.[0].firstName", is("John")))
                .andExpect(jsonPath("$.[0].dateOfBirth", is("2010-01-01")))
                .andExpect(jsonPath("$.[0].siblings", is(0)))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].lastName", is("Doe")))
                .andExpect(jsonPath("$.[1].middleName", is("Foo")))
                .andExpect(jsonPath("$.[1].firstName", is("Jane")))
                .andExpect(jsonPath("$.[1].dateOfBirth", is("1999-12-31")))
                .andExpect(jsonPath("$.[1].siblings", is(2)))
                .andExpect(header().longValue("X-Users-Total", 2L))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pageParameters(), userCollection()));
        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(userService).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(1);
        assertThat(captor.getValue().getPageSize()).isEqualTo(10);
    }


    @Test
    public void findAllShouldReturnErrorIfPageIsNegative() throws Exception {
        when(userService.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Lists.newArrayList(
                new User(1L, "Doe", null, "John", LocalDate.of(2010, 1, 1), 0),
                new User(2L, "Doe", "Foo", "Jane", LocalDate.of(1999, 12, 31), 2))));

        mockMvc.perform(get("/api/user/paging?page=-1")
                //.accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("findAllWithPaging.page")))
//                .andExpect(jsonPath("$.subErrors.[0].codes", is("findAllWithPaging.page")))
                .andExpect(jsonPath("$.subErrors.[0].message", is("Page number should be a positive number")))
                .andDo(print())
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pageParameters(), apiError()));
    }

    @Test
    public void findAllShouldReturnErrorIfPageSizeIsNegative() throws Exception {
        when(userService.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Lists.newArrayList(
                new User(1L, "Doe", null, "John", LocalDate.of(2010, 1, 1), 0),
                new User(2L, "Doe", "Foo", "Jane", LocalDate.of(1999, 12, 31), 2))));
        mockMvc.perform(get("/api/user/paging?size=-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("findAllWithPaging.size")))
//                .andExpect(jsonPath("$.subErrors.[0].codes", is("findAllWithPaging.size")))
                .andExpect(jsonPath("$.subErrors.[0].message", is("Page size should be a positive number")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pageParameters(), apiError()));
    }

    @Test
    public void saveShouldReturnUser() throws Exception {
        when(userService.save(any())).thenReturn(new User(3L, "Doe", "Bar", "Joe", LocalDate.of(2000, 1, 1), 4));

        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"Doe\",\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":4}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.middleName", is("Bar")))
                .andExpect(jsonPath("$.firstName", is("Joe")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-01")))
                .andExpect(jsonPath("$.siblings", is(4)))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), user()));
        ArgumentCaptor<UserInput> captor = ArgumentCaptor.forClass(UserInput.class);
        verify(userService).save(captor.capture());
        // assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getLastName()).isEqualTo("Doe");
        assertThat(captor.getValue().getMiddleName()).isEqualTo("Bar");
        assertThat(captor.getValue().getFirstName()).isEqualTo("Joe");
        assertThat(captor.getValue().getDateOfBirth()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(captor.getValue().getSiblings()).isEqualTo(4);
    }

    @Test
    public void saveShouldReturnErrorIfFirstNameIsEmpty() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName(null);
        userInput.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"lastName\":Doe\",\"middleName\":\"Bar\",\"firstName\":\"null\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":4}"))
                        .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.subErrors.[0].codes", is("findAllWithPaging.size")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("NotNull", "NotNull.firstName", "NotNull.userInput.firstName", "NotNull.java.lang.String")))
                .andExpect(jsonPath("$.subErrors.[0].message", is("First name should not be empty")))

                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfFirstNameIsTooLong() throws Exception {

        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("should be less than sixty characters otherwise we get an error");
        userInput.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"lastName\":\"Doe\",\"middleName\":\"Bar\",\"firstName\":\"should be less than sixty characters otherwise we get an error\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":4}"))
                        .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("First name should be between 1 and 60 characters")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("Size", "Size.firstName", "Size.userInput.firstName", "Size.java.lang.String")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfLastNameIsEmpty() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setLastName(null);
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":null,\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":4}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.subErrors.[0].codes", is("findAllWithPaging.size")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("NotNull", "NotNull.lastName", "NotNull.userInput.lastName", "NotNull.java.lang.String")))
                .andExpect(jsonPath("$.subErrors.[0].message", is("Last name should not be empty")))

                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfLastNameIsTooLong() throws Exception {

        UserInput userInput = new UserInput();
        userInput.setLastName("should be less than sixty characters otherwise we get an error");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"should be less than sixty characters otherwise we get an error\",\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":4}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("Last name should be between 1 and 60 characters")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("Size", "Size.lastName", "Size.userInput.lastName", "Size.java.lang.String")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfMiddleNameIsTooLong() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("should be less than sixty characters otherwise we get an error");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"Doe\",\"middleName\":\"should be less than sixty characters otherwise we get an error\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":4}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("Middle name should be at most 60 characters")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("Size", "Size.middleName", "Size.userInput.middleName", "Size.java.lang.String")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfDateOfBirthIsEmpty() throws Exception {

        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(null);
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"Doe\",\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":null,\"siblings\":4}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("Date of birth should not be empty")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("NotNull", "NotNull.dateOfBirth", "NotNull.userInput.dateOfBirth", "NotNull.java.time.LocalDate")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfDateOfBirthIsInFuture() throws Exception {

        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2999,1,1));
        userInput.setSiblings(4);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"Doe\",\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2999-01-01\",\"siblings\":4}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("Date of birth should be in the past")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("Past", "Past.dateOfBirth", "Past.userInput.dateOfBirth", "Past.java.time.LocalDate")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfSiblingsIsNull() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2000,1,1));
        userInput.setSiblings(null);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"Doe\",\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":null}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("The amount of siblings should not be empty")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("NotNull", "NotNull.siblings", "NotNull.userInput.siblings", "NotNull.java.lang.Integer")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void saveShouldReturnErrorIfSiblingsIsNegative() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setLastName("Doe");
        userInput.setMiddleName("Bar");
        userInput.setFirstName("Joe");
        userInput.setDateOfBirth(LocalDate.of(2000,1,1));
        userInput.setSiblings(-2);

        mockMvc.perform(post("/api/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"lastName\":\"Doe\",\"middleName\":\"Bar\",\"firstName\":\"Joe\",\"dateOfBirth\":\"2000-01-01\",\"siblings\":-2}")
                .content(objectMapper.writeValueAsString(userInput))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors.[0].message", is("The amount of siblings should be positive")))
                .andExpect(jsonPath("$.subErrors.[0].codes", containsInAnyOrder("PositiveOrZero", "PositiveOrZero.siblings", "PositiveOrZero.userInput.siblings", "PositiveOrZero.java.lang.Integer")))
                .andDo(document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        userInput(), apiError()));
    }

    @Test
    public void findOneShouldReturnUser() throws Exception {
        when(userService.findById(1L)).thenReturn(new User(1L, "Doe", null, "John", LocalDate.of(2010, 1, 1), 0));
        mockMvc.perform(get("/api/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.middleName", nullValue()))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.dateOfBirth", is("2010-01-01")))
                .andExpect(jsonPath("$.siblings", is(0)))
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("The unique identifier of the user")
                                ), user()
                        )
                );
    }

    @Test
    public void findOneShouldReturnErrorIfNotFound() throws Exception {
//        when(userService.findById(-1L)).thenReturn(Optional.empty());
        when(userService.findById(-1L)).thenThrow(new UserNotFoundException("User not found with id : " + -1));

        mockMvc.perform(get("/api/user/{id}", -1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id : -1")))
                .andExpect(jsonPath("$.debugMessage", is("Exception class used: UserNotFoundException")))
                .andDo(
                        document("{method-name}",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("The unique identifier of the user")
                                ), apiError()
                        )
                );
    }

    @Test
    void create() {
    }


    private ResponseFieldsSnippet apiError() {
        return responseFields(
                fieldWithPath(".status").description("The HTTP STATUS"),
                fieldWithPath(".statusCode").description("The HTTP STATUS numeric"),
                fieldWithPath(".timestamp").description("The time that the error was thrown"),
                fieldWithPath(".subErrors").description("A list of technical codes describing the error"),
                fieldWithPath(".message").description("A message describing the error").optional(),
                fieldWithPath(".debugMessage").description("A message describing the error").optional(),
                fieldWithPath(".subErrors").description("All validation errors").optional(),
                fieldWithPath(".subErrors.[].object").description("From which place is the error thrown").optional().type(String.class.getSimpleName()),
                fieldWithPath(".subErrors.[].field").description("From which parameter is the error thrown").optional().type(String.class.getSimpleName()),
                fieldWithPath(".subErrors.[].rejectedValue").description("What was the value that was rejected?").optional().type(Object.class.getSimpleName()),
                fieldWithPath(".subErrors.[].message").description("What was the message given for the error?").optional().type(String.class.getSimpleName()),
                fieldWithPath(".subErrors.[].codes").description("What was the path given for the error?").optional().type(String[].class.getSimpleName())
        );
    }

    private ResponseFieldsSnippet user() {
        return responseFields(
                fieldWithPath("id").description("The unique identifier of the user"),
                fieldWithPath("lastName").description("The last name of the user"),
                fieldWithPath("middleName").description("The optional middle name of the user").optional(),
                fieldWithPath("firstName").description("The first name of the user"),
                fieldWithPath("dateOfBirth").description("The birthdate of the user in ISO 8601 format"),
                fieldWithPath("siblings").description("The amount of siblings the user has"));
    }

    private RequestFieldsSnippet userInput() {
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(UserInput.class);
        return requestFields(
                fieldWithPath("lastName").description("The last name of the user")
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("lastName"))),
                fieldWithPath("middleName").description("The optional middle name of the user").optional()
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("middleName"))),
                fieldWithPath("firstName").description("The first name of the user")
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("firstName"))),
                fieldWithPath("dateOfBirth").description("The birthdate of the user in ISO 8601 format")
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("dateOfBirth"))),
                fieldWithPath("siblings").description("The amount of siblings the user has")
                        .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("siblings"))));
    }

    private RequestParametersSnippet pageParameters() {
        return requestParameters(
                parameterWithName("page").description("The page to retrieve").optional(),
                parameterWithName("size").description("The number of elements within a single page").optional()
        );
    }

    private ResponseFieldsSnippet userCollection() {
        return responseFields(
                fieldWithPath("[].id").description("The unique identifier of the user"),
                fieldWithPath("[].lastName").description("The last name of the user"),
                fieldWithPath("[].middleName").description("The optional middle name of the user").optional(),
                fieldWithPath("[].firstName").description("The first name of the user"),
                fieldWithPath("[].dateOfBirth").description("The birthdate of the user in ISO 8601 format"),
                fieldWithPath("[].siblings").description("The amount of siblings the user has"));
    }

    private ResponseHeadersSnippet pageHeaders() {
        return responseHeaders(headerWithName("X-Users-Total").description("The total amount of users"));
    }
}