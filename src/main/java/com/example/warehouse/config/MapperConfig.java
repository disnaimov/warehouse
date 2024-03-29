package com.example.warehouse.config;

import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entities.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper getMapper(){
       return new ModelMapper();
    }
}
