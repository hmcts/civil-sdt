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

package uk.gov.moj.sdt.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.LocalDateTime;

import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;

/**
 * Error log.
 * 
 * @author d130680
 * 
 */
public class ErrorLog extends AbstractDomainObject implements IErrorLog
{
    /**
     * Individual request, null for error raised on bulk file.
     */
    private IIndividualRequest individualRequest;

    /**
     * Date record was created.
     */
    private LocalDateTime createdDate;

    /**
     * Date record was updated.
     */
    private LocalDateTime updatedDate;

    /**
     * The error code.
     */
    private String errorCode;

    /**
     * The error text.
     */
    private String errorText;

    /**
     * Creates an instance of ErrorLog.
     */
    public ErrorLog ()
    {
    }

    /**
     * Creates an instance of ErrorLog.
     * 
     * @param errorCode error code
     * @param errorText error text
     */
    public ErrorLog (final String errorCode, final String errorText)
    {
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.createdDate = LocalDateTime.now ();
    }

    @Override
    public IIndividualRequest getIndividualRequest ()
    {
        return individualRequest;
    }

    @Override
    public void setIndividualRequest (final IIndividualRequest individualRequest)
    {
        this.individualRequest = individualRequest;
    }

    @Override
    public LocalDateTime getCreatedDate ()
    {
        return createdDate;
    }

    @Override
    public void setCreatedDate (final LocalDateTime createdDate)
    {
        this.createdDate = createdDate;
    }

    @Override
    public LocalDateTime getUpdatedDate ()
    {
        return updatedDate;
    }

    @Override
    public void setUpdatedDate (final LocalDateTime updatedDate)
    {
        this.updatedDate = updatedDate;
    }

    @Override
    public String getErrorText ()
    {
        return errorText;
    }

    @Override
    public void setErrorText (final String errorText)
    {
        this.errorText = errorText;
    }

    @Override
    public String getErrorCode ()
    {
        return errorCode;
    }

    @Override
    public void setErrorCode (final String errorCode)
    {
        this.errorCode = errorCode;
    }

    @Override
    public String toString ()
    {
        return new ToStringBuilder (this).appendSuper (super.toString ()).append ("errorCode", errorCode)
                .append ("errorText", errorText).toString ();
    }

}
