package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.moj.sdt.cmc.consumers.config.CMCConfig;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;


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
}
