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

package uk.gov.moj.sdt.domain.api;

import org.joda.time.LocalDateTime;


/**
 * Interface for all classes implementing {@link IErrorLog}.
 * 
 * @author Manoj Kulkarni
 * 
 */
public interface IErrorLog extends IDomainObject
{

    /**
     * Get the Bulk Submission.
     * 
     * @return bulk submission
     */
    IBulkSubmission getBulkSubmission ();

    /**
     * Set the Bulk Submission.
     * 
     * @param bulkSubmission bulk submission
     */
    void setBulkSubmission (final IBulkSubmission bulkSubmission);

    /**
     * Get the Individual Request.
     * 
     * @return individual request
     */
    IIndividualRequest getIndividualRequest ();

    /**
     * Set the Individual Request.
     * 
     * @param individualRequest individual request
     */
    void setIndividualRequest (final IIndividualRequest individualRequest);

    /**
     * Get the Error Message.
     * 
     * @return error message
     */
    IErrorMessage getErrorMessage ();

    /**
     * Set the Error Message.
     * 
     * @param errorMessage error message
     */
    void setErrorMessage (final IErrorMessage errorMessage);

    /**
     * Get created date.
     * 
     * @return created date
     */
    LocalDateTime getCreatedDate ();

    /**
     * Set created date.
     * 
     * @param createdDate created dated
     */
    void setCreatedDate (final LocalDateTime createdDate);

    /**
     * Get updated date.
     * 
     * @return updated date
     */
    LocalDateTime getUpdatedDate ();

    /**
     * Set updated date.
     * 
     * @param updatedDate updated date
     */
    void setUpdatedDate (final LocalDateTime updatedDate);

    /**
     * Get error Text.
     * 
     * @return error text
     */
    String getErrorText ();

    /**
     * Set error Text.
     * 
     * @param errorText error text
     */
    void setErrorText (final String errorText);

}