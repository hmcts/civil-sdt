package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClaimStatusUpdateResponseTest {

    @Test
    void testEnumSerialisation() throws JsonProcessingException {
        ClaimStatusUpdateResponse response = new ClaimStatusUpdateResponse();
        response.setProcessingStatus(ProcessingStatus.PROCESSED);

        String json = new ObjectMapper().writeValueAsString(response);

        assertNotNull(json, "ClaimStatusUpdateResponse JSON should not be null");
        assertEquals("{\"processingStatus\":\"processed\"}", json, "ClaimStatusUpdateResponse JSON has unexpected value");
    }

    @Test
    void testEnumDeserialization() throws JsonProcessingException {
        String json = "{\"processingStatus\":\"queued\"}";

        ClaimStatusUpdateResponse response = new ObjectMapper().readValue(json, ClaimStatusUpdateResponse.class);

        assertNotNull(response, "ClaimStatusUpdateResponse should not be null");
        assertEquals(ProcessingStatus.QUEUED, response.getProcessingStatus(), "");
    }
}
