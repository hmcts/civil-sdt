package uk.gov.moj.sdt.cmc.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObject;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.model.breathingspace.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.xml.XmlElementValueReader;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.RequestType;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;

@Component("CmcConsumerGateway")
public class CmcConsumerGateway implements IConsumerGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmcConsumerGateway.class);

    private IBreathingSpace breathingSpace;
    private IClaimStatusUpdate claimStatusUpdate;

    private IClaimDefences claimDefences;

    @Autowired
    public CmcConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpace breathingSpace,
                              @Qualifier("ClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdate,
                              @Qualifier("ClaimDefencesService") IClaimDefences claimDefences) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdate = claimStatusUpdate;
        this.claimDefences = claimDefences;
    }

    @Override
    public void individualRequest(IIndividualRequest individualRequest,
                                  long connectionTimeOut,
                                  long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Invoke cmc target application service for individual request");
        try {
            if (RequestType.CLAIM_STATUS_UPDATE.getRequestType().equals(individualRequest.getRequestType())) {
                ClaimStatusUpdateRequest claimStatusUpdateRequest = null;
                claimStatusUpdate.claimStatusUpdate(claimStatusUpdateRequest, "", "");
            } else if (RequestType.BREATHING_SPACE.getRequestType().equals(individualRequest.getRequestType())) {
                BreathingSpaceRequest request = XmlToObject.convertXmlToObject(individualRequest.getRequestPayload(), BreathingSpaceRequest.class);
                breathingSpace.breathingSpace(request);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    @Override
    public void submitQuery(ISubmitQueryRequest submitQueryRequest,
                            long connectionTimeOut,
                            long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Submitting query to target application[{}], for customer[{}]",
                     submitQueryRequest.getTargetApplication().getTargetApplicationCode(),
                     submitQueryRequest.getBulkCustomer().getSdtCustomerId());

        // extract fromDate and toDate from submitQueryRequest
        String[] values = getClaimDefencesFields(submitQueryRequest);
        String fromDate = values[0];
        String toDate = values[1];
        claimDefences.claimDefences(fromDate, toDate);
    }

    private String[] getClaimDefencesFields(ISubmitQueryRequest submitQueryRequest) {
        String xmlContent = null;
        try {
            xmlContent = XmlToObject.convertObjectToXml(submitQueryRequest);
        } catch (Exception e) {
            throw new RuntimeException("Unable to extract xml content from submitQueryRequest");
        }

        String[] values = new String[2];
        XmlElementValueReader xmlReader = new XmlElementValueReader();
        String fromDate = xmlReader.getElementValue(xmlContent, "fromDate");
        values[0] = fromDate;
        String toDate = xmlReader.getElementValue(xmlContent, "toDate");
        values[1] = toDate;
        return values;
    }

}
