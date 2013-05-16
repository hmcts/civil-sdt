/* Copyrights and Licenses
 * 
 * Copyright (c) 2013 by the Ministry of Justice. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * - All advertising materials mentioning features or use of this software must display the
 * following acknowledgment: "This product includes Money Claims OnLine."
 * - Products derived from this software may not be called "Money Claims OnLine" nor may
 * "Money Claims OnLine" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgment: "This
 * product includes Money Claims OnLine."
 * This software is provided "as is" and any expressed or implied warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the Ministry of Justice or its contributors be liable for any
 * direct, indirect, incidental, special, exemplary, or consequential damages (including, but
 * not limited to, procurement of substitute goods or services; loss of use, data, or profits;
 * or business interruption). However caused any on any theory of liability, whether in contract,
 * strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 * 
 * $Id: $
 * $LastChangedRevision: $
 * $LastChangedDate: $
 * $LastChangedBy: $ */
package uk.gov.moj.sdt.utils;

/**
 * Interface to provide constants to test methods.
 * 
 * @author Simon Holmes
 * 
 */
public interface SdtXmlConstants
{

    /**
     * The claim number fails validation.
     */
    String CLAIM_NUMBER_INVALID_GENERIC = "cvc-type.3.1.3: The value '1234567890' "
            + "of element 'claimNumber' is not valid.";

    /**
     * The warrant number fails validation.
     */
    String WARRANT_NUMBER_INVALID_GENERIC = "cvc-type.3.1.3: The value '0987654321' "
            + "of element 'warrantNumber' is not valid.";

    /**
     * The claim number does not match the expected pattern.
     */
    String DOCUMENT_NUMBER_INVALID_PATTERN_1 = "cvc-pattern-valid: Value '1234567890' "
            + "is not facet-valid with respect to pattern '[0-9A-Za-z]{8}' " + "for type 'documentNumber'.";

    /**
     * The claim number does not match the expected pattern.
     */
    String DOCUMENT_NUMBER_INVALID_PATTERN_2 = "cvc-pattern-valid: Value '0987654321' "
            + "is not facet-valid with respect to pattern '[0-9A-Za-z]{8}' " + "for type 'documentNumber'.";

    /**
     * DefendantId fails validation.
     */
    String DEFENDANT_ID_VALUE_INVALID_GENERIC = "cvc-attribute.3: The value '3' of "
            + "attribute 'defendantId' on element 'defendant' is not "
            + "valid with respect to its type, 'defendantId'.";

    /**
     * The defendantId is greater than 2, there can only be 2 defendants on a case.
     */
    String DEFENDANT_ID_INVALID_PATTERN = "cvc-pattern-valid: Value '3' is not "
            + "facet-valid with respect to pattern ' |1|2' for type 'defendantId'.";

    /**
     * Timestamp fails validation.
     */
    String DATE_TIME_INVALID_FORMAT_GENERIC = "cvc-type.3.1.3: The value '2001-12-31T12:00:001' "
            + "of element 'filedDate' is not valid.";

    /**
     * The time stamp is in an invalid format.
     */
    String DATE_TIME_INVALID_DATE_TIME_FORMAT = "cvc-datatype-valid.1.2.1: '2001-12-31T12:00:001' "
            + "is not a valid value for 'dateTime'.";

    /**
     * The response type fails validation.
     */
    String RESPONSE_TYPE_INVALID_GENERIC = "cvc-type.3.1.3:"
            + " The value 'YZ' of element 'responseType' is not valid.";

    /**
     * The response type does not match an enum.
     */
    String RESPONSE_TYPE_INVALID_ENUM = "cvc-enumeration-valid: Value 'YZ' is not facet-valid with "
            + "respect to enumeration '[DE, DC, PA]'." + " It must be a value from the enumeration.";

    /**
     * StatusCode fails validation.
     */
    String STATUS_CODE_INVALID_GENERIC = "cvc-attribute.3: The value 'NOTENUM1'"
            + " of attribute 'code' on element 'status' is not " + "valid with respect to its type, 'statusCode'.";

    /**
     * The status code does not match an enum.
     */
    String STATUS_CODE_INVALID_ENUM = "cvc-enumeration-valid: Value 'NOTENUM1' "
            + "is not facet-valid with respect to "
            + "enumeration '[OK, ERROR]'. It must be a value from the enumeration.";

    /**
     * StatusCode fails validation.
     */
    String CREATE_STATUS_CODE_INVALID_GENERIC = "cvc-attribute.3: The value 'NOTENUM2'"
            + " of attribute 'code' on element 'status' is not "
            + "valid with respect to its type, 'createStatusCode'.";

    /**
     * The create status code does not match an enum.
     */
    String CREATE_STATUS_CODE_INVALID_ENUM =
            "cvc-enumeration-valid: Value 'NOTENUM2' is not facet-valid with respect to enumeration "
                    + "'[ACCEPTED, INITIALLY_ACCEPTED, REJECTED, ERROR]'. It must be a value from the enumeration.";

    /**
     * Error code fails validation.
     */
    String ERROR_CODE_INVALID_GENERIC =
            "cvc-type.3.1.3: The value 'abcdefghijklmnopqrstuvwxyz123456789' of element 'code' is not valid.";

    /**
     * The error code is in an invalid format.
     */
    String ERROR_CODE_INVALID_MAX_LENGHT_EXCEEDED = "cvc-maxLength-valid: Value "
            + "'abcdefghijklmnopqrstuvwxyz123456789' with length = '35' " + "is not facet-valid "
            + "with respect to maxLength '32' for type 'stringMaxLength32'.";

    /**
     * Description fails validation.
     */
    String ERROR_DESCRIPTION_INVALID_GENERIC = "cvc-type.3.1.3: The value 'Lorem ipsum dolor sit amet, "
            + "consectetur adipiscing elit. Quisque vel est lacus. Integer ut mi facilisis nisi varius "
            + "mattis eu sit amet dui. Donec neque purus, rutrum sit " + "amet iaculis nec, faucibus porttitor velit."
            + " Integer vel pulvinar nunc aenean suscipit.' of element 'description' is not valid.";

    /**
     * The description given does not match the expected pattern.
     */
    String ERROR_DESCRIPTION_INVALID_MAX_LENGHT_EXCEEDED = "cvc-maxLength-valid: Value"
            + " 'Lorem ipsum dolor sit amet, consectetur "
            + "adipiscing elit. Quisque vel est lacus. Integer ut mi facilisis"
            + " nisi varius mattis eu sit amet dui. " + "Donec neque purus, rutrum sit amet iaculis nec, faucibus "
            + "porttitor velit. Integer vel pulvinar nunc " + "aenean suscipit.' with length = '256' is not"
            + " facet-valid with respect to maxLength '255' " + "for type 'stringMaxLength255'.";

    /**
     * When there are more than 2 defendants on the same case present within the xml.
     */
    String DEFENDANTS_NUMBER_GREATER_THAN_2 = "cvc-complex-type.2.4.a: Invalid content was found starting with "
            + "element 'claim'. One of '{\"http://ws.sdt.moj.gov.uk\":defenceDetail}' is expected.";

    /**
     * Sdt customer Id fails validation.
     */
    String SDT_CUSTOMER_ID_INVALID_GENERIC =
            "cvc-type.3.1.3: The value 'abcdefghijklmnopqrstuvwxyz' of element 'sdtCustomerId' is not valid.";

    /**
     * Sdt Customer Id has exceeded max number of characters.
     */
    String SDT_CUSTOMER_ID_INVALID_MAX_LENGTH_EXCEEDED =
            "cvc-maxLength-valid: Value 'abcdefghijklmnopqrstuvwxyz' with length = '26' is not "
                    + "facet-valid with respect to maxLength '24' for type 'sdtCustomerId'.";

    /**
     * Sdt Request fails validation.
     */
    String SDT_REQUEST_ID_INVALID_GENERIC =
            "cvc-type.3.1.3: The value 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz' "
                    + "of element 'sdtRequestId' is not valid.";

    /**
     * Sdt Request Id has exceeded max number of characters.
     */
    String SDT_REQUEST_ID_INVALID_MAX_LENGTH_EXCEEDED =
            "cvc-maxLength-valid: Value 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz' with length = '52' "
                    + "is not facet-valid with respect to maxLength '40' for type 'sdtRequestId'.";

    /**
     * Target System Id is invalid.
     */
    String TARGET_SYSTEM_ID_INVALID_GENERIC =
            "cvc-type.3.1.3: The value 'notValid' of element 'targetSystemId' is not valid.";

    /**
     * Target System Id does not match an enum.
     */
    String TARGET_SYSTEM_ID_INVALID_ENUM = "cvc-enumeration-valid: Value 'notValid' is not facet-valid with respect to"
            + " enumeration '[mcol]'. It must be a value from the enumeration.";

    /**
     * The date is not valid. Suffix of '1' to specify which field is to be checked.
     */
    String INVALID_DATE_GENERIC_1 = "cvc-datatype-valid.1.2.1: '2012-01-011' is not a valid value for 'date'.";

    /**
     * The date is not valid. Suffix of '2' to specify which field is to be checked.
     */
    String INVALID_DATE_GENERIC_2 = "cvc-datatype-valid.1.2.1: '2012-01-012' is not a valid value for 'date'.";

    /**
     * FromDate is not a valid date.
     */
    String TO_DATE_INVALID_FORMAT_1 = "cvc-type.3.1.3: The value '2012-01-011' of element 'fromDate' is not valid.";

    /**
     * ToDate is not a valid date.
     */
    String FROM_DATE_INVALID_FORMAT_1 = "cvc-type.3.1.3: The value '2012-01-012' of element 'toDate' is not valid.";

    /**
     * NCP ID not valid.
     */
    String NCP_ID_INVALID_GENERIC = "cvc-type.3.1.3: The value 'ABCDEF' of element 'ncpId' is not valid.";

    /**
     * NCP ID exceeds maximum number of chars allowed.
     */
    String NCP_ID_INVALID_MAX_LENGTH_EXCEEDED = "cvc-maxLength-valid: Value 'ABCDEF' with length = '6' is not"
            + " facet-valid with respect to maxLength '4' for type 'ncpId'.";

    /**
     * Issue date is not a valid date.
     */
    String ISSUE_DATE_INVALID_FORMAT_1 = "cvc-type.3.1.3: The value '2012-01-011' of element 'issueDate' is not valid.";

    /**
     * Service date is not a valid date.
     */
    String SERVICE_DATE_INVALID_FORMAT_1 =
            "cvc-type.3.1.3: The value '2012-01-012' of element 'serviceDate' is not valid.";

    /**
     * Court code does not match expected pattern.
     */
    String COURT_CODE_INVALID_PATTERN = "cvc-pattern-valid: Value '1234' is not facet-valid with respect to "
            + "pattern '[0-9]{3}' for type 'courtCode'.";

    /**
     * Enforcing court is not valid.
     */
    String ENFORCING_COURT_INVALID_GENERIC =
            "cvc-type.3.1.3: The value '1234' of element 'enforcingCourtCode' is not valid.";

    /**
     * Court name exceeds maximum number of chars allowed.
     */
    String COURT_NAME_INVALID_MAX_LENGTH_EXCEEDED = "cvc-maxLength-valid: Value 'Lorem ipsum dolor sit "
            + "amet viverra fusce.' with length = '41' is not facet-valid with respect "
            + "to maxLength '40' for type 'courtName'.";

    /**
     * Enforcing court name is invalid.
     */
    String ENFORCING_COURT_NAME_INVALID_GENERIC =
            "cvc-type.3.1.3: The value 'Lorem ipsum dolor sit amet viverra fusce.' of element "
                    + "'enforcingCourtName' is not valid.";

    /**
     * Fee is not valid.
     */
    String FEE_INVALID_GENERIC = "cvc-maxInclusive-valid: Value '99999999999999' is not "
            + "facet-valid with respect to maxInclusive '999999999' for type 'amountType999999999'.";

    /**
     * Fee is greater than the expected amount.
     */
    String FEE_INVALID_MAX_INCLUSIVE_EXCEEDED =
            "cvc-type.3.1.3: The value '99999999999999' of element 'fee' is not valid.";

}
