package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BreathingSpaceResponseTest {

    @Test
    void testEnumSerialisation() throws JsonProcessingException {
        BreathingSpaceResponse response = new BreathingSpaceResponse();
        response.setProcessingStatus(ProcessingStatus.PROCESSED);

        String json = new ObjectMapper().writeValueAsString(response);

        assertNotNull(json, "BreathingSpaceResponse JSON should not be null");
        assertEquals("{\"processingStatus\":\"processed\"}", json, "BreathingSpaceResponse JSON has unexpected value");
    }

    @Test
    void testEnumDeserialization() throws JsonProcessingException {
        String json = "{\"processingStatus\":\"queued\"}";

        BreathingSpaceResponse response = new ObjectMapper().readValue(json, BreathingSpaceResponse.class);

        assertNotNull(response, "BreathingSpaceResponse should not be null");
        assertEquals(ProcessingStatus.QUEUED,
                     response.getProcessingStatus(),
                     "BreathingSpaceResponse has unexpected processing status");
    }
}
