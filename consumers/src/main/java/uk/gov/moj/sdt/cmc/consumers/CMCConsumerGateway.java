package uk.gov.moj.sdt.cmc.consumers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.cmc.RequestType;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;
import uk.gov.moj.sdt.utils.cmc.xml.XmlElementValueReader;

@Component("CMCConsumerGateway")
public class CMCConsumerGateway implements IConsumerGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMCConsumerGateway.class);

    private IBreathingSpace breathingSpace;

    private IClaimStatusUpdateService claimStatusUpdate;

    private IClaimDefences claimDefences;

    private XmlToObjectConverter xmlToObject;

    private XmlElementValueReader xmlElementValueReader;

    @Autowired
    public CMCConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpace breathingSpace,
                              @Qualifier("ClaimStatusUpdateService") IClaimStatusUpdateService claimStatusUpdate,
                              @Qualifier("ClaimDefencesService") IClaimDefences claimDefences,
                              XmlToObjectConverter xmlToObject,
                              XmlElementValueReader xmlElementValueReader) {
        this.breathingSpace = breathingSpace;
        this.claimStatusUpdate = claimStatusUpdate;
        this.claimDefences = claimDefences;
        this.xmlToObject = xmlToObject;
        this.xmlElementValueReader = xmlElementValueReader;
    }

    @Override
    public void individualRequest(IIndividualRequest individualRequest,
                                  long connectionTimeOut,
                                  long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Invoke cmc target application service for individual request");
        String requestType = individualRequest.getRequestType();
        String sdtRequestReference = individualRequest.getSdtRequestReference();
        String idamId = ""; // Todo get it from SDTContext
        try {
            if(RequestType.BREATHING_SPACE.getType().equals(requestType)) {
                BreathingSpaceRequest request = xmlToObject.convertXmlToObject(
                    individualRequest.getRequestPayload(),
                    BreathingSpaceRequest.class);
                BreathingSpaceResponse response = breathingSpace.breathingSpace(request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            } else if (RequestType.CLAIM_STATUS_UPDATE.getType().equals(requestType)) {
                ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(
                    individualRequest.getRequestPayload(),
                    ClaimStatusUpdateRequest.class);
                ClaimStatusUpdateResponse response = claimStatusUpdate.claimStatusUpdate(idamId, sdtRequestReference, request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            }
        } catch (Exception e) {
            throw new CMCException(e.getMessage(), e);
        }
    }

    @Override
    public Object submitQuery(ISubmitQueryRequest submitQueryRequest,
                            long connectionTimeOut,
                            long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Submitting query to target application[{}], for customer[{}]",
                     submitQueryRequest.getTargetApplication().getTargetApplicationCode(),
                     submitQueryRequest.getBulkCustomer().getSdtCustomerId());

        // extract fromDate and toDate from submitQueryRequest
        String[] values = getClaimDefencesFields();
        String fromDate = values[0];
        String toDate = values[1];
        ClaimDefencesResponse response =  claimDefences.claimDefences(fromDate, toDate);

        List<Object> listObjects = Arrays.asList(response.getResults());

        return listObjects;
    }

    private String[] getClaimDefencesFields() {

        // get dates from request defence criteria.
        String xmlContent = SdtContext.getContext().getRawOutXml();
        String[] values = new String[2];

        values[0] = xmlElementValueReader.getElementValue(xmlContent, "fromDate");
        values[1] = xmlElementValueReader.getElementValue(xmlContent, "toDate");

        return values;
    }

}
