package uk.gov.moj.sdt.cmc.consumers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest extends ResponseTestBase {

    @Test
    void convertToObject() throws JsonProcessingException {
        String responseJson = """
            {
              "errorCode" : 23,
              "errorText" : "Specified claim does not belong to the requesting customer"
            }""";

        ErrorResponse errorResponse = objectMapper.readValue(responseJson, ErrorResponse.class);

        assertEquals(23, errorResponse.getErrorCode(), "Error response has unexpected error code");
        assertEquals("Specified claim does not belong to the requesting customer",
                     errorResponse.getErrorText(),
                     "Error response has unexpected error text");
    }
}
