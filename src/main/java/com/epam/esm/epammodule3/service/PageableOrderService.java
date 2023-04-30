package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageableOrderService {

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByUserId(Long id, Pageable pageable);
}
