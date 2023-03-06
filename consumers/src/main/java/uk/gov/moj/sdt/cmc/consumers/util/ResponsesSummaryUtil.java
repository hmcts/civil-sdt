package uk.gov.moj.sdt.cmc.consumers.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.DefendantResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailType;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.submitqueryresponseschema.SubmitQueryResponseType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ResponsesSummaryUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponsesSummaryUtil.class);

    private XmlToObjectConverter xmlToObjectConverter;

    public ResponsesSummaryUtil(XmlToObjectConverter xmlToObjectConverter) {
        this. xmlToObjectConverter =  xmlToObjectConverter;
    }

    public String getSummaryResults(Object mcolSubmitResponseTypeObject,
                                    Object cmcSubmitResponseTypeObject) {
        // process mcolResults
        String mcolResultsXml = "";
        if (null != mcolSubmitResponseTypeObject) {
            SubmitQueryResponseType mcolSubmitResponseType = (SubmitQueryResponseType) mcolSubmitResponseTypeObject;
            List<Object> mcolResultObjects = mcolSubmitResponseType.getTargetAppDetail().getAny();
            mcolResultsXml = getMcolResultsXml(mcolResultObjects);
            LOGGER.debug("mcol Results XML: {}", mcolResultsXml);
        }

        String cmcResultsXml = "";
        if (null != cmcSubmitResponseTypeObject) {
            List<Object> cmcResultObjects = (List<Object>) cmcSubmitResponseTypeObject;
            cmcResultsXml = convertToMcolResultsXml(cmcResultObjects);
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
     * @param mcolResultObjects
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
     * @param cmcResultObjects
     * @return String xml
    */
    public String convertToMcolResultsXml(List<Object> cmcResultObjects) {
        List<Object> convertedObjects = convertToMcolResultObjects(cmcResultObjects);
        String claimDefencesObjectXML = "";
        try {
            claimDefencesObjectXML = xmlToObjectConverter.convertObjectToXml(convertedObjects);
            claimDefencesObjectXML = convertToMcolResultsXml(claimDefencesObjectXML);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return claimDefencesObjectXML;
     }

    public String convertToMcolResultsXml(String inXml) {
        inXml = inXml.replace("<ArrayList>", "");
        inXml = inXml.replace("</ArrayList>", "");
        inXml = inXml.replace("<item>", "<ns3:mcolDefenceDetail>");
        inXml = inXml.replace("</item>", "</ns3:mcolDefenceDetail>");
        inXml = inXml.replace("<results>", "<qresp:results>");
        inXml = inXml.replace("</results>", "</qresp:results>");

        return inXml;
    }

    public List<Object> convertToMcolResultObjects(List<Object> cmcResultObjects) {
        List<Object> convertedResults = new ArrayList<>();

        for (Object object :  cmcResultObjects) {
            ClaimDefencesResult claimDefencesResult = (ClaimDefencesResult) object;
            McolDefenceDetailType defenceDetailType = convertToMcolDefenceDetailType(claimDefencesResult);
            convertedResults.add(defenceDetailType);
        }
        return convertedResults;
    }

    public McolDefenceDetailType convertToMcolDefenceDetailType(ClaimDefencesResult claimDefencesResult) {

        DefendantResponseType defendantResponseType = new DefendantResponseType();

        defendantResponseType.setFiledDate(
                convertLocalDateToCalendar(claimDefencesResult.getDefendantResponseFiledDate()));
        defendantResponseType.setEventCreatedDateOnMcol(
                convertLocalDateTimeToCalendar(claimDefencesResult.getDefendantResponseCreatedDate()));
        defendantResponseType.setRaisedOnMcol(false);
        defendantResponseType.setResponseType(ResponseType.DE);
        defendantResponseType.setDefence(claimDefencesResult.getDefence());
        defendantResponseType.setDefendantId(claimDefencesResult.getRespondentId());

        McolDefenceDetailType defenceDetailType = new McolDefenceDetailType();
        defenceDetailType.setClaimNumber(claimDefencesResult.getCaseManRef());
        defenceDetailType.setDefendantResponse(defendantResponseType);

        return defenceDetailType;
    }


    public Calendar convertLocalDateTimeToCalendar(String strDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(strDateTime, formatter);

        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public Calendar convertLocalDateToCalendar(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(strDate, formatter);

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
