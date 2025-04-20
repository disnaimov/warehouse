package com.example.warehouse.service;

import com.example.warehouse.dao.CustomerRepository;
import com.example.warehouse.dto.CreateCustomerDto;
import com.example.warehouse.dto.CustomerDto;
import com.example.warehouse.dto.CustomerResponseDto;
import com.example.warehouse.dto.UpdateCustomerDto;
import com.example.warehouse.entities.Customer;
import com.example.warehouse.exceptions.InvalidEntityDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final ModelMapper mapper;

    private void validation(CustomerDto customerDto) {

        if (customerRepository.findByLogin(customerDto.getLogin()) != null) {
            log.error("received a non-unique login");
            throw new InvalidEntityDataException("Указанный логин уже существует", "INCORRECT_LOGIN", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (customerDto.getLogin() == null || customerDto.getLogin().isBlank()) {
            throw new InvalidEntityDataException("Неккоректное логин: Проверьте правильность ввода и повторите попытку.", "INCORRECT_LOGIN", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (customerDto.getEmail() == null || customerDto.getEmail().isBlank()) {
            throw new InvalidEntityDataException("Неккоректное емейл: Проверьте правильность ввода и повторите попытку.", "INCORRECT_EMAIL", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    public Long create(CreateCustomerDto createCustomerDto) {

        log.info("Validation create customer dto");
        log.debug("Validation create customer dto {}", createCustomerDto);
        CustomerDto customerDto = mapper.map(createCustomerDto, CustomerDto.class);
        validation(customerDto);

        Customer customer = mapper.map(customerDto, Customer.class);
        customer.setActive(true);

        log.info("Saving customer");
        log.debug("Saving customer {}", customer);
        customer = customerRepository.save(customer);

        CustomerResponseDto responseDto = mapper.map(customer, CustomerResponseDto.class);

        log.info("Customer saved");
        log.debug("Customer saved {}", customer);
        return responseDto.getId();
    }

    @Transactional
    public CustomerResponseDto update(UpdateCustomerDto updateCustomerDto) {

        log.info("Validation customer update dto");
        log.debug("Validation customer update dto {}", updateCustomerDto);
        CustomerDto customerDto = mapper.map(updateCustomerDto, CustomerDto.class);
        validation(customerDto);


        Customer customer = customerRepository.findById(updateCustomerDto.getId()).orElseThrow();

        customer.setLogin(customerDto.getLogin());
        customer.setEmail(customerDto.getEmail());
        customer.setActive(true);

        log.info("Update customer");
        log.debug("Update customer {}", customerDto);
        customer = customerRepository.save(customer);

        log.info("Customer updated");
        log.debug("Customer updated {}", customer);
        return mapper.map(customer, CustomerResponseDto.class);
    }

    @Transactional
    public List<CustomerResponseDto> getAll(PageRequest pageRequest) {
        log.info("Getting all customers");
        log.debug("Getting all customers");

        List<Customer> customers = customerRepository.findAll(pageRequest).getContent();
        List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();

        for (Customer c : customers) {
            customerResponseDtos.add(mapper.map(c, CustomerResponseDto.class));
        }

        log.info("All customers received");
        log.debug("All customers received");
        return customerResponseDtos;
    }

    @Transactional
    public CustomerResponseDto getById(Long id) {
        log.info("Getting customer by id");
        log.debug("Getting customer by id {}", id);

        if (customerRepository.findById(id).isPresent()) {
            return mapper.map(customerRepository.findById(id), CustomerResponseDto.class);
        }
        else throw new InvalidEntityDataException("Ошибка: указанный id не существует", "INCORRECT_ID", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public void removeById(Long id) {
        log.info("Removal customer by id");
        log.debug("Removal customer by id {}", id);
        if (customerRepository.findById(id).isPresent()) {
            customerRepository.delete(customerRepository.findById(id).orElseThrow());
        }
        else throw new InvalidEntityDataException("Ошибка: указанный id не существует", "INCORRECT_ID", HttpStatus.NOT_FOUND);
        log.info("Customer by id removed");
        log.debug("Customer by id removed {}", customerRepository.findById(id));
    }
}
