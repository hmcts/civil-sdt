package uk.gov.moj.sdt.cmc.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
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
    private IClaimStatusUpdate claimStatusUpdate;

    private XmlToObjectConverter xmlToObject;

    @Autowired
    public CMCConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpace breathingSpace,
                              @Qualifier("ClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdate,
                              XmlToObjectConverter xmlToObject) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdate = claimStatusUpdate;
        this.xmlToObject = xmlToObject;
    }

    @Override
    public void individualRequest(IIndividualRequest individualRequest,
                                  long connectionTimeOut,
                                  long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Invoke cmc target application service for individual request");
        try {
            if(RequestType.BREATHING_SPACE.getType().equals(individualRequest.getRequestType())) {
                BreathingSpaceRequest request = xmlToObject.convertXmlToObject(
                    individualRequest.getRequestPayload(),
                    BreathingSpaceRequest.class
                );
                BreathingSpaceResponse response = breathingSpace.breathingSpace(request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            } else if (RequestType.CLAIM_STATUS_UPDATE.getType().equals(individualRequest.getRequestType())) {
                String FORTesting = individualRequest.getRequestPayload().toString();
               // check the confluance page on the structure to retest will to truncate - cascade service requests table.

                ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(
                    individualRequest.getRequestPayload(),
                    ClaimStatusUpdateRequest.class);
                ClaimStatusUpdateResponse response = claimStatusUpdate.claimStatusUpdate(request);
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
