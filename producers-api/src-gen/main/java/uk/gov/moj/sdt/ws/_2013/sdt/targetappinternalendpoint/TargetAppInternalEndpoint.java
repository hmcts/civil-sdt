package uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.5
 * 2013-09-24T18:22:41.643+01:00
 * Generated source version: 2.7.5
 * 
 */
@WebServiceClient(name = "TargetAppInternalEndpoint", 
                  wsdlLocation = "src/main/resources/wsdl/TargetAppInternalEndpoint.wsdl",
                  targetNamespace = "http://ws.sdt.moj.gov.uk/2013/sdt/TargetAppInternalEndpoint") 
public class TargetAppInternalEndpoint extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/TargetAppInternalEndpoint", "TargetAppInternalEndpoint");
    public final static QName TargetAppInternalEndpointPort = new QName("http://ws.sdt.moj.gov.uk/2013/sdt/TargetAppInternalEndpoint", "TargetAppInternalEndpointPort");
    static {
        URL url = TargetAppInternalEndpoint.class.getResource("src/main/resources/wsdl/TargetAppInternalEndpoint.wsdl");
        if (url == null) {
            url = TargetAppInternalEndpoint.class.getClassLoader().getResource("src/main/resources/wsdl/TargetAppInternalEndpoint.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(TargetAppInternalEndpoint.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "src/main/resources/wsdl/TargetAppInternalEndpoint.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public TargetAppInternalEndpoint(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public TargetAppInternalEndpoint(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TargetAppInternalEndpoint() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns ITargetAppInternalEndpointPortType
     */
    @WebEndpoint(name = "TargetAppInternalEndpointPort")
    public ITargetAppInternalEndpointPortType getTargetAppInternalEndpointPort() {
        return super.getPort(TargetAppInternalEndpointPort, ITargetAppInternalEndpointPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ITargetAppInternalEndpointPortType
     */
    @WebEndpoint(name = "TargetAppInternalEndpointPort")
    public ITargetAppInternalEndpointPortType getTargetAppInternalEndpointPort(WebServiceFeature... features) {
        return super.getPort(TargetAppInternalEndpointPort, ITargetAppInternalEndpointPortType.class, features);
    }

}
