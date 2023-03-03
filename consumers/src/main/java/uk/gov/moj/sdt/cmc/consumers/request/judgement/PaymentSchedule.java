package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "installmentAmount",
    "installmentFrequency",
    "paymentInFullBy",
    "paymentScheduleType"
})
public class PaymentSchedule {

    protected FullPaymentType inFullByPayment;

    protected InstalmentPaymentType instalment;

    protected ImmediatePaymentType immediatePayment;

    public PaymentScheduleType getPaymentScheduleType() {
        if (inFullByPayment != null) {
            return PaymentScheduleType.inFull;
        } else if (instalment != null) {
            return PaymentScheduleType.installment;
        }
        return PaymentScheduleType.immediate;
    }

    public Date getPaymentInFullBy() {
        return inFullByPayment != null ? inFullByPayment.fullByDate : null;
    }

    public Long getInstallmentAmount() {
        return instalment != null ? instalment.getAmount() : null;
    }

    public InstalmentFrequencyType getInstallmentFrequency() {
        return instalment != null ? instalment.getFrequency() : null;
    }
}
