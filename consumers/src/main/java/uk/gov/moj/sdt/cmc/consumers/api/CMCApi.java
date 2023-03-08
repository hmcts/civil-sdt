package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.moj.sdt.cmc.consumers.config.CMCConfig;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;


@FeignClient(name = "civil-api",
    url = "${civil.api.url}",
    configuration = CMCConfig.class,
    fallback = CMCApiFallback.class)
public interface CMCApi {

    String IDAM_ID_HEADER = "IDAMID";

    String SDT_REQUEST_ID = "SDTREQUESTID";

    @PostMapping("/breathingSpace")
    BreathingSpaceResponse breathingSpace(
        @RequestBody BreathingSpaceRequest breathingSpaceRequest
    );

    @PostMapping("/claimStatusUpdate")
    ClaimStatusUpdateResponse claimStatusUpdate(
        @RequestHeader(IDAM_ID_HEADER)  String idamId,
        @RequestHeader(SDT_REQUEST_ID) String sdtRequestId,
        @RequestBody ClaimStatusUpdateRequest claimStatusUpdateRequest
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
