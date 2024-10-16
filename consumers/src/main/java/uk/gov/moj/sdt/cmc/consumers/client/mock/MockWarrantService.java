package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IWarrantService;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponseDetail;

@Service("MockWarrantService")
public class MockWarrantService implements IWarrantService {


    @Override
    public WarrantResponse warrantRequest(String authorization,
                                          String serviceAuthorization,
                                          String idamId,
                                          String sdtRequestId,
                                          WarrantRequest warrantRequest) {
        WarrantResponseDetail warrantResponseDetail = new WarrantResponseDetail();
        warrantResponseDetail.setWarrantNumber("123456");
        warrantResponseDetail.setFee(45678L);

        WarrantResponse response = new WarrantResponse();
        response.setResponseStatus(ResponseStatus.INITIALLY_ACCEPTED);
        response.setWarrantResponseDetail(warrantResponseDetail);

        return response;
    }
}
