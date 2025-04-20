package com.example.warehouse.controllers;

import com.example.warehouse.dto.CreateCustomerDto;
import com.example.warehouse.dto.CustomerResponseDto;
import com.example.warehouse.dto.UpdateCustomerDto;
import com.example.warehouse.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("customers")
@RestController
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Long> create(@RequestBody CreateCustomerDto createCustomerDto) {
        return new ResponseEntity<>(customerService.create(createCustomerDto), CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CustomerResponseDto> update(@RequestBody UpdateCustomerDto updateCustomerDto) {
        return new ResponseEntity<>(customerService.update(updateCustomerDto), OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CustomerResponseDto>> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam (required = false, defaultValue = "20") int size) {

        return new ResponseEntity<>(customerService.getAll(PageRequest.of(page, size)), OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CustomerResponseDto> getById(@PathVariable("id") Long id){
        return new ResponseEntity<>(customerService.getById(id), OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteById(@PathVariable("id") Long id) {
        customerService.removeById(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
