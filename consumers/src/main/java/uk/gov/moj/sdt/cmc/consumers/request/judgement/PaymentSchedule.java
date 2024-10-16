package uk.gov.moj.sdt.cmc.consumers.request.judgement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "paymentScheduleType",
    "paymentInFullBy",
    "installmentFrequency",
    "installmentAmount"
})
public class PaymentSchedule {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE_EUROPE_LONDON = "Europe/London";

    protected FullPaymentType inFullByPayment;

    protected InstalmentPaymentType instalment;

    protected ImmediatePaymentType immediatePayment;

    public String getPaymentScheduleType() {
        if (inFullByPayment != null) {
            return PaymentScheduleType.IN_FULL.getScheduleType();
        } else if (instalment != null) {
            return PaymentScheduleType.INSTALLMENT.getScheduleType();
        }
        return PaymentScheduleType.IMMEDIATE.getScheduleType();
    }

    @JsonFormat(pattern = DATE_FORMAT, timezone = TIMEZONE_EUROPE_LONDON)
    public Date getPaymentInFullBy() {
        return inFullByPayment != null ? inFullByPayment.fullByDate : null;
    }

    public Long getInstallmentAmount() {
        return instalment != null ? instalment.getAmount() : null;
    }

    public InstalmentFrequencyType getInstallmentFrequency() {
        return instalment != null ? instalment.getFrequency() : null;
    }

    @JsonIgnore
    public ImmediatePaymentType getImmediatePayment() {
        return immediatePayment;
    }
}
