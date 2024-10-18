package uk.gov.moj.sdt.cmc.consumers.decoder;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCCaseLockedException;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CMCErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 423) {
            return new CMCCaseLockedException();
        } else {
            return createCMCException(response);
        }
    }

    private CMCException createCMCException(Response response) {
        String responseBody = responseBodyToString(response.body());

        String messageStatusElement = "status: " + response.status();
        String messageBodyElement = responseBody.isEmpty() ? "" : ", body: " + responseBody;
        return new CMCException(messageStatusElement + messageBodyElement);
    }

    /**
     * Convert the body of the response to a string.  Only used for unexpected errors.
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
                log.error("Unable to read response body");
            }
        }

        return body;
    }
}
