package uk.gov.moj.sdt.services.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.moj.sdt.dao.GenericDao;
import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class SdtBulkReferenceGeneratorTest extends AbstractSdtUnitTestBase{
    @Mock
    IGenericDao genericDaoMock;

    private static final String FOUR_CHARACTERS_ONLY ="The target application length is expected to be 4 characters.";

    ISdtBulkReferenceGenerator sdtBulkReferenceGenerator;
    @BeforeEach
    @Override
    public void setUp() {

        sdtBulkReferenceGenerator = new SdtBulkReferenceGenerator(genericDaoMock);

    }

    @Test
    void testGetSdtBulkReferenceGenerator() {

        String generatedRef = sdtBulkReferenceGenerator.getSdtBulkReference("MCOL");

        assertNotNull(generatedRef);
    }

    @Test
    void testGetSdtBulkReferenceGeneratorIllegalArgumentException() {

        try {
                sdtBulkReferenceGenerator.getSdtBulkReference(null);
        }catch(IllegalArgumentException ex){

            assertEquals(FOUR_CHARACTERS_ONLY, ex.getMessage());
        }

    }

    @Test
    void testGetSdtBulkReferenceGeneratorTargetLengthIllegalArgumentException() {

        try {
             sdtBulkReferenceGenerator.getSdtBulkReference("over4Characters");
        }catch(IllegalArgumentException ex){

            assertEquals(FOUR_CHARACTERS_ONLY, ex.getMessage());
        }
    }

    @Test
    void testSetGenericDao() {

        IGenericDao mockGenericDao = mock(GenericDao.class);
        SdtBulkReferenceGenerator sdtBulkReferenceGeneratorObj = new SdtBulkReferenceGenerator(mockGenericDao);
        sdtBulkReferenceGeneratorObj.setGenericDao(mockGenericDao);

        assertNotNull(sdtBulkReferenceGeneratorObj,"Object should have been populated");

    }
}
