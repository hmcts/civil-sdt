package uk.gov.moj.sdt.cmc.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgement;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
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

@Component("CMCConsumerGateway")
public class CMCConsumerGateway implements IConsumerGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMCConsumerGateway.class);

    private IBreathingSpace breathingSpace;

    private IJudgement judgementService;

    private XmlToObjectConverter xmlToObject;

    @Autowired
    public CMCConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpace breathingSpace,
                              @Qualifier("JudgementRequestService") IJudgement judgementService,
                              XmlToObjectConverter xmlToObject) {
        this.breathingSpace = breathingSpace;
        this.judgementService = judgementService;
        this.xmlToObject = xmlToObject;
    }

    @Override
    public void individualRequest(IIndividualRequest individualRequest,
                                  long connectionTimeOut,
                                  long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Invoke cmc target application service for individual request");
        try {
            if (RequestType.JUDGMENT.getType().equals(individualRequest.getRequestType())) {
                JudgementRequest judgementRequest = xmlToObject.convertXmlToObject(individualRequest.getRequestPayload(),
                                                                                   JudgementRequest.class);
                JudgementResponse judgementResponse = judgementService.requestJudgment("",
                                                                                      individualRequest.getSdtRequestReference(),
                                                                                       judgementRequest);
            } else if (RequestType.BREATHING_SPACE.getType().equals(individualRequest.getRequestType())) {
                BreathingSpaceRequest request = xmlToObject.convertXmlToObject(individualRequest.getRequestPayload(),
                                                                               BreathingSpaceRequest.class);
                BreathingSpaceResponse response = breathingSpace.breathingSpace(request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            }
        } catch (Exception e) {
            throw new CMCException(e.getMessage(), e);
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
