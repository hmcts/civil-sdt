package uk.gov.moj.sdt.cmc.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpaceService;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementService;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.utils.cmc.RequestType;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;
import uk.gov.moj.sdt.utils.cmc.exception.CaseOffLineException;

import static uk.gov.moj.sdt.utils.cmc.exception.CMCExceptionMessages.CASE_OFF_LINE;

@Component("CMCConsumerGateway")
public class CMCConsumerGateway implements IConsumerGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMCConsumerGateway.class);

    private IBreathingSpaceService breathingSpace;

    private IJudgementService judgementService;

    private XmlConverter xmlToObject;

    @Autowired
    public CMCConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpaceService breathingSpace,
                              @Qualifier("JudgementRequestService") IJudgementService judgementService,
                              XmlConverter xmlToObject) {
        this.breathingSpace = breathingSpace;
        this.judgementService = judgementService;
        this.xmlToObject = xmlToObject;
    }

    @Override
    public void individualRequest(IIndividualRequest individualRequest,
                                  long connectionTimeOut,
                                  long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Invoke cmc target application service for individual request");
        String sdtRequestReference = individualRequest.getSdtRequestReference();
        String requestType = individualRequest.getRequestType();
        String idamId = ""; // Todo get it from SDTContext
        String requestPayload = individualRequest.getRequestPayload();
        try {
            if (RequestType.JUDGMENT.getType().equals(requestType)) {
                JudgementRequest judgementRequest = xmlToObject.convertXmlToObject(requestPayload,
                                                                                   JudgementRequest.class);

                JudgementResponse judgementResponse = judgementService.requestJudgment(idamId,
                                                                                       sdtRequestReference,
                                                                                       judgementRequest);
                individualRequest.setTargetApplicationResponse(xmlToObject.convertObjectToXml(judgementResponse));
            } else if (RequestType.BREATHING_SPACE.getType().equals(requestType)) {
                BreathingSpaceRequest request = xmlToObject.convertXmlToObject(requestPayload,
                                                                               BreathingSpaceRequest.class);

                BreathingSpaceResponse response = breathingSpace.breathingSpace(idamId, sdtRequestReference, request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message != null && message.contains(CASE_OFF_LINE)) {
                throw new CaseOffLineException(message, e);
            }
            throw new CMCException(message, e);
        }
    }

    @Override
    public void submitQuery(ISubmitQueryRequest submitQueryRequest,
                            long connectionTimeOut,
                            long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Submitting query to target application[{}], for customer[{}]",
                     submitQueryRequest.getTargetApplication().getTargetApplicationCode(),
                     submitQueryRequest.getBulkCustomer().getSdtCustomerId());
    }

}
