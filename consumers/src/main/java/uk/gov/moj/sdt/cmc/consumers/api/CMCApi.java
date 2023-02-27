package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.moj.sdt.cmc.consumers.config.CMCConfig;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;

import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "civil-api",
    url = "${civil.api.url}",
    configuration = CMCConfig.class,
    fallback = CMCApiFallback.class)
public interface CMCApi {

    @PostMapping("/breathingSpace")
    BreathingSpaceResponse breathingSpace(
        @RequestBody BreathingSpaceRequest breathingSpaceRequest
    );

    @PostMapping("/claimStatusUpdate")
    Object claimStatusUpdate(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader("ServiceAuthorization") String serviceAuthorization,
        @RequestHeader("idAmId") String idAmId,
        @RequestHeader("sdtRequestId") String sdtRequestId,
        @RequestBody ClaimStatusUpdateRequest claimStatusUpdateRequestObj
    );

    @GetMapping("/claimDefences")
    ClaimDefencesResponse claimDefences(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader("ServiceAuthorzation") String serviceAuthorization,
            @RequestHeader("idAmId") String idAmId,
            @RequestHeader("fromDateTime") String fromDateTime,
            @RequestHeader("toDate") String toDate
    );
}
