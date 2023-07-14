package uk.gov.moj.sdt.cmc.consumers.model;

import org.junit.jupiter.api.Test;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.Utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefendantResponseTypeTest extends AbstractSdtUnitTestBase {

    private DefendantResponseType defendantResponseType;

    @Override
    protected void setUpLocalTests() {
        defendantResponseType = new DefendantResponseType();
    }

    @Test
    void testFiledDate() {
        LocalDateTime testDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        Calendar testFiledDate = Utilities.convertLocalDateTimeToCalendar(testDate);

        defendantResponseType.setFiledDate(testFiledDate);
        Calendar actualFiledDate = defendantResponseType.getFiledDate();

        assertDate(testDate, actualFiledDate);
    }

    @Test
    void testEventCreatedDateOnMcol() {
        LocalDateTime testDate = LocalDateTime.of(2023, 2, 2, 0, 0);
        Calendar testEventCreatedDateOnMcol = Utilities.convertLocalDateTimeToCalendar(testDate);

        defendantResponseType.setEventCreatedDateOnMcol(testEventCreatedDateOnMcol);
        Calendar actualEventCreatedDateOnMcol = defendantResponseType.getEventCreatedDateOnMcol();

        assertDate(testDate, actualEventCreatedDateOnMcol);
    }

    @Test
    void testRaisedOnMcol() {
        defendantResponseType.setRaisedOnMcol(true);

        assertTrue(defendantResponseType.isRaisedOnMcol(), "Unexpected raisedOnMcol value");
    }

    @Test
    void testResponseType() {
        ResponseType responseType = ResponseType.AS;

        defendantResponseType.setResponseType(responseType);

        assertEquals(responseType.value(),
                     defendantResponseType.getResponseType().value(),
                     "Unexpected ResponseType value");
    }

    @Test
    void testDefence() {
        String defence = "This is the defence";

        defendantResponseType.setDefence(defence);

        assertEquals(defence, defendantResponseType.getDefence(), "Unexpected defence value");
    }

    @Test
    void testDefendantId() {
        String defendantId = "1";

        defendantResponseType.setDefendantId(defendantId);

        assertEquals(defendantId, defendantResponseType.getDefendantId(), "Unexpected defendantId value");
    }

    private void assertDate(LocalDateTime expectedDate, Calendar actualDate) {
        // Convert Calendar to a LocalDateTime to allow comparison
        LocalDateTime actualLocalDateTime = LocalDateTime.ofInstant(actualDate.toInstant(), ZoneId.systemDefault());

        assertTrue(expectedDate.isEqual(actualLocalDateTime), "Date does not match the expected date");
    }
}
