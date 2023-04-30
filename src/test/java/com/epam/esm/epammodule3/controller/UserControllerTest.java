package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.controller.advice.ApplicationControllerAdvice;
import com.epam.esm.epammodule3.exception.UserNotFoundException;
import com.epam.esm.epammodule3.model.dto.*;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Order;
import com.epam.esm.epammodule3.model.entity.Tag;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Long USER_ID = 1L;
    private static final Long CERTIFICATE_ID = 1L;
    private static final Long ORDER_ID = 1L;
    private static final Long TAG_ID = 1L;
    @InjectMocks
    private UserController subject;
    @Mock
    private UserService userService;
    @Mock
    private OrderService orderService;
    @Mock
    private TagService tagService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private OrderMapper orderMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new ApplicationControllerAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper = new ObjectMapper();
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

    @Test
    void addOrderForUser() throws Exception {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();
        GiftCertificateDto certificateDtoRequest = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();

        User user = new User(USER_ID, "User");
        UserDto userDto = new UserDto(USER_ID, "User");

        Order createdOrder = Order.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(user)
                .giftCertificate(certificate)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(userDto)
                .certificate(certificateDtoRequest)
                .build();

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(USER_ID);
        createOrderRequest.setCertificateId(CERTIFICATE_ID);

        when(orderService.createForUser(any(CreateOrderRequest.class))).thenReturn(createdOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        RequestBuilder requestBuilder = post("/users/{userId}/orders", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createdOrder))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdOrder.getId().toString()))
                .andExpect(jsonPath("$.price").value(createdOrder.getPrice()))
                .andExpect(jsonPath("$.user.id").value(createdOrder.getUser().getId()))
                .andExpect(jsonPath("$.certificate.id").value(createdOrder.getGiftCertificate().getId()));

        verify(orderMapper).toDto(createdOrder);
        verifyNoMoreInteractions(userService, orderMapper);
    }

    @Test
    void getOneOrderForUser() throws Exception {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();
        GiftCertificateDto certificateDtoRequest = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();

        User user = new User(USER_ID, "User");
        UserDto userDto = new UserDto(USER_ID, "User");

        Order expectedOrder = Order.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(user)
                .giftCertificate(certificate)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(userDto)
                .certificate(certificateDtoRequest)
                .build();

        when(orderService.findByOrderIdAndUserId(any(Long.class), any(Long.class))).thenReturn(expectedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(
                        get("/users/{userId}/orders/{orderId}", USER_ID, ORDER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedOrder.getId().toString()))
                .andExpect(jsonPath("$.price").value(expectedOrder.getPrice()))
                .andExpect(jsonPath("$.user.id").value(expectedOrder.getUser().getId()))
                .andExpect(jsonPath("$.certificate.id").value(expectedOrder.getGiftCertificate().getId()));

        verify(orderService).findByOrderIdAndUserId(USER_ID, ORDER_ID);
        verify(orderMapper).toDto(expectedOrder);
        verifyNoMoreInteractions(orderService, orderMapper);
    }


    @Test
    void getAllOrdersForUser() throws Exception {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();
        GiftCertificateDto certificateDtoRequest = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();

        User user = new User(USER_ID, "User");
        UserDto userDto = new UserDto(USER_ID, "User");

        Order expectedOrder = Order.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(user)
                .giftCertificate(certificate)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(userDto)
                .certificate(certificateDtoRequest)
                .build();


        List<Order> expectedOrders = List.of(expectedOrder);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> pageableExpectedOrders = new PageImpl(expectedOrders, pageable, expectedOrders.size());

        when(orderService.findAllByUserId(any(Long.class), any(Pageable.class))).thenReturn(pageableExpectedOrders);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(
                        get("/users/{id}/orders", USER_ID)
                                .param("page", "0")
                                .param("size", "5")
                )
                .andExpect(status().isOk());

        verify(orderService).findAllByUserId(USER_ID, pageable);
        verify(orderMapper).toDto(expectedOrder);
        verifyNoMoreInteractions(orderService, orderMapper);
    }

    @Test
    void getTopUsedTag() throws Exception {
        Tag expectedTag = new Tag();
        TagDto tagDto = new TagDto(TAG_ID, "Tag");

        when(tagService.getTopUsedTag(any(Long.class))).thenReturn(Optional.of(expectedTag));
        when(modelMapper.map(any(Tag.class), any(Class.class))).thenReturn(tagDto);

        mockMvc.perform(
                        get("/users/{id}/tag", TAG_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tagDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));

        verify(tagService).getTopUsedTag(TAG_ID);
        verify(modelMapper).map(expectedTag, TagDto.class);
        verifyNoMoreInteractions(tagService, modelMapper);
    }

    @Test
    void getHighestCost() throws Exception {
        when(orderService.getHighestCost(any(Long.class))).thenReturn(100.11);

        mockMvc.perform(
                        get("/users/{id}/cost", USER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("100.11"));

        verify(orderService).getHighestCost(USER_ID);
        verifyNoMoreInteractions(orderService);
    }
}