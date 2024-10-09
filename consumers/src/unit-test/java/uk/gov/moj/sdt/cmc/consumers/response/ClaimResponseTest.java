package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.ACCEPTED;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.INITIALLY_ACCEPTED;

public class ClaimResponseTest extends ResponseTestBase {

    @Test
    void testConvertToObjectAllFields() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Accepted",
              "claimResponseDetail" : {
                "claimNumber" : "0000-0000-0000-0001",
                "issueDate" : "2024-01-01",
                "serviceDate" : "2025-02-02",
                "fee" : 100
              }
            }""";

        ClaimResponse claimResponse = objectMapper.readValue(responseJson, ClaimResponse.class);
        assertNotNull(claimResponse, "ClaimResponse should not be null");
        assertEquals(ACCEPTED, claimResponse.getResponseStatus(), "ClaimResponse has unexpected response status");

        ClaimResponseDetail claimResponseDetail = claimResponse.getClaimResponseDetail();
        assertNotNull(claimResponseDetail, "ClaimResponseDetail should not be null");
        assertEquals("0000-0000-0000-0001",
                     claimResponseDetail.getClaimNumber(),
                     "ClaimResponseDetail has unexpected claim number");
        assertEquals(createDate(2024, 1, 1),
                     claimResponseDetail.getIssueDate(),
                     "ClaimResponseDetail has unexpected issue date");
        assertEquals(createDate(2025, 2, 2),
                     claimResponseDetail.getServiceDate(),
                     "ClaimResponseDetail has unexpected service date");
        assertEquals(100L, claimResponseDetail.getFee(), "ClaimResponseDetail has unexpected fee");
    }

    @Test
    void testConvertToObjectMandatoryFieldsOnly() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Initially Accepted",
              "claimResponseDetail" : {
                "claimNumber" : "0000-0000-0000-0001",
                "issueDate" : null,
                "serviceDate" : null,
                "fee" : null
              }
            }""";

        ClaimResponse claimResponse = objectMapper.readValue(responseJson, ClaimResponse.class);
        assertNotNull(claimResponse, "ClaimResponse should not be null");
        assertEquals(INITIALLY_ACCEPTED,
                     claimResponse.getResponseStatus(),
                     "ClaimResponse has unexpected response status");

        ClaimResponseDetail claimResponseDetail = claimResponse.getClaimResponseDetail();
        assertNotNull(claimResponseDetail, "ClaimResponseDetail should not be null");
        assertEquals("0000-0000-0000-0001",
                     claimResponseDetail.getClaimNumber(),
                     "ClaimResponseDetail has unexpected claim number");
        assertNull(claimResponseDetail.getIssueDate(), "ClaimResponseDetail issue date should be null");
        assertNull(claimResponseDetail.getServiceDate(), "ClaimResponseDetail service date should be null");
        assertNull(claimResponseDetail.getFee(), "ClaimResponseDetail fee should be null");
    }
}
