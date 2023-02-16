package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.ClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;

@Service("MockClaimStatusUpdate")
public class MockClaimStatusUpdateService implements IClaimStatusUpdate {
    @Override
    public Object claimStatusUpdate(ClaimStatusUpdate claimStatusUpdateObj, String idAmId, String sdtRequestId) {
        return null;
    }
}
