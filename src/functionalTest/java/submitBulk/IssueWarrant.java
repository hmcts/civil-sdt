
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;

import net.thucydides.core.annotations.Step;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.CoreMatchers.containsString;
import io.restassured.path.xml.XmlPath;
import org.junit.jupiter.api.Assertions;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.matchesRegex;
import functions.XMLFunctions;


import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SerenityRunner.class)
public class IssueWarrant {

    private String xml;
    private Response response;
    private int randomNumber;
    String custId = "10000002";
    String requestCount = "1";
    private String custIDInvalidErrorCode = "CUST_ID_INVALID";
    private String sdtBulkReferenceRegex = "MCOL-\\d+-\\d+";
    private String custIDInvalidDesc = "The Bulk Customer organisation does not have an SDT Customer ID set up";
    private String requestCountErrorCode = "REQ_COUNT_MISMATCH";
    private String requestCountErrorDesc = "Unexpected Total Number of Requests identified";
    XMLFunctions XMLFunctions = new XMLFunctions();

    @Step("Given the soap request body")
    public String soapRequestBody() {
        String xmlContent = "";
        try {
            xmlContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("warrant.xml").toURI())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        randomNumber = XMLFunctions.generateRandomNumber();
        xmlContent = xmlContent.replace("RANDOM_NUMBER", Integer.toString(randomNumber));  
        xmlContent = xmlContent.replace("CUSTOMER_ID",custId);
        xmlContent = xmlContent.replace("REQUEST_COUNT",requestCount);
        return xmlContent;
    }

    @Before
    public void setUp() {
        this.xml = soapRequestBody();
        this.response = soapResponse(xml);
    }

    @Step("When we send the soap request and get the response")
    public Response soapResponse(String xml) {
        Response response = RestAssured
                .given()
                .baseUri("http://civil-sdt-demo.service.core-compute-demo.internal")
                .basePath("/producers/service/sdtapi")
                .header("Content-Type", "application/soap+xml;charset=UTF-8")
                .body(xml)
                .post();
        return response;
    }

    @Step("Then the status code should be 200")
    public void checkStatusCode(Response response) {
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test
    public void issueWarrantResponseCode() {

        XmlPath xmlPath = new XmlPath(XMLFunctions.getCleanResponse(response)).setRoot("Envelope.Body.bulkResponse");
        String xmlResponseCode = xmlPath.getString("status.@code");
        String customerReference = xmlPath.getString("customerReference");
        String sdtBulkReference = xmlPath.getString("sdtBulkReference");
        String submittedDate = xmlPath.getString("submittedDate");
        String sdtService = xmlPath.getString("sdtService");
        String requestCount = xmlPath.getString("requestCount");
        int statusCode = response.getStatusCode();

        System.out.println("xmlResponseCode: " + xmlResponseCode);
        System.out.println("Customer Reference: " + customerReference);
        System.out.println("SDT Bulk Reference: " + sdtBulkReference);
        System.out.println("Submitted Date: " + submittedDate);
        System.out.println("SDT Service: " + sdtService);
        System.out.println("Request Count: " + requestCount);

        assertThat(xmlResponseCode, equalTo("Ok"));
        Assertions.assertEquals(200, statusCode);

    }

    @Test
    public void validateCustRefIssueWarrant() {

        XmlPath xmlPath = new XmlPath(XMLFunctions.getCleanResponse(response)).setRoot("Envelope.Body.bulkResponse");
        String customerReference = xmlPath.getString("customerReference");

        assertThat(customerReference, equalTo(Integer.toString(randomNumber)));

    }

    @Test
    public void validateSDTBulkRefIssueWarrant() {
 
        XmlPath xmlPath = new XmlPath(XMLFunctions.getCleanResponse(response)).setRoot("Envelope.Body.bulkResponse");
        String sdtBulkReference = xmlPath.getString("sdtBulkReference");
    
        assertThat(sdtBulkReference, matchesRegex(sdtBulkReferenceRegex));
    }

    @Test
    public void validateCustID() {
 
        custId = "12345678";
        setUp();
        
        XmlPath xmlPath = new XmlPath(XMLFunctions.getCleanResponse(response)).setRoot("Envelope.Body.bulkResponse");
        String errorCode = xmlPath.getString("status.error.code");
        String errorDescription = xmlPath.getString("status.error.description");

        assertThat(errorCode, equalTo(custIDInvalidErrorCode));
        assertThat(errorDescription, containsString(custIDInvalidDesc));
        
    }

    @Test
    public void validateRequestCount() {
 
        requestCount = "2";
        setUp();
        
        XmlPath xmlPath = new XmlPath(XMLFunctions.getCleanResponse(response)).setRoot("Envelope.Body.bulkResponse");
        String errorCode = xmlPath.getString("status.error.code");
        String errorDescription = xmlPath.getString("status.error.description");

        assertThat(errorCode, equalTo(requestCountErrorCode));
        assertThat(errorDescription, containsString(requestCountErrorDesc));
        
    }

    // @Test
    // public void issueWarrantResponseDescription() {

    //     checkStatusCode(response);

    //     String xmlResponse = response.asString();
    //     xmlResponse = removeNamespaces(xmlResponse);

    //     XmlPath xmlPath = new XmlPath(xmlResponse).setRoot("Envelope.Body.bulkResponse.status.error");
    //     String errorDescription = xmlPath.getString("description");

    //     assertThat(errorDescription, containsString("Duplicate User File Reference 91 supplied"));
    // }

    @Test
    public void issueWarrantResponseStructure() {
        checkStatusCode(response);

        String xmlResponse = response.asString();
        xmlResponse = XMLFunctions.removeNamespaces(xmlResponse);

        assertThat(xmlResponse, containsString("status"));
        assertThat(xmlResponse, containsString("customerReference"));
        assertThat(xmlResponse, containsString("code"));
        assertThat(xmlResponse, containsString("sdtBulkReference"));
    }
}
