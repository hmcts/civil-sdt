package uk.gov.moj.sdt.consumers.exception;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlConversionExceptionTest {

    private static final String EXCEPTION_MESSAGE = "Test exception message";

    @Test
    void testConstructor() {
        JAXBException jaxbException = new JAXBException(EXCEPTION_MESSAGE);

        XmlConversionException xmlConversionException = new XmlConversionException(jaxbException);

        String expectedExceptionMessage = JAXBException.class.getName() + ": " + EXCEPTION_MESSAGE;
        assertEquals(expectedExceptionMessage,
                     xmlConversionException.getMessage(),
                     "XmlConversionException has unexpected exception message");
    }
}
