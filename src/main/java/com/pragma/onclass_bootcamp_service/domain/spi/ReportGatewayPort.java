package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampReportRequestDTO;

public interface ReportGatewayPort {

    void notifyBootcampCreated(BootcampReportRequestDTO reportDto);

}
