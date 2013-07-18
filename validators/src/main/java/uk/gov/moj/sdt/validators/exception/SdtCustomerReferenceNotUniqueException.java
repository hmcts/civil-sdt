/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2013 by the Ministry of Justice. All rights reserved.
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
package uk.gov.moj.sdt.validators.exception;

import java.util.List;

/**
 * SDT Customer Reference not unique across data retention period.
 * 
 * @author d130680
 * 
 */
public class SdtCustomerReferenceNotUniqueException extends AbstractBusinessException
{
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * SDT Customer Reference not unique across data retention period.
     * 
     * @param code error code
     * @param description error description
     */
    public SdtCustomerReferenceNotUniqueException (final String code, final String description)
    {
        super (code, description);
    }

    /**
     * SDT Customer Reference not unique across data retention period.
     * 
     * @param code code
     * @param description description
     * @param replacements string replacements with tokens
     */
    public SdtCustomerReferenceNotUniqueException (final String code, final String description,
            final List<String> replacements)
    {
        super (code, description, replacements);
    }

    /**
     * SDT Customer Reference not unique across data retention period.
     * 
     * @param s the s
     */
    public SdtCustomerReferenceNotUniqueException (final String s)
    {
        super (s);
    }

    /**
     * SDT Customer Reference not unique across data retention period.
     * 
     * @param cause the cause
     */
    public SdtCustomerReferenceNotUniqueException (final Throwable cause)
    {
        super (cause);
    }

    /**
     * SDT Customer Reference not unique across data retention period.
     * 
     * @param s the s
     * @param cause the cause
     */
    public SdtCustomerReferenceNotUniqueException (final String s, final Throwable cause)
    {
        super (s, cause);
    }
}
