package uk.gov.moj.sdt.cmc.consumers.decoder;

import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.cmc.consumers.exception.CMCCaseLockedException;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.cmc.exception.CMCException;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CMCErrorDecoderTest extends AbstractSdtUnitTestBase {

    private static final String METHOD_KEY = "testMethodKey";
    private static final String REQUEST_URL = "testUrl";

    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_CASE_LOCKED = 423;
    private static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    private static final String ERROR_UNEXPECTED_EXCEPTION = "Error decoder returned an unexpected exception";

    private CMCErrorDecoder cmcErrorDecoder;

    @Override
    protected void setUpLocalTests() {
        cmcErrorDecoder = new CMCErrorDecoder();
    }

    @Test
    void testDecodeCaseLockedException() {
        Response response = createResponse(STATUS_CASE_LOCKED);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCCaseLockedException.class, exception, ERROR_UNEXPECTED_EXCEPTION);
    }

    @Test
    void testDecodeUnexpectedException() {
        String responseBody = "{\"status\" : 404, \"error\" : \"Not Found\"}";
        Response response = createResponseWithBody(STATUS_NOT_FOUND, responseBody);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCException.class, exception, ERROR_UNEXPECTED_EXCEPTION);

        String expectedResponseBody = "status: 404, body: " + responseBody;
        assertEquals(expectedResponseBody, exception.getMessage(), "CMCException has unexpected message");
    }

    @Test
    void testDecodeUnexpectedExceptionNoBody() {
        Response response = createResponse(STATUS_INTERNAL_SERVER_ERROR);

        Exception exception = cmcErrorDecoder.decode(METHOD_KEY, response);

        assertInstanceOf(CMCException.class, exception, ERROR_UNEXPECTED_EXCEPTION);
        assertEquals("status: 500", exception.getMessage(), "CMCException has unexpected message");
    }

    private Response createResponse(int status) {
        return createResponseBuilder(status).build();
    }

    private Response createResponseWithBody(int status, String responseBody) {
        return createResponseBuilder(status).body(responseBody.getBytes(StandardCharsets.UTF_8)).build();
    }

    private Response.Builder createResponseBuilder(int status) {
        Response.Builder builder = Response.builder();
        builder.status(status).request(createRequest());

        return builder;
    }

    /**
     * Create a dummy feign Request.  This is needed to be able to create a feign Response.
     * @return A dummy feign Request
     */
    private Request createRequest() {
        Map<String, Collection<String>> headers = new HashMap<>();
        return Request.create(Request.HttpMethod.POST, REQUEST_URL, headers, null, null, null);
    }
}
