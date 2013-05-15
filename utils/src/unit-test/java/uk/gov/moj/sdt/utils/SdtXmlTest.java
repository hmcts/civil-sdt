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
 * @author Simon Holmes
 *
 */
public interface SdtXmlTest {

	/**
	 * The claim number does not match the expected pattern.
	 */
	String CLAIM_NUMBER_PATTERN_INVALID = "cvc-pattern-valid: Value '1234567890' " +
			"is not facet-valid with respect to pattern '[0-9A-Za-z]{8}' " +
			"for type 'documentNumber'." + "cvc-type.3.1.3: The value '1234567890' " +
			"of element 'claimNumber' is not valid.";
	/**
	 * The defendantId is greater than 2, there can only be 2 defendants on a case.
	 */
	String DEFENDANT_ID_VALUE_INVALID = "cvc-pattern-valid: Value '3' is not " +
			"facet-valid with respect to pattern ' |1|2' for type 'defendantId'. " +
			"cvc-attribute.3: The value '3' of " +
			"attribute 'defendantId' on element 'defendant' is not " +
			"valid with respect to its type, 'defendantId'.";
	/**
	 * The time stamp is in an invalid format.
	 */
	String DATE_TIME_INVALID_FORMAT = "cvc-datatype-valid.1.2.1: '2001-12-31T12:00:001' " +
			"is not a valid value for 'dateTime'. " +
			"cvc-type.3.1.3: The value '2001-12-31T12:00:001' " +
			"of element 'filedDate' is not valid.";;
	/**
	 * The response type does not match an enum.
	 */
	String RESPONSE_TYPE_NOT_VALID = "cvc-type.3.1.3:" +
			" The value 'YZ' of element 'responseType' is not valid.  " +
			"cvc-enumeration-valid: Value 'YZ' is not facet-valid with " +
			"respect to enumeration '[DE, DC, PA]'." +
			" It must be a value from the enumeration.";
	/**
	 * The status code does not match an enum.
	 */
	String STATUS_CODE_INVALID = "cvc-enumeration-valid: Value 'INCORRECT' " +
			"is not facet-valid with respect to " +
			"enumeration '[OK, ERROR]'. It must be a value from the enumeration.  " +
			"cvc-attribute.3: The value 'INCORRECT'" +
			" of attribute 'code' on element 'status' is not " +
			"valid with respect to its type, 'statusCode'.";
	/**
	 * The error code is in an invalid format.
	 */
	String ERROR_CODE_INVALID = "cvc-maxLength-valid: Value " +
			"'abcdefghij' with length = '10' is not facet-valid " +
			"with respect to maxLength '8' for type 'stringMaxLength8'.  " +
			"cvc-type.3.1.3: The value 'abcdefghij' " +
			"of element 'code' is not valid.";
	/**
	 * The description given does not match the expected pattern.
	 */
	String ERROR_DESCRIPTION_INVALID =  "cvc-maxLength-valid: Value" +
			" 'Lorem ipsum dolor sit amet, consectetur " +
			"adipiscing elit. Quisque vel est lacus. Integer ut mi facilisis" +
			" nisi varius mattis eu sit amet dui. " +
			"Donec neque purus, rutrum sit amet iaculis nec, faucibus " +
			"porttitor velit. Integer vel pulvinar nunc " +
			"aenean suscipit.' with length = '256' is not" +
			" facet-valid with respect to maxLength '255' " +
			"for type 'stringMaxLength255'. " +
			"cvc-type.3.1.3: The value 'Lorem ipsum dolor sit amet, " +
			"consectetur adipiscing elit. Quisque vel est lacus." +
			" Integer ut mi facilisis nisi varius " +
			"mattis eu sit amet dui. Donec neque purus, rutrum sit " +
			"amet iaculis nec, faucibus porttitor velit." +
			" Integer vel pulvinar nunc aenean suscipit.' of element 'description' is not valid.";
}
