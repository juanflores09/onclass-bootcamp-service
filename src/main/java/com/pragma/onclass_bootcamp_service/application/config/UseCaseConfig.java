package com.pragma.onclass_bootcamp_service.application.config;

import com.pragma.onclass_bootcamp_service.domain.api.CreateBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import com.pragma.onclass_bootcamp_service.domain.usecase.CreateBootcampUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateBootcampServicePort bootcampServicePort(
            BootcampPersistencePort bootcampPersistencePort,
            CapabilityGateway capabilityGateway){
        return new CreateBootcampUseCase(bootcampPersistencePort, capabilityGateway);
    }
}
