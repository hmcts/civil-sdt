package uk.gov.moj.sdt.producers.prototype.greetingendpoint;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * WSDL File for GreetingEndpoint
 *
 * This class was generated by Apache CXF 2.7.5
 * 2013-07-23T11:05:58.246+01:00
 * Generated source version: 2.7.5
 * 
 */
@WebServiceClient(name = "GreetingEndpoint", 
                  wsdlLocation = "src/main/resources/wsdl/GreetingEndpoint.wsdl",
                  targetNamespace = "http://prototype.producers.sdt.moj.gov.uk/GreetingEndpoint") 
public class GreetingEndpoint extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://prototype.producers.sdt.moj.gov.uk/GreetingEndpoint", "GreetingEndpoint");
    public final static QName GreetingEndpointPort = new QName("http://prototype.producers.sdt.moj.gov.uk/GreetingEndpoint", "GreetingEndpointPort");
    static {
        URL url = GreetingEndpoint.class.getResource("src/main/resources/wsdl/GreetingEndpoint.wsdl");
        if (url == null) {
            url = GreetingEndpoint.class.getClassLoader().getResource("src/main/resources/wsdl/GreetingEndpoint.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(GreetingEndpoint.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "src/main/resources/wsdl/GreetingEndpoint.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public GreetingEndpoint(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public GreetingEndpoint(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public GreetingEndpoint() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns IGreetingEndpointPortType
     */
    @WebEndpoint(name = "GreetingEndpointPort")
    public IGreetingEndpointPortType getGreetingEndpointPort() {
        return super.getPort(GreetingEndpointPort, IGreetingEndpointPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IGreetingEndpointPortType
     */
    @WebEndpoint(name = "GreetingEndpointPort")
    public IGreetingEndpointPortType getGreetingEndpointPort(WebServiceFeature... features) {
        return super.getPort(GreetingEndpointPort, IGreetingEndpointPortType.class, features);
    }

}
