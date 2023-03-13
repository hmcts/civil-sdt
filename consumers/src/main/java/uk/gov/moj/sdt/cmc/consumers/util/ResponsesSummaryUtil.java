package uk.gov.moj.sdt.cmc.consumers.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.SubmitQueryResponse;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ResponsesSummaryUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponsesSummaryUtil.class);

    private XmlToObjectConverter xmlToObjectConverter;

    public ResponsesSummaryUtil(XmlToObjectConverter xmlToObjectConverter) {
        this. xmlToObjectConverter =  xmlToObjectConverter;
    }

    public String getSummaryResults(SubmitQueryResponse mcolSubmitQueryResponse,
                                    List<ClaimDefencesResult> cmcResults) {
        // process mcolResults
        String mcolResultsXml = "";

        if (null != mcolSubmitQueryResponse && null != mcolSubmitQueryResponse.getResponseType()
        && null != mcolSubmitQueryResponse.getResponseType().getTargetAppDetail()
        && null != mcolSubmitQueryResponse.getResponseType().getTargetAppDetail().getAny()) {
            List<Object> mcolResultObjects = mcolSubmitQueryResponse.getResponseType().getTargetAppDetail().getAny();
            mcolResultsXml = getMcolResultsXml(mcolResultObjects);
            LOGGER.debug("mcol Results XML: {}", mcolResultsXml);
        }

        String cmcResultsXml = "";
        if (null != cmcResults) {
            cmcResultsXml = convertToMcolResultsXml(cmcResults);
            LOGGER.debug("cmc Results XML: {}", cmcResultsXml);
        }

        // Join both results XML
        StringBuilder sbJoin = new StringBuilder().append("<results>")
                .append(mcolResultsXml)
                .append(cmcResultsXml).append("</results>");

        String summaryResultsXml = convertToMcolResultsXml(sbJoin.toString());
        LOGGER.debug("summary XML: {}", summaryResultsXml);

        return summaryResultsXml;
    }

    /**
     * get mcol Results XML from List of mcolResultObjects
     *
     * @param mcolResultObjects mcol Result list of objects
     * @return String xml
     */
    public String getMcolResultsXml(List<Object> mcolResultObjects) {
        String mcolClaimDefencesObjectXML = "";
        try {
            mcolClaimDefencesObjectXML = xmlToObjectConverter.convertObjectToXml(mcolResultObjects);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error getting Mcol Results XML", e);
        }
        return mcolClaimDefencesObjectXML;
    }

    /**
     * convert to Mcol Results XML from List of cmcResultObjects
     *
     * @param cmcResults List
     * @return String xml
    */
    public String convertToMcolResultsXml(List<ClaimDefencesResult> cmcResults) {
        List<String> claimNumbers = new ArrayList<>();
        StringBuilder claimDefencesObjectXML = new StringBuilder();
        for (ClaimDefencesResult result : cmcResults) {
            int defendantId = 1;
            if (claimNumbers.contains(result.getCaseManRef())) {
                defendantId = defendantId + 1;
            } else {
                claimNumbers.add(result.getCaseManRef());
            }
            claimDefencesObjectXML.append(convertToMcolResultsXml(result, defendantId));
        }

        return claimDefencesObjectXML.toString();
    }

    public String convertToMcolResultsXml(ClaimDefencesResult result, Integer defendantId) {
        StringBuilder sbXML = new StringBuilder();
        DateTimeFormatter zdtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH);

        sbXML.append("<mcolDefenceDetail>")
                .append("<claimNumber>").append(result.getCaseManRef()).append("</claimNumber>")
                .append("<defendantResponse defendantId=\"").append(defendantId).append("\" xmlns:ns2=\"http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema\">")
                .append("<filedDate>").append(result.getDefendantResponseFiledDate()).append("</filedDate>")
                .append("<eventCreatedDateOnMcol>").append(
                        result.getDefendantResponseCreatedDate().format(zdtFormatter)
                ).append("</eventCreatedDateOnMcol>")
                .append("<raisedOnMcol>").append(false).append("</raisedOnMcol>")
                .append("<responseType>").append(result.getResponseType()).append("</responseType>")
                .append("<defence>").append(result.getDefence()).append("</defence>")
                .append("</defendantResponse>")
                .append("</mcolDefenceDetail>");

        return sbXML.toString();
    }

    public String convertToMcolResultsXml(String inXml) {
        inXml = inXml.replace("<mcolDefenceDetail>", "<ns3:mcolDefenceDetail  xmlns=\"http://ws.sdt.moj.gov.uk/2013/sdt/targetApp/SubmitQueryRequestSchema\" xmlns:ns3=\"http://ws.sdt.moj.gov.uk/2013/sdt/SubmitQueryResponseSchema\">");
        inXml = inXml.replace("</mcolDefenceDetail>", "</ns3:mcolDefenceDetail>");
        inXml = inXml.replace("<results>", "<qresp:results>");
        inXml = inXml.replace("</results>", "</qresp:results>");

        return inXml;
    }

}
