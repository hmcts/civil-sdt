package steps;

import net.thucydides.core.annotations.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.nio.file.Files;
import java.nio.file.Paths;


public class ClaimStatusSteps {

    private String xml;
    private Response response;
    private int randomNumber;

    @Step("Given the soap request body")
    public String soapRequestBody() {
        String xmlContent = "";
        try {
            xmlContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("statusUpdate.xml").toURI())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        randomNumber = (int)(Math.random() * 10000);  // Generate a random number
        xmlContent = xmlContent.replace("RANDOM_NUMBER", Integer.toString(randomNumber));  // Replace the token

        return xmlContent;
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

    public String removeNamespaces(String xmlResponse) {
        return xmlResponse.replaceAll("xmlns.*?(\"|\').*?(\"|\')", "")
                          .replaceAll("(<)(\\w+:)(.*?>)", "$1$3")
                          .replaceAll("(</)(\\w+:)(.*?>)", "$1$3");
    }

    public String getCleanResponse() {
        String xmlResponse = response.asString();
        xmlResponse = removeNamespaces(xmlResponse);
        return xmlResponse;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }
}
