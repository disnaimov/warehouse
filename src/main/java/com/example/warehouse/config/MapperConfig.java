package com.example.warehouse.config;

import com.example.warehouse.dto.OrderResponseDto;
import com.example.warehouse.entities.Order;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 * Mapper configuration class
 */
@Configuration
public class MapperConfig {
    /**
     * Creating mapper bean
     * @return new ModelMapper object
     */
    @Bean
    public ModelMapper getMapper(){
       ModelMapper mapper = new ModelMapper();
        TypeMap<Order, OrderResponseDto> typeMap = mapper.createTypeMap(Order.class, OrderResponseDto.class);
        typeMap.addMapping(order -> order.getCustomer().getId(), OrderResponseDto::setCustomerId);

        return mapper;
    }
}
