package uk.gov.moj.sdt.ws._2013.sdt.sdtinternalendpoint;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * Internal API to be used by other HOLI applications, for
 * 			example MCOL
 * 		
 *
 * This class was generated by Apache CXF 2.7.5
 * 2013-09-25T11:27:58.040+01:00
 * Generated source version: 2.7.5
 * 
 */
@WebServiceClient(name = "SdtInternalEndpoint", 
                  wsdlLocation = "src/main/resources/wsdl/SdtInternalEndpoint.wsdl",
                  targetNamespace = "http://ws.sdt.moj.gov.uk/2013/sdt/SdtInternalEndpoint") 
public class SdtInternalEndpoint extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/SdtInternalEndpoint", "SdtInternalEndpoint");
    public final static QName SdtInternalEndpointPort = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/SdtInternalEndpoint", "SdtInternalEndpointPort");
    static {
        URL url = SdtInternalEndpoint.class.getResource("src/main/resources/wsdl/SdtInternalEndpoint.wsdl");
        if (url == null) {
            url = SdtInternalEndpoint.class.getClassLoader().getResource("src/main/resources/wsdl/SdtInternalEndpoint.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(SdtInternalEndpoint.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "src/main/resources/wsdl/SdtInternalEndpoint.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SdtInternalEndpoint(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SdtInternalEndpoint(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SdtInternalEndpoint() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns ISdtInternalEndpointPortType
     */
    @WebEndpoint(name = "SdtInternalEndpointPort")
    public ISdtInternalEndpointPortType getSdtInternalEndpointPort() {
        return super.getPort(SdtInternalEndpointPort, ISdtInternalEndpointPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ISdtInternalEndpointPortType
     */
    @WebEndpoint(name = "SdtInternalEndpointPort")
    public ISdtInternalEndpointPortType getSdtInternalEndpointPort(WebServiceFeature... features) {
        return super.getPort(SdtInternalEndpointPort, ISdtInternalEndpointPortType.class, features);
    }

}
