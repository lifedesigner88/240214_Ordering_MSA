package com.example.ordering.ordering.controller;

import com.example.ordering.common.CommonResponse;
import com.example.ordering.ordering.domain.Ordering;
import com.example.ordering.ordering.dto.OrderReqDto;
import com.example.ordering.ordering.dto.OrderResDto;
import com.example.ordering.ordering.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

    private final OrderService service;

    public OrderController(@Autowired OrderService service) {
        this.service = service;
    }

    //    Create
    @PostMapping("/order/create")
    public ResponseEntity<CommonResponse> createOrder(@RequestBody List<OrderReqDto> dtos, @RequestHeader("myEmail") String email) {
        Ordering ordering = service.createOrder(dtos, email);
        return new ResponseEntity<>(
                new CommonResponse(
                        HttpStatus.CREATED,
                        "Item Successfully Created",
                        ordering.getId()
                ),
                HttpStatus.CREATED
        );
    }

    //    Read
    @GetMapping("/orders")
    public List<OrderResDto> orderList() {
        return service.orderList();
    }

    //    Cancel
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponse> cancelOrder(@PathVariable Long id) {
        Ordering ordering = service.cancelOrder(id);
        return new ResponseEntity<>(
                new CommonResponse(
                        HttpStatus.CREATED,
                        "order Successfully canceled",
                        ordering.getId()
                ),
                HttpStatus.CREATED
        );
    }

}


