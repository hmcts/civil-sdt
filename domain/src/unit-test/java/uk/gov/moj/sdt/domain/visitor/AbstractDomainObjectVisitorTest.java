package uk.gov.moj.sdt.domain.visitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import uk.gov.moj.sdt.domain.api.*;
import uk.gov.moj.sdt.utils.visitor.Tree;

import static org.junit.Assert.assertThrows;

class AbstractDomainObjectVisitorTest {

    public AbstractDomainObjectVisitor abstractDomainObjectVisitor;

    private static final String EXPECTED_MESSAGE =
        "Missing visitor implementation: this method should never be called.";

    @Mock
    Tree mockTree;

    @BeforeEach
    public void setUp() {
        abstractDomainObjectVisitor = new AbstractDomainObjectVisitor() {};
    }

    @Test
    void testVisitBulkCustomerThrows() {
        IBulkCustomer mockBulkCustomer = Mockito.mock(IBulkCustomer.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockBulkCustomer, mockTree));
    }

    @Test
    void testVisitBulkSubmissionThrows() {
        IBulkSubmission mockBulkSubmission = Mockito.mock(IBulkSubmission.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockBulkSubmission, mockTree));
    }

    @Test
    void testVisitServiceTypeThrows() {
        IServiceType mockServiceType = Mockito.mock(IServiceType.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockServiceType, mockTree));
    }

    @Test
    void testVisitTargetApplicationThrows() {
        ITargetApplication mockTargetApplication = Mockito.mock(ITargetApplication.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockTargetApplication, mockTree));
    }

    @Test
    void testVisitIndividualRequestThrows() {
        IIndividualRequest mockIndividualRequest = Mockito.mock(IIndividualRequest.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockIndividualRequest, mockTree));
    }

    @Test
    void testVisitSubmitQueryRequestThrows() {
        ISubmitQueryRequest mockSubmitQueryRequest = Mockito.mock(ISubmitQueryRequest.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockSubmitQueryRequest, mockTree));
    }

    @Test
    void testVisitBulkFeedbackRequestThrows() {
        IBulkFeedbackRequest mockBulkFeedbackRequest = Mockito.mock(IBulkFeedbackRequest.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockBulkFeedbackRequest, mockTree));
    }

    @Test
    void testVisitErrorLogThrows() {
        IErrorLog mockErrorLog = Mockito.mock(IErrorLog.class);
        assertThrows(EXPECTED_MESSAGE, UnsupportedOperationException.class,
                     () -> abstractDomainObjectVisitor.visit(mockErrorLog, mockTree));
    }

}
