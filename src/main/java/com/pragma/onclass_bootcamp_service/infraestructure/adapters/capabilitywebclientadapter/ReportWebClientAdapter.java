package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter;

import com.pragma.onclass_bootcamp_service.domain.spi.ReportGatewayPort;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampReportRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class ReportWebClientAdapter implements ReportGatewayPort {

    private final WebClient webClient;

    public ReportWebClientAdapter(@Qualifier("reportWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void notifyBootcampCreated(BootcampReportRequestDTO reportDto) {
        webClient.post()
                .uri("/api/v1/reports/bootcamps")
                .bodyValue(reportDto)
                .retrieve()
                .toBodilessEntity()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        response -> log.info("Reporte de bootcamp notificado exitosamente ID: {}", reportDto.bootcampId()),
                        error -> log.error("Error al notificar reporte de bootcamp al servicio de reportes", error)
                );
    }
}
