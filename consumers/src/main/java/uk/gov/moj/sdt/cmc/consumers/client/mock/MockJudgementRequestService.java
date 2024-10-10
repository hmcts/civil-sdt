package uk.gov.moj.sdt.cmc.consumers.client.mock;

import java.util.Calendar;

import org.springframework.stereotype.Service;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementService;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponseDetail;

@Service("MockJudgementRequestService")
public class MockJudgementRequestService implements IJudgementService {

    @Override
    public JudgementResponse requestJudgment(String idamId,
                                             String sdtRequestId,
                                             JudgementRequest judgementRequest) {
        JudgementResponseDetail judgementResponseDetail = new JudgementResponseDetail();
        judgementResponseDetail.setJudgmentEnteredDate(Calendar.getInstance().getTime());
        judgementResponseDetail.setFirstPaymentDate(Calendar.getInstance().getTime());

        JudgementResponse judgementResponse = new JudgementResponse();
        judgementResponse.setResponseStatus(ResponseStatus.ACCEPTED);
        judgementResponse.setJudgementResponseDetail(judgementResponseDetail);

        return judgementResponse;
    }
}
