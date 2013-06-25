
package uk.gov.moj.sdt.ws._2013.sdt.baseschema;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.gov.moj.sdt.ws._2013.sdt.baseschema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.gov.moj.sdt.ws._2013.sdt.baseschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateStatusType }
     * 
     */
    public CreateStatusType createCreateStatusType() {
        return new CreateStatusType();
    }

    /**
     * Create an instance of {@link StatusType }
     * 
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link UpdateStatusType }
     * 
     */
    public UpdateStatusType createUpdateStatusType() {
        return new UpdateStatusType();
    }

    /**
     * Create an instance of {@link SotSignatureType }
     * 
     */
    public SotSignatureType createSotSignatureType() {
        return new SotSignatureType();
    }

    /**
     * Create an instance of {@link PersonType }
     * 
     */
    public PersonType createPersonType() {
        return new PersonType();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link BulkStatusType }
     * 
     */
    public BulkStatusType createBulkStatusType() {
        return new BulkStatusType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

}
