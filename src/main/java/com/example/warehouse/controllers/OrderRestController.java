package com.example.warehouse.controllers;

import com.example.warehouse.dto.CreateOrderDto;
import com.example.warehouse.dto.OrderGetByIdDto;
import com.example.warehouse.dto.PatchOrderDto;
import com.example.warehouse.enums.OrderStatus;
import com.example.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UUID> create(@RequestHeader Long customerId,
            @RequestBody CreateOrderDto createOrderDto) {
        return new ResponseEntity<>(orderService.create(createOrderDto, customerId), CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderGetByIdDto> getById(@PathVariable("id") UUID id,
                                                   @RequestHeader Long customerId){
        return new ResponseEntity<>(orderService.getById(customerId, id), OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<UUID> patch(@PathVariable("id") UUID orderId,
                                      @RequestHeader Long customerId,
                                      @RequestBody PatchOrderDto patchOrderDto) {

        return new ResponseEntity<>(orderService.patchOrder(patchOrderDto, orderId, customerId), OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") UUID orderId,
                                             @RequestHeader Long customerId) {
        orderService.deleteOrderById(orderId, customerId);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/{id}/confirm", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> confirm(@PathVariable("id") UUID orderId,
                                    @RequestHeader Long customerId) {
        orderService.confirmOrder(orderId, customerId);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PATCH)
    public ResponseEntity<OrderStatus> updateStatus(@PathVariable("id") UUID orderId) {
        return new ResponseEntity<>(orderService.updateStatus(orderId), OK);
    }
}
