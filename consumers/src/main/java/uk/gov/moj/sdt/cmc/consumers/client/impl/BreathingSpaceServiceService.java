package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpaceService;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;


@Service("BreathingSpaceService")
public class BreathingSpaceServiceService implements IBreathingSpaceService {

    private CMCApi cmcApi;

    @Autowired
    public BreathingSpaceServiceService(CMCApi cmcApi) {
        this.cmcApi = cmcApi;
    }

    @Override
    public BreathingSpaceResponse breathingSpace(String idamId,
                                                 String sdtRequestId,
                                                 BreathingSpaceRequest breathingSpaceRequest) {
        return cmcApi.breathingSpace(idamId, sdtRequestId, breathingSpaceRequest);
    }
}
