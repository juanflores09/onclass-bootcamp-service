package com.pragma.onclass_bootcamp_service.application.config;

import com.pragma.onclass_bootcamp_service.domain.api.CreateBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.api.DeleteBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.api.FindBootcampsServicePort;
import com.pragma.onclass_bootcamp_service.domain.api.GetMostPopularBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGatewayPort;
import com.pragma.onclass_bootcamp_service.domain.spi.PersonGatewayPort;
import com.pragma.onclass_bootcamp_service.domain.spi.ReportGatewayPort;
import com.pragma.onclass_bootcamp_service.domain.usecase.CreateBootcampUseCase;
import com.pragma.onclass_bootcamp_service.domain.usecase.DeleteBootcampUseCase;
import com.pragma.onclass_bootcamp_service.domain.usecase.GetMostPopularBootcampUseCase;
import com.pragma.onclass_bootcamp_service.domain.usecase.ListBootcampUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateBootcampServicePort bootcampServicePort(
            BootcampPersistencePort bootcampPersistencePort,
            CapabilityGatewayPort capabilityGateway,
            ReportGatewayPort reportGatewayPort){
        return new CreateBootcampUseCase(bootcampPersistencePort, capabilityGateway, reportGatewayPort);
    }

    @Bean
    public FindBootcampsServicePort findCapabilitiesServicePort(
            BootcampPersistencePort capabilityPersistencePort,
            CapabilityGatewayPort technologyValidatorGateway){
        return new ListBootcampUseCase(capabilityPersistencePort, technologyValidatorGateway);
    }

    @Bean
    public DeleteBootcampServicePort deleteBootcampServicePort(
            BootcampPersistencePort bootcampPersistencePort,
            CapabilityGatewayPort capabilityGateway){
        return new DeleteBootcampUseCase(bootcampPersistencePort, capabilityGateway);
    }

    @Bean
    public GetMostPopularBootcampServicePort getMostPopularBootcampServicePort(
            BootcampPersistencePort bootcampPersistencePort,
            PersonGatewayPort personGateway,
            CapabilityGatewayPort capabilityGateway){
        return new GetMostPopularBootcampUseCase(bootcampPersistencePort, personGateway, capabilityGateway);
    }
}
