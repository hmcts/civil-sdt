package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.model.BreathingSpaceRequest;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

@Service("MockBreathingSpaceService")
public class MockBreathingSpaceService implements IBreathingSpace {

    @Override
    public void breathingSpace(BreathingSpaceRequest breathingSpaceRequest) {
    }
}
