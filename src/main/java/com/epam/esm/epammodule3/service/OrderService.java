package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.dto.CreateOrderRequest;
import com.epam.esm.epammodule3.model.dto.UpdateOrderRequest;
import com.epam.esm.epammodule3.model.entity.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OrderService extends PageableOrderService {

    Order findById(Long id);

    Order create(CreateOrderRequest createRequest);

    Order update(UpdateOrderRequest updateRequest);

    void delete(Long id);

    Order findByOrderIdAndUserId(Long orderId, Long userId);

    Optional<Order> findByGiftCertificateId(Long certId);

    @Transactional
    Order createForUser(CreateOrderRequest createRequest);

    Double getHighestCost(Long id);
}
