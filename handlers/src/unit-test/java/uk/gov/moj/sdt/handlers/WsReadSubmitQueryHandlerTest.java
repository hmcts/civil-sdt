package uk.gov.moj.sdt.handlers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.services.api.ISubmitQueryService;
import uk.gov.moj.sdt.transformers.api.ITransformer;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;
import uk.gov.moj.sdt.utils.visitor.VisitableTreeWalker;
import uk.gov.moj.sdt.validators.exception.CustomerNotFoundException;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.SubmitQueryResponseType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@ExtendWith(MockitoExtension.class)
public class WsReadSubmitQueryHandlerTest extends AbstractSdtUnitTestBase {

    @Mock
    ISubmitQueryService mockSubmitQueryService;

    @Mock
    ITransformer<SubmitQueryRequestType, SubmitQueryResponseType, ISubmitQueryRequest, ISubmitQueryRequest> mockTransformer;

    @Mock
    SubmitQueryRequestType mockSubmitQueryRequestType;

    @Mock
    SubmitQueryResponseType mockSubmitQueryResponseType;

    @Mock
    ISubmitQueryRequest mockSubmitQueryRequest;

    @Mock
    HeaderType mockHeaderType;

    @Spy
    WsReadSubmitQueryHandler wsReadSubmitQueryHandler = new WsReadSubmitQueryHandler(null, null);

    long startingSubmitQueryCount;

    @BeforeEach
    @Override
    public void setUp() {
        wsReadSubmitQueryHandler.setSubmitQueryService(mockSubmitQueryService);
        wsReadSubmitQueryHandler.setTransformer(mockTransformer);

        startingSubmitQueryCount = getMetricsSubmitQueryCount();

        when(mockSubmitQueryRequestType.getHeader()).thenReturn(mockHeaderType);
        when(mockHeaderType.getSdtCustomerId()).thenReturn(1L);
        when(mockHeaderType.getQueryReference()).thenReturn("1");
    }

    @Test
    public void testSubmitQuery() {

        when(mockTransformer.transformJaxbToDomain(mockSubmitQueryRequestType))
            .thenReturn(mockSubmitQueryRequest);
        when(mockTransformer.transformDomainToJaxb(mockSubmitQueryRequest))
            .thenReturn(mockSubmitQueryResponseType);

        SubmitQueryResponseType result;

        try (MockedStatic<VisitableTreeWalker> mockStaticVisitableTreeWalker = Mockito.mockStatic(VisitableTreeWalker.class)) {
            mockStaticVisitableTreeWalker.when(() -> VisitableTreeWalker.walk(mockSubmitQueryRequest, "Validator"))
                .then((Answer<Void>) invocation -> null);

            result = wsReadSubmitQueryHandler.submitQuery(mockSubmitQueryRequestType);

            verifyStatic(VisitableTreeWalker.class);
            VisitableTreeWalker.walk(mockSubmitQueryRequest, "Validator");

            verify(mockSubmitQueryService).submitQuery(mockSubmitQueryRequest);

        }

        long currentSubmitQueryCount = getMetricsSubmitQueryCount();

        Assertions.assertTrue(startingSubmitQueryCount < currentSubmitQueryCount);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockSubmitQueryResponseType, result);
    }

    @Test
    public void testSubmitQueryThrowsAbstractBusinessException() throws Exception {

        CustomerNotFoundException exception = new CustomerNotFoundException("MOCK_CODE", "MOCK_DESCRIPTION");

        when(mockTransformer.transformJaxbToDomain(mockSubmitQueryRequestType))
            .thenThrow(exception);

        wsReadSubmitQueryHandler.submitQuery(mockSubmitQueryRequestType);

        verifyPrivate(wsReadSubmitQueryHandler)
            .invoke(
                "handleBusinessException",
                any(CustomerNotFoundException.class),
                any(SubmitQueryResponseType.class)
            );

        long currentSubmitQueryCount = getMetricsSubmitQueryCount();

        Assertions.assertTrue(startingSubmitQueryCount < currentSubmitQueryCount);
    }

    private static long getMetricsSubmitQueryCount() {
        return (long) ReflectionTestUtils.invokeGetterMethod(SdtMetricsMBean.getMetrics(), "getSubmitQueryCount");
    }
}
