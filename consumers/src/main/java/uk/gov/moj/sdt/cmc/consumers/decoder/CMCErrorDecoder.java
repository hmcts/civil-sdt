package uk.gov.moj.sdt.cmc.consumers.decoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCCaseLockedException;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCRejectedException;
import uk.gov.moj.sdt.cmc.consumers.response.ErrorResponse;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class CMCErrorDecoder implements ErrorDecoder {

    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_LOCKED = 423;

    @Override
    public Exception decode(String methodKey, Response response) {
        int responseStatus = response.status();
        String responseBody = responseBodyToString(response.body());

        if (responseStatus == STATUS_BAD_REQUEST) {
            CMCRejectedException cmcRejectedException = createCMCRejectedException(responseBody);
            return Objects.requireNonNullElseGet(cmcRejectedException,
                                                 () -> createCMCException(responseStatus, responseBody));
        } else if (responseStatus == STATUS_LOCKED) {
            return new CMCCaseLockedException();
        } else {
            return createCMCException(responseStatus, responseBody);
        }
    }

    /**
     * Convert the body of the response to a string with any line breaks removed.
     *
     * @param responseBody The body of the response
     * @return A string containing the body with line breaks removed
     */
    private String responseBodyToString(Response.Body responseBody) {
        String body = "";

        if (responseBody != null) {
            try (InputStream inputStream = responseBody.asInputStream()) {
                byte[] bodyBytes = Util.toByteArray(inputStream);
                body = new String(bodyBytes, StandardCharsets.UTF_8).replaceAll("[\\r\\n]", "").trim();
            } catch (IOException e) {
                log.error("Unable to read response body, error [{}]", e.getMessage());
            }
        }

        return body;
    }

    private CMCRejectedException createCMCRejectedException(String responseBody) {
        CMCRejectedException cmcRejectedException = null;

        if (responseBody != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

                if (errorResponse.getErrorCode() == null) {
                    log.error("Response body does not contain expected error code field");
                } else {
                    cmcRejectedException =
                        new CMCRejectedException(errorResponse.getErrorCode(), errorResponse.getErrorText());
                }
            } catch (JsonProcessingException e) {
                log.error("Unable to convert response body to CMCRejectedException, error [{}]", e.getMessage());
            }
        }

        return cmcRejectedException;
    }

    private CMCException createCMCException(int status, String responseBody) {
        String messageStatusElement = "status: " + status;
        String messageBodyElement = responseBody.isEmpty() ? "" : ", body: " + responseBody;
        return new CMCException(messageStatusElement + messageBodyElement);
    }
}
