package uk.gov.moj.sdt.cmc.consumers.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static uk.gov.moj.sdt.cmc.consumers.response.JudgmentWarrantStatus.WARRANT_REQUEST_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JudgementWarrantResponseTest {

    private JudgementWarrantResponse judgementWarrantResponse;

    @BeforeEach
    void setup() {
        judgementWarrantResponse = new JudgementWarrantResponse();
    }

    @Test
    void testGetJudgementWarrantStatus() {
        judgementWarrantResponse.setJudgmentWarrantStatus(WARRANT_REQUEST_ERROR);

        assertEquals(WARRANT_REQUEST_ERROR.getMessage(),
                     judgementWarrantResponse.getJudgmentWarrantStatus(),
                     "JudgementWarrantResponse has unexpected judgement warrant status");
    }

    @Test
    void testGetJudgmentWarrantStatusNull() {
        judgementWarrantResponse.setJudgmentWarrantStatus(null);

        assertNull(
            judgementWarrantResponse.getJudgmentWarrantStatus(),
            "JudgementWarrantResponse judgement warrant status should be null"
        );
    }
}
