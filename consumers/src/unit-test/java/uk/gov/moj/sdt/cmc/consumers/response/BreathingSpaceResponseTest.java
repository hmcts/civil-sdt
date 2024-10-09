package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.moj.sdt.cmc.consumers.response.ResponseStatus.INITIALLY_ACCEPTED;

class BreathingSpaceResponseTest extends ResponseTestBase {

    @Test
    void testConvertToObject() throws JsonProcessingException {
        String responseJson = """
            {
              "responseStatus" : "Initially Accepted"
            }""";

        BreathingSpaceResponse breathingSpaceResponse =
            objectMapper.readValue(responseJson, BreathingSpaceResponse.class);

        assertNotNull(breathingSpaceResponse, "BreathingSpaceResponse should not be null");
        assertEquals(INITIALLY_ACCEPTED,
                     breathingSpaceResponse.getResponseStatus(),
                     "BreathingSpaceResponse has unexpected response status");
    }
}
