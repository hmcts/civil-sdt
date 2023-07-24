package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentScheduleTest {

    private PaymentSchedule paymentSchedule;

    @BeforeEach
    void setup() {
        paymentSchedule = new PaymentSchedule();
    }

    @Test
    void testGetPaymentScheduleTypeFull() {
        FullPaymentType fullPaymentType = new FullPaymentType();
        paymentSchedule.setInFullByPayment(fullPaymentType);

        String actualPaymentType = paymentSchedule.getPaymentScheduleType();

        assertEquals(PaymentScheduleType.IN_FULL.getScheduleType(),
                     actualPaymentType,
                     "PaymentSchedule type should be in full");
    }

    @Test
    void testGetPaymentScheduleTypeInstallment() {
        InstalmentPaymentType instalmentPaymentType = new InstalmentPaymentType();
        paymentSchedule.setInstalment(instalmentPaymentType);

        String actualPaymentType = paymentSchedule.getPaymentScheduleType();

        assertEquals(PaymentScheduleType.INSTALLMENT.getScheduleType(),
                     actualPaymentType,
                     "PaymentSchedule type should be installment");
    }

    @Test
    void testGetPaymentScheduleTypeImmediate() {
        ImmediatePaymentType immediatePaymentType = new ImmediatePaymentType();
        paymentSchedule.setImmediatePayment(immediatePaymentType);

        String actualPaymentType = paymentSchedule.getPaymentScheduleType();

        assertEquals(PaymentScheduleType.IMMEDIATE.getScheduleType(),
                     actualPaymentType,
                     "PaymentSchedule type should be immediate");
    }

    @Test
    void testGetPaymentInFullBy() {
        LocalDateTime byLocalDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        FullPaymentType fullPaymentType = new FullPaymentType();
        fullPaymentType.setFullByDate(Date.from(byLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        paymentSchedule.setInFullByPayment(fullPaymentType);

        Date actualDate = paymentSchedule.getPaymentInFullBy();

        assertNotNull(actualDate, "PaymentSchedule payment in full by date should not be null");

        LocalDateTime actualLocalDateTime = LocalDateTime.ofInstant(actualDate.toInstant(), ZoneId.systemDefault());
        assertTrue(byLocalDateTime.isEqual(actualLocalDateTime), "Unexpected payment in full date");
    }

    @Test
    void testGetPaymentInFullByNull() {
        FullPaymentType fullPaymentType = new FullPaymentType();
        fullPaymentType.setFullByDate(null);
        paymentSchedule.setInFullByPayment(fullPaymentType);

        Date actualDate = paymentSchedule.getPaymentInFullBy();

        assertNull(actualDate, "PaymentSchedule payment in full by date should be null");
    }

    @Test
    void testGetInstallmentAmount() {
        Long amount = 3L;
        InstalmentPaymentType instalmentPaymentType = new InstalmentPaymentType();
        instalmentPaymentType.setAmount(amount);
        paymentSchedule.setInstalment(instalmentPaymentType);

        Long actualAmount = paymentSchedule.getInstallmentAmount();

        assertNotNull(actualAmount, "PaymentSchedule installment amount should not be null");
        assertEquals(amount, actualAmount, "Unexpected installment amount");
    }

    @Test
    void testGetInstallmentAmountNull() {
        InstalmentPaymentType instalmentPaymentType = new InstalmentPaymentType();
        instalmentPaymentType.setAmount(null);
        paymentSchedule.setInstalment(instalmentPaymentType);

        Long actualAmount = paymentSchedule.getInstallmentAmount();

        assertNull(actualAmount, "PaymentSchedule installment amount should be null");
    }

    @Test
    void testGetInstallmentFrequency() {
        InstalmentPaymentType instalmentPaymentType = new InstalmentPaymentType();
        instalmentPaymentType.setFrequency(InstalmentFrequencyType.W);
        paymentSchedule.setInstalment(instalmentPaymentType);

        InstalmentFrequencyType actualInstalmentFrequency = paymentSchedule.getInstallmentFrequency();

        assertNotNull(actualInstalmentFrequency, "PaymentSchedule instalment frequency should not be null");
        assertEquals(InstalmentFrequencyType.W.name(),
                     actualInstalmentFrequency.name(),
                     "Unexpected installment frequency");
    }

    @Test
    void testGetInstallmentFrequencyNull() {
        InstalmentPaymentType instalmentPaymentType = new InstalmentPaymentType();
        instalmentPaymentType.setFrequency(null);
        paymentSchedule.setInstalment(instalmentPaymentType);

        InstalmentFrequencyType actualInstalmentFrequency = paymentSchedule.getInstallmentFrequency();

        assertNull(actualInstalmentFrequency, "PaymentSchedule instalment frequency should be null");
    }
}
