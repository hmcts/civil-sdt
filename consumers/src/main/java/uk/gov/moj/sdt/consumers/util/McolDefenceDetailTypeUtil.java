package uk.gov.moj.sdt.consumers.util;

import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.cmc.consumers.model.DefendantResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailType;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailTypes;
import uk.gov.moj.sdt.cmc.consumers.model.ResponseType;
import uk.gov.moj.sdt.cmc.consumers.model.claimdefences.ClaimDefencesResult;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

@Component
public class McolDefenceDetailTypeUtil {
    private ResponsesSummaryUtil responsesSummaryUtil;
    private ClaimDefencesResultsUtil claimDefencesResultsUtil;

    public McolDefenceDetailTypeUtil(ClaimDefencesResultsUtil claimDefencesResultsUtil,
                                     ResponsesSummaryUtil responsesSummaryUtil) {
        this.claimDefencesResultsUtil = claimDefencesResultsUtil;
        this.responsesSummaryUtil = responsesSummaryUtil;
    }

    public List<McolDefenceDetailType> createMcolDefencesDetailTypeList() {
        List<McolDefenceDetailType> detailTypes = new ArrayList<>();
        List<ClaimDefencesResult> results = claimDefencesResultsUtil.createClaimDefencesList();
        results.forEach(cmcResult ->
            detailTypes.add(convertToMcolDefenceDetailType(cmcResult))
        );

        return detailTypes;
    }

    public McolDefenceDetailType convertToMcolDefenceDetailType(ClaimDefencesResult cmcResult) {
        McolDefenceDetailType detailType = new McolDefenceDetailType();
        DefendantResponseType defendantResponseType = new DefendantResponseType();
        defendantResponseType.setDefendantId(cmcResult.getRespondentId());
        defendantResponseType.setResponseType(ResponseType.fromValue(cmcResult.getResponseType()));
        defendantResponseType.setDefence(cmcResult.getDefence());
        defendantResponseType.setRaisedOnMcol(false);
        defendantResponseType.setFiledDate(
                responsesSummaryUtil.convertLocalDateToCalendar(cmcResult.getDefendantResponseFiledDate()));
        defendantResponseType.setEventCreatedDateOnMcol(
                responsesSummaryUtil.convertLocalDateTimeToCalendar(cmcResult.getDefendantResponseCreatedDate()));

        detailType.setClaimNumber(cmcResult.getCaseManRef());
        detailType.setDefendantResponse(defendantResponseType);

        return detailType;
    }

    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", name = "mcolDefenceDetail")
    public JAXBElement<McolDefenceDetailType> createMcolDefenceDetail(McolDefenceDetailType value) {
        final QName qnameMcolDefenceDetail = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema",
                "mcolDefenceDetail");
        return new JAXBElement<>(qnameMcolDefenceDetail, McolDefenceDetailType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema", name = "results")
    public JAXBElement<McolDefenceDetailTypes> createMcolDefenceDetailList(McolDefenceDetailTypes value) {
        final QName qnameMcolDefenceDetail = new QName("http://ws.sdt.moj.gov.uk/2013/mcol/QuerySchema",
                "results");
        return new JAXBElement<>(qnameMcolDefenceDetail, McolDefenceDetailTypes.class, null, value);
    }

}
