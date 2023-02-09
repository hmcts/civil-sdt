package uk.gov.moj.sdt.handlers.cmc.client.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.handlers.cmc.api.IBreathingSpace;

@Service
@Profile("Development")
public class MockBreathingSpaceService implements IBreathingSpace {

    public Object breathingSpace() {
        return "";
    }
}
