package uk.gov.moj.sdt.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.moj.sdt.cmc.consumers.client.BaseXmlTest;

import java.nio.charset.Charset;
import javax.inject.Inject;

@ActiveProfiles("integration-test")
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 0, stubs="file:src/integrationTest/resources/mappings")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public abstract class WireMockBaseTest extends BaseXmlTest {

    @Inject
    protected WireMockServer wireMockServer;

    @Value("${wiremock.server.port}")
    protected Integer wiremockPort;

    protected static final MediaType JSON_CONTENT_TYPE = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        Charset.forName("utf8"));

    public static final JsonFactory jsonFactory = JsonFactory.builder()
        .disable(JsonFactory.Feature.INTERN_FIELD_NAMES)
        .build();

    public static final ObjectMapper MAPPER = JsonMapper.builder(jsonFactory)
        .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
        .addModule(new JavaTimeModule())
        .build();


    @BeforeEach
    public void initMock() {
        log.info("Wire mock test, host url is {}", "http://localhost:" + wiremockPort);
    }
}
