package uk.gov.moj.sdt.cmc.consumers.api;

import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;

public interface IJudgementWarrantService {

    JudgementWarrantResponse judgementWarrantRequest(String authorization,
                                                     String serviceAuthorization,
                                                     String idamId,
                                                     String sdtRequestId,
                                                     JudgementWarrantRequest judgementWarrantRequest);
}
