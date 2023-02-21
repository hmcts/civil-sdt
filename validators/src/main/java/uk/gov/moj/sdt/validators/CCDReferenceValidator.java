package uk.gov.moj.sdt.validators;

import org.springframework.stereotype.Component;

@Component
public class CCDReferenceValidator {

    private static final String CASE_REFERENCE_REGEX = "(?:^[0-9]{16}$|^\\d{4}-\\d{4}-\\d{4}-\\d{4}$)";

    public boolean isValidCCDReference(String claimNumber) {
        return claimNumber != null && claimNumber.matches(CASE_REFERENCE_REGEX);
    }
}