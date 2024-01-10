package uk.gov.moj.sdt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.moj.sdt.base.WireMockBaseTest;
import uk.gov.moj.sdt.request.CMCFeedback;

import java.util.Calendar;
import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeedbackControllerIT extends WireMockBaseTest {

    private String CLAIM_ERROR = "Claim Error";

    private String CLAIM_NUMBER = "123456";

    private String ERROR_CODE = "1234";

    private static final String SDT_REFERENCE = "SDT_REQ_TEST_1";

    private static final String POST_CMC_URL = "/CMCFeedback";

    @Inject
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        super.initMock();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Sql(scripts = {"classpath:sql/IndividualRequestDaoTest.sql"})
    public void shouldCreateClaim() throws Exception {
        mockMvc.perform(post(POST_CMC_URL)
                            .contentType(JSON_CONTENT_TYPE)
                            .content(MAPPER.writeValueAsBytes(createCmcFeedback()))
                            .header("SDTREQUESTID", SDT_REFERENCE))
            .andExpect(status().is(200))
            .andReturn();
    }

    private CMCFeedback createCmcFeedback() {
        CMCFeedback cmcFeedback = new CMCFeedback();
        cmcFeedback.setErrorCode(ERROR_CODE);
        cmcFeedback.setErrorText(CLAIM_ERROR);
        cmcFeedback.setClaimNumber(CLAIM_NUMBER);
        cmcFeedback.setIssueDate(Calendar.getInstance().getTime());
        cmcFeedback.setServiceDate(Calendar.getInstance().getTime());
        return cmcFeedback;
    }

}
