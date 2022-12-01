/* Copyrights and Licenses
 *
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
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
package uk.gov.moj.sdt.producers.comx.cache;

import org.springframework.stereotype.Component;
import uk.gov.moj.sdt.domain.ErrorMessage;
import uk.gov.moj.sdt.domain.api.IDomainObject;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.domain.cache.api.ICacheable;
import uk.gov.moj.sdt.services.cache.api.IErrorMessagesCache;

/**
 * Mock implementation of the ErrorMessagessCache.
 *
 * @author d164190
 *
 */
@Component("MockErrorMessagesCache")
public class MockErrorMessagesCache implements ICacheable, IErrorMessagesCache
{

    @SuppressWarnings ("unchecked")
    @Override
    public <DomainType extends IDomainObject> DomainType
            getValue (final Class<DomainType> domainType, final String key)
    {
        final IErrorMessage errorMessage = new ErrorMessage ();

        if (IErrorMessage.ErrorCode.DUP_CUST_FILEID.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.DUP_CUST_FILEID.toString ());
            errorMessage.setErrorText ("Duplicate User File Reference {0} supplied. This was previously used to "
                    + "submit a Bulk Request on {1} and the SDT Bulk Reference {2} was allocated.");
        }
        else if (IErrorMessage.ErrorCode.REQ_COUNT_MISMATCH.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.REQ_COUNT_MISMATCH.toString ());
            errorMessage.setErrorText ("Unexpected Total Number of Requests identified. {0} requested identified,"
                    + " {1} requests expected in Bulk Request {2}.");
        }
        else if (IErrorMessage.ErrorCode.SDT_INT_ERR.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.SDT_INT_ERR.toString ());
            errorMessage.setErrorText ("A system error has occurred. Please contact {0} for assistance.");
        }
        else if (IErrorMessage.ErrorCode.CUST_NOT_SETUP.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.CUST_NOT_SETUP.toString ());
            errorMessage.setErrorText ("The Bulk Customer organisation is not setup to send Service Request "
                    + "messages to the {0}. Please contact {1} for assistance.");
        }
        else if (IErrorMessage.ErrorCode.CUST_ID_INVALID.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.CUST_ID_INVALID.toString ());
            errorMessage.setErrorText ("The Bulk Customer organisation does not have an SDT Customer ID set up. "
                    + "Please contact {0} for assistance.");
        }
        else if (IErrorMessage.ErrorCode.BULK_REF_INVALID.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.BULK_REF_INVALID.toString ());
            errorMessage.setErrorText ("There is no Bulk Request submission associated with your account for the"
                    + " supplied SDT Bulk Reference {0}.");
        }
        else if (IErrorMessage.ErrorCode.DUP_CUST_REQID.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.DUP_CUST_REQID.toString ());
            errorMessage.setErrorText ("Duplicate Unique Request Identifier submitted {0}.");
        }

        else if (IErrorMessage.ErrorCode.DUPLD_CUST_REQID.toString ().equals (key))
        {
            errorMessage.setErrorCode (IErrorMessage.ErrorCode.DUP_CUST_REQID.toString ());
            errorMessage.setErrorText ("Unique Request Identifier has been specified more than once "
                    + "within the originating Bulk Request.");
        }
        return (DomainType) errorMessage;

    }

}
