package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.moj.sdt.cmc.consumers.config.CmcConfig;
import uk.gov.moj.sdt.cmc.consumers.model.BreathingSpaceRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "civil-api",
    url = "${civil.api.url}",
    configuration = CmcConfig.class,
    fallback = CmcApiFallback.class)
public interface CmcApi {

    @PostMapping("/breathingSpace")
    void breathingSpace(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader("ServiceAuthorization") String serviceAuthorization,
        @RequestBody BreathingSpaceRequest breathingSpaceRequest
    );

    @PostMapping("/claimStatusUpdate")
    Object claimStatusUpdate(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader("ServiceAuthorization") String serviceAuthorization,
        @RequestHeader("idAmId") String idAmId,
        @RequestHeader("sdtRequestId") String sdtRequestId,
        @RequestBody ClaimStatusUpdate claimStatusUpdateObj
    );
}
