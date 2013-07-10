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
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */
package uk.gov.moj.sdt.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;

import uk.gov.moj.sdt.domain.RequestDefenceDetail;
import uk.gov.moj.sdt.validators.api.IRequestDefenceDetailValidator;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.validators.exception.InvalidDateRangeException;
import uk.gov.moj.sdt.visitor.AbstractDomainObjectVisitor;

/**
 * Request Defence Detail domain validator.
 * 
 * @author d130680
 * 
 */
public class RequestDefenceDetailValidator extends AbstractDomainObjectVisitor implements
        IRequestDefenceDetailValidator
{

    /**
     * Retention period in days for defence details.
     */
    public static final int DEFENCE_DETAILS_RETENTION_PERIOD = 90;

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (RequestDefenceDetailValidator.class);

    /**
     * No-argument Constructor.
     */
    public RequestDefenceDetailValidator ()
    {
    }

    @Override
    public void visit (final RequestDefenceDetail defenceDetail)
    {
        // TODO - need to validate these fields
        // LOGGER.debug ("validate SDT Customer id");
        //
        // LOGGER.debug ("validate Target application id");

        LOGGER.info ("Request Defence Detail SDT Customer id [" + defenceDetail.getSdtCustomerId () + "].");

        boolean isValid = true;

        final LocalDate toDate = defenceDetail.getToDate ();
        final LocalDate fromDate = defenceDetail.getFromDate ();
        // Check the date range is valid
        isValid = DateValidator.isDateAfter (toDate, fromDate);

        if ( !isValid)
        {
            throw new InvalidDateRangeException (AbstractBusinessException.ErrorCode.INVALID_DATE_RANGE.name (),
                    "SDT detected that an invalid date range was specified for the Request Defence Details.");
        }

        // Check date is within last 90days
        isValid = DateValidator.isDateWitinLastXDays (fromDate, new Integer (DEFENCE_DETAILS_RETENTION_PERIOD));

        if ( !isValid)
        {
            // CHECKSTYLE:OFF
            throw new InvalidDateRangeException (
                    AbstractBusinessException.ErrorCode.ABOVE_MAXIMUM_RETENTION_PERIOD.name (),
                    "SDT has reached the maximum number of Defence Requests that can be forwarded to the online Case Management application (MCOL) for processing at any point in time.");
            // CHECKSTYLE:ON
        }

    }

}
