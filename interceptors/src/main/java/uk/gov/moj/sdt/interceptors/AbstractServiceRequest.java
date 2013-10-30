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
package uk.gov.moj.sdt.interceptors;

import org.springframework.beans.factory.annotation.Autowired;

import uk.gov.moj.sdt.dao.api.IGenericDao;

/**
 * Shared abstract class for audit logging.
 * 
 * @author d195274
 * 
 */
public abstract class AbstractServiceRequest extends AbstractSdtInterceptor
{

    /**
     * The persistence class for this interceptor.
     */
    @Autowired
    private IGenericDao serviceRequestDao;

   
    /**
     * The Service Request Id.
     */
    private Long serviceRequestId; 
   

    /**
     * The soap message as a String.
     */
    private String xmlMessage;

    /**
     * Default constructor.
     * 
     * @param phase the CXF phase to apply this to.
     */
    public AbstractServiceRequest (final String phase)
    {
        super (phase);
    }

   

    /**
     * get.
     * 
     * @return the xml message representing the entire Soap message.
     */
    public String getMessage ()
    {
        return xmlMessage;
    }

    /**
     * set.
     * 
     * @param xmlMessage the original Soap Message expressed as a String.
     */
    public void setMessage (final String xmlMessage)
    {
        this.xmlMessage = xmlMessage;
    }
    
    /**
     * get.
     * @return the serviceRequestId
     */
    public Long getServiceRequestId ()
    {
        return serviceRequestId;
    }

    /**
     * set.
     * @param serviceRequestId the serviceRequestId to set
     */
    public void setServiceRequestId (final Long serviceRequestId)
    {
        this.serviceRequestId = serviceRequestId;
    }

    
}
