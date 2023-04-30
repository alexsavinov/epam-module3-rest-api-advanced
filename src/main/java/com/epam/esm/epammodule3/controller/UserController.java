package com.epam.esm.epammodule3.controller;

import com.epam.esm.epammodule3.model.dto.CreateOrderRequest;
import com.epam.esm.epammodule3.model.dto.OrderDto;
import com.epam.esm.epammodule3.model.dto.TagDto;
import com.epam.esm.epammodule3.model.dto.UserDto;
import com.epam.esm.epammodule3.model.entity.Order;
import com.epam.esm.epammodule3.model.entity.Tag;
import com.epam.esm.epammodule3.model.entity.User;
import com.epam.esm.epammodule3.service.GiftCertificateService;
import com.epam.esm.epammodule3.service.OrderService;
import com.epam.esm.epammodule3.service.TagService;
import com.epam.esm.epammodule3.service.UserService;
import com.epam.esm.epammodule3.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final TagService tagService;
    private final OrderMapper orderMapper;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User foundUser = userService.findById(id);
        UserDto userDto = modelMapper.map(foundUser, UserDto.class);

        userDto.add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel());
        return userDto;
    }

    @GetMapping
    public Page<UserDto> getUsers(Pageable pageable) {
        Page<User> foundUsers = userService.findAll(pageable);

        return foundUsers.map(user -> modelMapper.map(user, UserDto.class));
    }

    @PostMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto addOrderForUser(@PathVariable Long userId, @RequestBody CreateOrderRequest createOrderRequest) {
        createOrderRequest.setUserId(userId);

        Order createdOrder = orderService.createForUser(createOrderRequest);
        OrderDto orderDto = orderMapper.toDto(createdOrder);

        orderDto.add(linkTo(methodOn(OrderController.class).getOrderById(orderDto.getId())).withSelfRel());
        return orderDto;
    }

    @GetMapping(value = "/{userId}/orders/{orderId}")
    public OrderDto getOneOrderForUser(@PathVariable Long userId, @PathVariable Long orderId) {
        Order foundOrder = orderService.findByOrderIdAndUserId(orderId, userId);
        OrderDto orderDto = orderMapper.toDto(foundOrder);

        orderDto.add(linkTo(methodOn(OrderController.class).getOrderById(orderDto.getId())).withSelfRel());
        return orderDto;
    }

    @GetMapping(value = "/{id}/orders")
    public Page<OrderDto> getAllOrdersForUser(@PathVariable Long id, Pageable pageable) {
        Page<Order> foundOrders = orderService.findAllByUserId(id, pageable);

        return foundOrders.map(orderMapper::toDto);
    }

    @GetMapping(value = "/{id}/tag")
    public TagDto getTopUsedTag(@PathVariable Long id) {
        Optional<Tag> foundTag = tagService.getTopUsedTag(id);

        TagDto tagDto = modelMapper.map(foundTag.get(), TagDto.class);

        tagDto.add(linkTo(methodOn(TagController.class).getTagById(tagDto.getId())).withSelfRel());
        return tagDto;
    }

    @GetMapping(value = "/{id}/cost")
    public Double getHighestCost(@PathVariable Long id) {
        Double cost = orderService.getHighestCost(id);

        return cost;
    }
}
