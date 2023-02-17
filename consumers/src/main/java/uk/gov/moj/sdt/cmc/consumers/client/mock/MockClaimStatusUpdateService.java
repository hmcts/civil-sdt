package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;

@Service("MockClaimStatusUpdateService")
public class MockClaimStatusUpdateService implements IClaimStatusUpdate {
    @Override
    public Object claimStatusUpdate(ClaimStatusUpdateRequest claimStatusUpdateRequestObj, String idAmId, String sdtRequestId) {
        return null;
    }
}
