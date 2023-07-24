package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InstalmentPaymentTypeTest {

    private InstalmentPaymentType instalmentPaymentType;

    @BeforeEach
    void setUp() {
        instalmentPaymentType = new InstalmentPaymentType();
    }

    @Test
    void testAmount() {
        Long amount = 2L;
        instalmentPaymentType.setAmount(amount);

        Long actualAmount = instalmentPaymentType.getAmount();

        assertNotNull(actualAmount, "InstalmentPaymentType amount should not be null");
        assertEquals(amount, instalmentPaymentType.getAmount(), "InstalmentPaymentType has unexpected amount");
    }

    @Test
    void testGetFrequency() {
        InstalmentFrequencyType instalmentFrequencyType = InstalmentFrequencyType.F;
        instalmentPaymentType.setFrequency(instalmentFrequencyType);

        InstalmentFrequencyType actualInstalmentFrequencyType = instalmentPaymentType.getFrequency();

        assertNotNull(actualInstalmentFrequencyType, "InstalmentPaymentType frequency should not be null");
        assertEquals(instalmentFrequencyType.name(),
                     actualInstalmentFrequencyType.name(),
                     "InstalmentPaymentType has unexpected frequency");
    }
}
