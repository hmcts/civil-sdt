package uk.gov.moj.sdt.cmc.consumers.client.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.moj.sdt.cmc.consumers.api.CMCApi;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;

@ExtendWith(MockitoExtension.class)
public class CMCConsumersClientTestBase extends AbstractSdtUnitTestBase {

    public static final String IDAM_ID_HEADER = "Test IDAM id";
    public static final String SDT_REQUEST_ID = "Test SDT Request id";
    public static final String SERVICE_AUTHORISATION = "Test service authorisation";

    @Mock
    protected CMCApi mockCmcApi;
}
