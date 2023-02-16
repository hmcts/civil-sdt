package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.model.ICmcRequest;

@Service("MockClaimDefencesService")
public class MockClaimDefencesService implements IClaimDefences {

    @Override
    public Object claimDefences(ICmcRequest cmcRequest) {
        return null;
    }
}
