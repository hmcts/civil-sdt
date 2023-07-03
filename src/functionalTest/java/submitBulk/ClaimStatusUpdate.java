import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.restassured.path.xml.XmlPath;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.jupiter.api.Assertions;
import steps.ClaimStatusSteps;


@RunWith(SerenityRunner.class)
public class ClaimStatusUpdate {

    @Steps
    ClaimStatusSteps claimStatusSteps;

    @Before
    public void setUp() {
    claimStatusSteps.setXml(claimStatusSteps.soapRequestBody());
    claimStatusSteps.setResponse(claimStatusSteps.soapResponse(claimStatusSteps.getXml()));
}

    @Test
    public void claimStatusUpdateResponseCode() {
        XmlPath xmlPath = new XmlPath(claimStatusSteps.getCleanResponse()).setRoot("Envelope.Body.bulkResponse");
        String xmlResponseCode = xmlPath.getString("status.@code");

        assertThat(xmlResponseCode, equalTo("Ok"));
        Assertions.assertEquals(200, claimStatusSteps.getResponse().getStatusCode());
    
    }

    @Test
    public void validateCustRefClaimStatusUpdate() {
        XmlPath xmlPath = new XmlPath(claimStatusSteps.getCleanResponse()).setRoot("Envelope.Body.bulkResponse");
        String customerReference = xmlPath.getString("customerReference");

        assertThat(customerReference, equalTo(Integer.toString(claimStatusSteps.getRandomNumber())));
    }

    @Test
    public void validateSDTBulkRefClaimStatusUpdate() {
        XmlPath xmlPath = new XmlPath(claimStatusSteps.getCleanResponse()).setRoot("Envelope.Body.bulkResponse");
        String sdtBulkReference = xmlPath.getString("sdtBulkReference");

        assertThat(sdtBulkReference, matchesRegex("MCOL-\\d+-\\d+"));
    }

    @Test
    public void claimStatusUpdateResponseStructure() {
        claimStatusSteps.checkStatusCode(claimStatusSteps.getResponse());

        String xmlResponse = claimStatusSteps.getCleanResponse();

        assertThat(xmlResponse, containsString("status"));
        assertThat(xmlResponse, containsString("customerReference"));
        assertThat(xmlResponse, containsString("code"));
        assertThat(xmlResponse, containsString("sdtBulkReference"));
    }
}
