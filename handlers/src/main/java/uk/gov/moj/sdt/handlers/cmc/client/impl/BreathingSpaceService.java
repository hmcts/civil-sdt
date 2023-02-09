package uk.gov.moj.sdt.handlers.cmc.client.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.handlers.cmc.api.IBreathingSpace;
import uk.gov.moj.sdt.handlers.cmc.client.CivilApi;

@Service
@Profile("!Development")
public class BreathingSpaceService implements IBreathingSpace {

    private CivilApi civilApi;

    public Object breathingSpace() {
        return civilApi.breathingSpace("", "", null);
    }
}
