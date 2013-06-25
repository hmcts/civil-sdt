package uk.gov.moj.sdt.producers.prototype.greetingendpoint;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.7.5
 * 2013-06-25T10:17:31.671+01:00
 * Generated source version: 2.7.5
 * 
 */
@WebService(targetNamespace = "http://prototype.producers.sdt.moj.gov.uk/GreetingEndpoint", name = "IGreetingEndpointPortType")
@XmlSeeAlso({uk.gov.moj.sdt.producers.prototype.common.disc_types.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IGreetingEndpointPortType {

    @WebResult(name = "getGreetingResponseElement", targetNamespace = "http://common.prototype.producers.sdt.moj.gov.uk/disc_types.xml", partName = "parameters")
    @WebMethod
    public uk.gov.moj.sdt.producers.prototype.common.disc_types.GetGreetingResponseType getGreeting(
        @WebParam(partName = "parameters", name = "getGreetingRequestElement", targetNamespace = "http://common.prototype.producers.sdt.moj.gov.uk/disc_types.xml")
        uk.gov.moj.sdt.producers.prototype.common.disc_types.GetGreetingRequestType parameters
    );
}
