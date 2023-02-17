package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;

@Service("MockClaimDefencesService")
public class MockClaimDefencesService implements IClaimDefences {

    @Override
    public Object claimDefences(String fromDate,
                                String toDate) {
        return null;
    }
}
