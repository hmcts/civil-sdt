package uk.gov.moj.sdt.cmc.consumers;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpace;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefences;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdate;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCException;
import uk.gov.moj.sdt.cmc.consumers.model.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.xml.XmlElementValueReader;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.RequestType;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;

@Component("CMCConsumerGateway")
public class CMCConsumerGateway implements IConsumerGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMCConsumerGateway.class);

    private IBreathingSpace breathingSpace;
    private IClaimStatusUpdate claimStatusUpdate;
    private IClaimDefences claimDefences;

    private XmlToObjectConverter xmlToObject;

    private XmlElementValueReader xmlElementValueReader;

    @Autowired
    public CMCConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpace breathingSpace,
                              @Qualifier("ClaimStatusUpdateService") IClaimStatusUpdate claimStatusUpdate,
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
        try {
            if (RequestType.CLAIM_STATUS_UPDATE.getRequestType().equals(individualRequest.getRequestType())) {
                ClaimStatusUpdateRequest claimStatusUpdateRequest = null;
                claimStatusUpdate.claimStatusUpdate(claimStatusUpdateRequest, "", "");
            } else if (RequestType.BREATHING_SPACE.getRequestType().equals(individualRequest.getRequestType())) {
                BreathingSpaceRequest request = xmlToObject.convertXmlToObject(individualRequest.getRequestPayload(),
                        BreathingSpaceRequest.class);
                BreathingSpaceResponse response = breathingSpace.breathingSpace(request);
            }
            individualRequest.setRequestStatus(response.getProcessingStatus().name());
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
        String[] values = getClaimDefencesFields(submitQueryRequest);
        String fromDate = values[0];
        String toDate = values[1];
        ClaimDefencesResponse response =  claimDefences.claimDefences(fromDate, toDate);

        List<Object> listObjects = new ArrayList<>();
        for (Object object : response.getResults()) {
            listObjects.add(object);
        }

        return listObjects;
    }

    private String[] getClaimDefencesFields(ISubmitQueryRequest submitQueryRequest) {
// TODO: fix xml data extraction
        //        String xmlContent = null;
//        try {
//            xmlContent = xmlToObject.convertObjectToXml(submitQueryRequest);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to extract xml content from submitQueryRequest");
//        }

        String[] values = new String[2];
        XmlElementValueReader xmlReader = new XmlElementValueReader();
//        String fromDate = xmlReader.getElementValue(xmlContent, "fromDate");
        String fromDate = "2020-10-12";
        values[0] = fromDate;
//        String toDate = xmlReader.getElementValue(xmlContent, "toDate");
        String toDate = "2020-10-14";
        values[1] = toDate;
        return values;
    }

}
