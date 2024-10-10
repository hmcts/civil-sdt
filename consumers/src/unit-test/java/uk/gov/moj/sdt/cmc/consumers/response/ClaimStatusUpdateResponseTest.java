package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.INITIALLY_ACCEPTED;

class ClaimStatusUpdateResponseTest extends ResponseTestBase {

    @Test
    void testConvertToObject() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Initially Accepted"
            }""";

        ClaimStatusUpdateResponse claimStatusUpdateResponse =
            objectMapper.readValue(responseJson, ClaimStatusUpdateResponse.class);

        assertNotNull(claimStatusUpdateResponse, "ClaimStatusUpdateResponse should not be null");
        assertEquals(INITIALLY_ACCEPTED,
                     claimStatusUpdateResponse.getResponseStatus(),
                     "ClaimStatusUpdateResponse response status has unexpected value");
    }
}
