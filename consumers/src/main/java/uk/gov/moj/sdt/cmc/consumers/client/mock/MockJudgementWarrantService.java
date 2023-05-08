package uk.gov.moj.sdt.cmc.consumers.client.mock;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementWarrantService;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;

@Service("MockJudgementWarrantService")
public class MockJudgementWarrantService implements IJudgementWarrantService {

    @Override
    public JudgementWarrantResponse judgementWarrantRequest(String authorization,
                                                            String serviceAuthorization,
                                                            String idamId,
                                                            String sdtRequestId,
                                                            JudgementWarrantRequest judgementWarrantRequest) {
        JudgementWarrantResponse response = new JudgementWarrantResponse();
        response.setWarrantNumber("123456");
        response.setFee(45678L);
        return response;
    }
}
