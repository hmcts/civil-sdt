package uk.gov.moj.sdt.handlers.cmc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "civl-api", url = "${civil.api.url})")
public interface CivilApi {


    @PostMapping("/breathingSpace")
    Object breathingSpace(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader("ServiceAuthorzation") String serviceAuthorization,
        @RequestBody Object breathingSpaceRequest
    );
}
