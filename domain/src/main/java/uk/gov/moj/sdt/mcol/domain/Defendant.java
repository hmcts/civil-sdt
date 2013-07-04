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
package uk.gov.moj.sdt.mcol.domain;

import java.util.Date;

/**
 * Defendant object as returned by MCOL.
 * 
 * @author d130680
 * 
 */
public class Defendant
{

    /**
     * The defendant ID.
     */
    private String defendantId;

    /**
     * The filed date.
     */
    private Date filedDate;

    /**
     * The response which can be of DE, DC or PA.
     */
    private String response;

    /**
     * Get the defendant Id.
     * 
     * @return defendant Id
     */
    public String getDefendantId ()
    {
        return defendantId;
    }

    /**
     * Set the defendant Id.
     * 
     * @param defendantId defendant Id
     */
    public void setDefendantId (final String defendantId)
    {
        this.defendantId = defendantId;
    }

    /**
     * Get the filed date.
     * 
     * @return filed date
     */
    public Date getFiledDate ()
    {
        return filedDate;
    }

    /**
     * Set the filed date.
     * 
     * @param filedDate filed date
     */
    public void setFiledDate (final Date filedDate)
    {
        this.filedDate = filedDate;
    }

    /**
     * Get the response.
     * 
     * @return response
     */
    public String getResponse ()
    {
        return response;
    }

    /**
     * Set the response.
     * 
     * @param response response.
     */
    public void setResponse (final String response)
    {
        this.response = response;
    }
}
