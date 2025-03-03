package com.mindfire.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;
@Configuration
public class EmployeeConfig {
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
}
