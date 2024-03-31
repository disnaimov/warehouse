package com.example.warehouse.config;

import org.modelmapper.ModelMapper;
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
       return new ModelMapper();
    }
}
