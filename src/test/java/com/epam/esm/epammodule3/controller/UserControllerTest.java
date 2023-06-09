package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.controller.advice.ApplicationControllerAdvice;
import com.epam.esm.epammodule3.exception.UserNotFoundException;
import com.epam.esm.epammodule3.model.dto.*;
import com.epam.esm.epammodule3.model.entity.User;
import com.epam.esm.epammodule3.service.OrderService;
import com.epam.esm.epammodule3.service.TagService;
import com.epam.esm.epammodule3.service.UserService;
import com.epam.esm.epammodule3.service.mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Long USER_ID = 1L;
    @InjectMocks
    private UserController subject;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new ApplicationControllerAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getUserById() throws Exception {
        User expectedUser = new User();
        UserDto userDto = new UserDto(USER_ID, "User1");

        when(userService.findById(any(Long.class))).thenReturn(expectedUser);
        when(modelMapper.map(any(User.class), any(Class.class))).thenReturn(userDto);

        mockMvc.perform(
                        get("/users/{id}", USER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));

        verify(userService).findById(USER_ID);
        verify(modelMapper).map(expectedUser, UserDto.class);
        verifyNoMoreInteractions(userService, modelMapper);
    }

    @Test
    void getUserById_whenUserNotFoundExceptionIsThrows_returns404() throws Exception {
        String errorMessage = "User not found";

        when(userService.findById(any(Long.class))).thenThrow(new UserNotFoundException(errorMessage));

        mockMvc.perform(
                        get("/users/{id}", USER_ID)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(errorMessage));

        verify(userService).findById(USER_ID);
        verifyNoInteractions(modelMapper);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUsers() throws Exception {
        User expectedUser = User.builder().id(USER_ID).name("User").build();

        List<User> expectedUsers = List.of(expectedUser);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("name"));
        Page<User> pageableExpectedUsers = new PageImpl(expectedUsers, pageable, expectedUsers.size());

        when(userService.findAll(any(Pageable.class))).thenReturn(pageableExpectedUsers);

        mockMvc.perform(
                        get("/users")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "name,asc")
                )
                .andExpect(status().isOk());

        verify(userService).findAll(pageable);
        verifyNoMoreInteractions(userService);
    }
}