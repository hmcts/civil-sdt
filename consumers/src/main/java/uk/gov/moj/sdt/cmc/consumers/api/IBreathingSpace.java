package uk.gov.moj.sdt.cmc.consumers.api;

import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

public interface IBreathingSpace {

    Object breathingSpace(IIndividualRequest individualRequest);
}
