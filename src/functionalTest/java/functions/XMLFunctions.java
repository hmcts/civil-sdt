package functions;

import io.restassured.response.Response;

public class XMLFunctions {

public String removeNamespaces(String xmlResponse) {
    return xmlResponse.replaceAll("xmlns.*?(\"|\').*?(\"|\')", "")
                      .replaceAll("(<)(\\w+:)(.*?>)", "$1$3")
                      .replaceAll("(</)(\\w+:)(.*?>)", "$1$3");
}

public String getCleanResponse(Response response) {
    String xmlResponse = response.asString();
    xmlResponse = removeNamespaces(xmlResponse);
    return xmlResponse;
}

public int generateRandomNumber(){
    final int randomNumber = (int)(Math.random() * 10000);
    return randomNumber; 
}
}