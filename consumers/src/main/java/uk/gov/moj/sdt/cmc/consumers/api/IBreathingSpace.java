package uk.gov.moj.sdt.cmc.consumers.api;

import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;

public interface IBreathingSpace {

    BreathingSpaceResponse breathingSpace(BreathingSpaceRequest breathingSpaceRequest);
}
