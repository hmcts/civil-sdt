package uk.gov.moj.sdt.cmc.consumers.client.mock;

import java.util.Calendar;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementWarrantService;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponseDetail;
import uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus;
import uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus;

@Service("MockJudgementWarrantService")
public class MockJudgementWarrantService implements IJudgementWarrantService {

    @Override
    public JudgementWarrantResponse judgementWarrantRequest(String authorization,
                                                            String serviceAuthorization,
                                                            String idamId,
                                                            String sdtRequestId,
                                                            JudgementWarrantRequest judgementWarrantRequest) {
        JudgementWarrantResponseDetail judgementWarrantResponseDetail = new JudgementWarrantResponseDetail();
        judgementWarrantResponseDetail.setWarrantNumber("123456");
        judgementWarrantResponseDetail.setFee(45678L);
        judgementWarrantResponseDetail
            .setJudgmentWarrantStatus(JudgmentWarrantStatus.JUDGMENT_ACCEPTED_WARRANT_ACCEPTED_BY_CCBC);
        judgementWarrantResponseDetail.setJudgmentEnteredDate(Calendar.getInstance().getTime());
        judgementWarrantResponseDetail.setEnforcingCourtCode("123");
        judgementWarrantResponseDetail.setEnforcingCourtName("Court Code");
        judgementWarrantResponseDetail.setFirstPaymentDate(Calendar.getInstance().getTime());

        JudgementWarrantResponse response = new JudgementWarrantResponse();
        response.setResponseStatus(ResponseStatus.ACCEPTED);
        response.setJudgementWarrantResponseDetail(judgementWarrantResponseDetail);

        return response;
    }
}
