package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.controller.advice.ApplicationControllerAdvice;
import com.epam.esm.epammodule3.exception.OrderAlreadyExistsException;
import com.epam.esm.epammodule3.exception.OrderNotFoundException;
import com.epam.esm.epammodule3.model.dto.*;
import com.epam.esm.epammodule3.model.entity.GiftCertificate;
import com.epam.esm.epammodule3.model.entity.Order;
import com.epam.esm.epammodule3.model.entity.User;
import com.epam.esm.epammodule3.service.OrderService;
import com.epam.esm.epammodule3.service.mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    private static final Long USER_ID = 1L;
    private static final Long CERTIFICATE_ID = 1L;
    private static final Long ORDER_ID = 1L;
    @Mock
    private OrderService orderService;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderController subject;
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
    void getOrderById() throws Exception {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();

        User expectedUser = new User(USER_ID, "User");
        UserDto userDto = new UserDto(USER_ID, "User");

        Order expectedOrder = Order.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(expectedUser)
                .giftCertificate(certificate)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(userDto)
                .certificate(certificateDto)
                .build();

        when(orderService.findById(any(Long.class))).thenReturn(expectedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(
                        get("/orders/{id}", ORDER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId().toString()))
                .andExpect(jsonPath("$.user.id").value(orderDto.getUser().getId()))
                .andExpect(jsonPath("$.user.name").value(orderDto.getUser().getName()))
                .andExpect(jsonPath("$.certificate.id").value(orderDto.getCertificate().getId()))
                .andExpect(jsonPath("$.certificate.name").value(orderDto.getCertificate().getName()))
                .andExpect(jsonPath("$.certificate.description").value(orderDto.getCertificate().getDescription()))
                .andExpect(jsonPath("$.price").value(orderDto.getPrice()));

        verify(orderService).findById(ORDER_ID);
        verify(orderMapper).toDto(expectedOrder);
        verifyNoMoreInteractions(orderService, orderMapper);
    }

    @Test
    void getOrderById_whenOrderNotFoundExceptionIsThrows_returns404() throws Exception {
        String errorMessage = "Order not found";

        when(orderService.findById(any(Long.class)))
                .thenThrow(new OrderNotFoundException(errorMessage));

        mockMvc.perform(
                        get("/orders/{id}", ORDER_ID)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(errorMessage));

        verify(orderService).findById(ORDER_ID);
        verifyNoInteractions(orderMapper);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void getAllOrders() throws Exception {
        List<Order> expectedOrders = List.of(new Order());

        Pageable pageable = PageRequest.of(0, 5, Sort.by("price"));
        Page<Order> pageableExpectedOrders = new PageImpl(expectedOrders, pageable, expectedOrders.size());

        when(orderService.findAll(any(Pageable.class))).thenReturn(pageableExpectedOrders);

        mockMvc.perform(
                        get("/orders")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "price,asc")
                )
                .andExpect(status().isOk());

        verify(orderService).findAll(pageable);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void addOrder() throws Exception {
        GiftCertificate expectedCertificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();

        User expectedUser = new User(USER_ID, "User");
        UserDto userDto = new UserDto(USER_ID, "User");

        Order expectedOrder = Order.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(expectedUser)
                .giftCertificate(expectedCertificate)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(userDto)
                .certificate(certificateDto)
                .build();

        when(orderService.create(any(CreateOrderRequest.class))).thenReturn(expectedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        RequestBuilder requestBuilder = post("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(orderDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderDto.getId().toString()))
                .andExpect(jsonPath("$.user.id").value(orderDto.getUser().getId()))
                .andExpect(jsonPath("$.user.name").value(orderDto.getUser().getName()))
                .andExpect(jsonPath("$.certificate.id").value(orderDto.getCertificate().getId()))
                .andExpect(jsonPath("$.certificate.name").value(orderDto.getCertificate().getName()))
                .andExpect(jsonPath("$.certificate.description").value(orderDto.getCertificate().getDescription()))
                .andExpect(jsonPath("$.price").value(orderDto.getPrice()));

        verify(orderMapper).toDto(expectedOrder);
        verifyNoMoreInteractions(orderService, orderMapper);
    }

    @Test
    void addOrder_whenOrderAlreadyExistsExceptionIsThrows_returns409() throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();

        when(orderService.create(any(CreateOrderRequest.class)))
                .thenThrow(new OrderAlreadyExistsException("Order already exists"));

        RequestBuilder requestBuilder = post("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createOrderRequest))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("Order already exists"));

        verifyNoInteractions(orderMapper);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void updateOrder() throws Exception {
        GiftCertificate expectedCertificate = GiftCertificate.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(CERTIFICATE_ID)
                .name("cert")
                .build();

        User expectedUser = new User(USER_ID, "User");
        UserDto userDto = new UserDto(USER_ID, "User");

        Order expectedOrder = Order.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(expectedUser)
                .giftCertificate(expectedCertificate)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(ORDER_ID)
                .price(10.2)
                .user(userDto)
                .certificate(certificateDto)
                .build();

        when(orderService.update(any(UpdateOrderRequest.class))).thenReturn(expectedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        ObjectMapper objectMapper = new ObjectMapper();

        RequestBuilder requestBuilder = patch("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(orderDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderDto.getId().toString()))
                .andExpect(jsonPath("$.user.id").value(orderDto.getUser().getId()))
                .andExpect(jsonPath("$.user.name").value(orderDto.getUser().getName()))
                .andExpect(jsonPath("$.certificate.id").value(orderDto.getCertificate().getId()))
                .andExpect(jsonPath("$.certificate.name").value(orderDto.getCertificate().getName()))
                .andExpect(jsonPath("$.certificate.description").value(orderDto.getCertificate().getDescription()))
                .andExpect(jsonPath("$.price").value(orderDto.getPrice()));

        verify(orderMapper).toDto(expectedOrder);
        verifyNoMoreInteractions(orderService, orderMapper);
    }

    @Test
    void deleteOrderById() throws Exception {
        RequestBuilder requestBuilder = delete("/orders/{id}", ORDER_ID);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());

        verify(orderService).delete(ORDER_ID);
        verifyNoMoreInteractions(orderService);
    }
}