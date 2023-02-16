package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.moj.sdt.cmc.consumers.config.CmcConfig;
import uk.gov.moj.sdt.cmc.consumers.model.ICmcRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "civil-api",
    url = "${civil.api.url}",
    configuration = CmcConfig.class,
    fallback = CmcApiFallback.class)
public interface CmcApi {

    @PostMapping("/breathingSpace")
    Object breathingSpace(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader("ServiceAuthorzation") String serviceAuthorization,
        @RequestBody IIndividualRequest individualRequest
    );

    @GetMapping("/claimDefences")
    Object claimDefences(
            @RequestHeader(AUTHORIZATION) String authorisation,
            @RequestHeader("ServiceAuthorzation") String serviceAuthorization,
            @RequestHeader("cmcRequest") ICmcRequest cmcRequest
    );
}
