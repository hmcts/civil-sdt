package uk.gov.moj.sdt.consumers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.moj.sdt.cmc.consumers.converter.XmlToObjectConverter;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailType;
import uk.gov.moj.sdt.cmc.consumers.model.McolDefenceDetailTypes;
import uk.gov.moj.sdt.cmc.consumers.util.ResponsesSummaryUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the consumer gateway.
 *
 * @author Mark Dathorne
 */
@ExtendWith(MockitoExtension.class)
class McolDefenceDetailTypeUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(McolDefenceDetailTypeUtilTest.class);

    private ClaimDefencesResultsUtil claimDefencesResultsUtil;
    private McolDefenceDetailTypeUtil mcolDefenceDetailTypeUtil;
    private ResponsesSummaryUtil responsesSummaryUtil;
    private XmlToObjectConverter xmlToObjectConverter;
    /**
     * Method to do any pre-test set-up.
     */
    @BeforeEach
    public void setUpLocalTests() {
        xmlToObjectConverter = new XmlToObjectConverter();
        responsesSummaryUtil = new ResponsesSummaryUtil(xmlToObjectConverter);
        claimDefencesResultsUtil = new ClaimDefencesResultsUtil();
        mcolDefenceDetailTypeUtil = new McolDefenceDetailTypeUtil(claimDefencesResultsUtil, responsesSummaryUtil);
    }

    @Test
    void shouldConvertMcolDefenceDetailTypeJAXBToXml() {

        McolDefenceDetailType detailType = mcolDefenceDetailTypeUtil.convertToMcolDefenceDetailType(
                claimDefencesResultsUtil.createClaimDefencesResult()
        );

        JAXBElement<McolDefenceDetailType> jaxbDetailType = mcolDefenceDetailTypeUtil.createMcolDefenceDetail(detailType);
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(McolDefenceDetailType.class);
            Marshaller marshaller =  jaxbContext.createMarshaller();
            marshaller.marshal(jaxbDetailType, stringWriter);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        String content = stringWriter.toString();
        LOGGER.info("content: {}", content);
        assertNotNull(content);
        assertTrue(content.contains(":mcolDefenceDetail"));
        assertTrue(content.contains("<claimNumber>"));
        assertTrue(content.contains("<defence>"));
        assertTrue(content.contains("<defendantResponse"));
    }

    @Test
    void shouldConvertMcolDefenceDetailTypeListJAXBToXml() {
        McolDefenceDetailTypes detailTypes = new McolDefenceDetailTypes();
        detailTypes.setMcolDefenceDetailTypeList(mcolDefenceDetailTypeUtil.createMcolDefencesDetailTypeList());

        JAXBElement<McolDefenceDetailTypes> jaxbDetailType = mcolDefenceDetailTypeUtil.createMcolDefenceDetailList(detailTypes);
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(McolDefenceDetailTypes.class);
            Marshaller marshaller =  jaxbContext.createMarshaller();
            marshaller.marshal(jaxbDetailType, stringWriter);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        String content = stringWriter.toString();
        LOGGER.info("content: {}", content);
        assertNotNull(content);
        assertTrue(content.contains("mcolDefenceDetail"));
        assertTrue(content.contains("<claimNumber>"));
        assertTrue(content.contains("<defence>"));
        assertTrue(content.contains("<defendantResponse"));
    }

}
