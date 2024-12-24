package uk.gov.moj.sdt.cmc.consumers;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.api.IBreathingSpaceService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimDefencesService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimRequestService;
import uk.gov.moj.sdt.cmc.consumers.api.IClaimStatusUpdateService;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementService;
import uk.gov.moj.sdt.cmc.consumers.api.IJudgementWarrantService;
import uk.gov.moj.sdt.cmc.consumers.api.IWarrantService;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlConverter;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResponse;
import uk.gov.moj.sdt.cmc.consumers.request.BreathingSpaceRequest;
import uk.gov.moj.sdt.cmc.consumers.request.ClaimStatusUpdateRequest;
import uk.gov.moj.sdt.cmc.consumers.request.JudgementWarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.WarrantRequest;
import uk.gov.moj.sdt.cmc.consumers.request.claim.ClaimRequest;
import uk.gov.moj.sdt.cmc.consumers.request.judgement.JudgementRequest;
import uk.gov.moj.sdt.cmc.consumers.response.BreathingSpaceResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimResponse;
import uk.gov.moj.sdt.cmc.consumers.response.ClaimStatusUpdateResponse;
import uk.gov.moj.sdt.cmc.consumers.response.JudgementWarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.WarrantResponse;
import uk.gov.moj.sdt.cmc.consumers.response.judgement.JudgementResponse;
import uk.gov.moj.sdt.consumers.api.IConsumerGateway;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.ErrorLog;
import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.idam.IdamRepository;
import uk.gov.moj.sdt.idam.S2SRepository;
import uk.gov.moj.sdt.response.SubmitQueryResponse;
import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.cmc.RequestType;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;
import uk.gov.moj.sdt.utils.cmc.exception.CaseOffLineException;
import uk.gov.moj.sdt.utils.cmc.xml.XmlElementValueReader;
import uk.gov.moj.sdt.ws._2013.sdt.baseschema.StatusCodeType;

import java.nio.charset.StandardCharsets;

import static uk.gov.moj.sdt.utils.cmc.exception.CMCExceptionMessages.CASE_OFF_LINE;

@Component("CMCConsumerGateway")
@SuppressWarnings("java:S6539")
public class CMCConsumerGateway implements IConsumerGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMCConsumerGateway.class);
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    private static final String MAX_RESULTS = "78";
    private static final int NO_DATA = 77;
    private static final int NOT_FOUND = 404;

    private IBreathingSpaceService breathingSpace;
    private IClaimStatusUpdateService claimStatusUpdate;

    private IClaimRequestService claimRequestService;

    private IClaimDefencesService claimDefences;

    private IJudgementService judgementService;

    private IWarrantService warrantService;

    private IJudgementWarrantService judgementWarrantService;

    private XmlConverter xmlToObject;

    private XmlElementValueReader xmlElementValueReader;

    private IdamRepository idamRepository;
    private S2SRepository s2SRepository;

    @Autowired
    public CMCConsumerGateway(@Qualifier("BreathingSpaceService") IBreathingSpaceService breathingSpace,
                              @Qualifier("JudgementRequestService") IJudgementService judgementService,
                              @Qualifier("ClaimStatusUpdateService") IClaimStatusUpdateService claimStatusUpdate,
                              @Qualifier("ClaimRequestService") IClaimRequestService claimRequestService,
                              @Qualifier("ClaimDefencesService") IClaimDefencesService claimDefences,
                              @Qualifier("WarrantService") IWarrantService warrantService,
                              @Qualifier("JudgementWarrantService") IJudgementWarrantService judgementWarrantService,
                              XmlConverter xmlToObject,
                              XmlElementValueReader xmlElementValueReader,
                              IdamRepository idamRepository,
                              S2SRepository s2SRepository) {
        this.breathingSpace = breathingSpace;
        this.judgementService = judgementService;
        this.claimStatusUpdate = claimStatusUpdate;
        this.claimDefences = claimDefences;
        this.warrantService = warrantService;
        this.judgementWarrantService = judgementWarrantService;
        this.claimRequestService = claimRequestService;
        this.xmlToObject = xmlToObject;
        this.xmlElementValueReader = xmlElementValueReader;
        this.idamRepository = idamRepository;
        this.s2SRepository = s2SRepository;
    }

    @Override
    public void individualRequest(IIndividualRequest individualRequest,
                                  long connectionTimeOut,
                                  long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Invoke cmc target application service for individual request");
        String sdtRequestReference = individualRequest.getSdtRequestReference();
        String requestType = individualRequest.getRequestType();
        String idamId = SdtContext.getContext().getCustomerIdamId();
        String sdtSystemUserAuthToken = idamRepository.getSdtSystemUserAccessToken();
        String serviceAuthToken = s2SRepository.getS2SToken();
        String requestPayload = null == individualRequest.getRequestPayload() ? "" :
            new String(individualRequest.getRequestPayload(), StandardCharsets.UTF_8);
        try {
            if (RequestType.JUDGMENT.getType().equals(requestType)) {
                JudgementRequest judgementRequest = xmlToObject.convertXmlToObject(requestPayload,
                                                                                   JudgementRequest.class);

                JudgementResponse judgementResponse = judgementService.requestJudgment(idamId,
                                                                                       sdtRequestReference,
                                                                                       judgementRequest);
                individualRequest.setTargetApplicationResponse(xmlToObject.convertObjectToXml(judgementResponse).getBytes(StandardCharsets.UTF_8));
            } else if (RequestType.BREATHING_SPACE.getType().equals(requestType)) {
                BreathingSpaceRequest request = xmlToObject.convertXmlToObject(requestPayload, BreathingSpaceRequest.class);
                BreathingSpaceResponse response = breathingSpace.breathingSpace(idamId, sdtRequestReference, request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            } else if (RequestType.CLAIM_STATUS_UPDATE.getType().equals(requestType)) {
                ClaimStatusUpdateRequest request = xmlToObject.convertXmlToObject(requestPayload, ClaimStatusUpdateRequest.class);
                ClaimStatusUpdateResponse response = claimStatusUpdate.claimStatusUpdate(idamId, sdtRequestReference, request);
                individualRequest.setRequestStatus(response.getProcessingStatus().name());
            } else if (RequestType.CLAIM.getType().equals(requestType)) {
                ClaimRequest request = xmlToObject.convertXmlToObject(requestPayload, ClaimRequest.class);
                ClaimResponse response = claimRequestService.claimRequest(idamId, sdtRequestReference, request);
                individualRequest.setTargetApplicationResponse(xmlToObject.convertObjectToXml(response).getBytes(StandardCharsets.UTF_8));
            } else if (RequestType.WARRANT.getType().equals(requestType)) {
                WarrantRequest request = xmlToObject.convertXmlToObject(requestPayload, WarrantRequest.class);
                WarrantResponse response = warrantService.warrantRequest(sdtSystemUserAuthToken, serviceAuthToken,
                                                                         idamId, sdtRequestReference, request);
                individualRequest.setTargetApplicationResponse(xmlToObject.convertObjectToXml(response).getBytes(StandardCharsets.UTF_8));
            } else if (RequestType.JUDGMENT_WARRANT.getType().equals(requestType)) {
                JudgementWarrantRequest request = xmlToObject.convertXmlToObject(requestPayload, JudgementWarrantRequest.class);
                JudgementWarrantResponse response = judgementWarrantService.judgementWarrantRequest(sdtSystemUserAuthToken,
                                                                                                    serviceAuthToken,
                                                                                                    idamId,
                                                                                                    sdtRequestReference,
                                                                                                    request);
                individualRequest.setTargetApplicationResponse(xmlToObject.convertObjectToXml(response).getBytes(StandardCharsets.UTF_8));
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
    public SubmitQueryResponse submitQuery(ISubmitQueryRequest submitQueryRequest,
                            long connectionTimeOut,
                            long receiveTimeOut) throws OutageException, TimeoutException {
        LOGGER.debug("Submitting query to target application[{}], for customer[{}]",
                     submitQueryRequest.getTargetApplication().getTargetApplicationCode(),
                     submitQueryRequest.getBulkCustomer().getSdtCustomerId());

        String xmlContent = SdtContext.getContext().getRawOutXml();
        String fromDate = xmlElementValueReader.getElementValue(xmlContent, FROM_DATE);
        String toDate = xmlElementValueReader.getElementValue(xmlContent, TO_DATE);
        SubmitQueryResponse submitQueryResponse = new SubmitQueryResponse();
        try {
            ClaimDefencesResponse response = claimDefences.claimDefences(idamRepository.getSdtSystemUserAccessToken(),
                                                                         s2SRepository.getS2SToken(),
                                                                         SdtContext.getContext().getCustomerIdamId(),
                                                                         fromDate,
                                                                         toDate);
            updateResponseStatus(submitQueryRequest, submitQueryResponse, response);
        } catch (FeignException e) {
            boolean noResults = NO_DATA == e.status() || NOT_FOUND == e.status();
            if (!noResults) {
                final IErrorLog errorLog = new ErrorLog(String.valueOf(e.status()), e.getMessage());
                submitQueryRequest.reject(errorLog);
            }
        }

        return submitQueryResponse;
    }

    private void updateResponseStatus(ISubmitQueryRequest submitQueryRequest,
                                      SubmitQueryResponse submitQueryResponse,
                                      ClaimDefencesResponse response) {
        submitQueryResponse.setClaimDefencesResults(response.getResults());
        if (!maximumResultCountReached(submitQueryRequest)) {
            submitQueryResponse.setClaimDefencesResultsCount(response.getResultCount());
            submitQueryRequest.setErrorLog(null);
            submitQueryRequest.setStatus(StatusCodeType.OK.value());
        }
    }

    private boolean maximumResultCountReached(ISubmitQueryRequest submitQueryRequest) {
        return submitQueryRequest.hasError()
            && submitQueryRequest.getErrorLog().getErrorCode().equalsIgnoreCase(MAX_RESULTS);
    }

}
