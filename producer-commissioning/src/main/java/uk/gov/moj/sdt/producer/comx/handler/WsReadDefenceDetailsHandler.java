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
package uk.gov.moj.sdt.producer.comx.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.domain.api.IRequestDefenceDetail;
import uk.gov.moj.sdt.producers.api.AbstractWsReadHandler;
import uk.gov.moj.sdt.producers.api.IWsReadDefenceDetailsHandler;
import uk.gov.moj.sdt.service.api.IDefenceService;
import uk.gov.moj.sdt.validators.exception.AbstractBusinessException;
import uk.gov.moj.sdt.visitor.VisitableTreeWalker;

/**
 * Implementation for handling request defence details flow.
 * 
 * @author d130680
 * 
 */
public class WsReadDefenceDetailsHandler extends AbstractWsReadHandler implements IWsReadDefenceDetailsHandler
{

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (WsReadDefenceDetailsHandler.class);

    /**
     * Defence services to return defence details.
     * 
     */
    private IDefenceService defenceService;

    /**
     * Processes request defence details message and returns the defence details.
     * 
     * @param defenceRequestType defence details to search for
     * @return defence details response
     */
    // public DefenceResponseType getDefenceDetails (final DefenceRequestType defenceRequestType)
    // {
    // LOGGER.info ("[requestDefenceDetails] started");
    //
    // // Initialise response
    // DefenceResponseType defenceResponse = new DefenceResponseType ();
    // defenceResponse.setStatus (new StatusType ());
    //
    // try
    // {
    //
    // logIncomingRequest (defenceRequestType);
    //
    // // Transform to domain object
    // final IRequestDefenceDetail requestDefenceDetail =
    // DefenceDetailsToDomainResolver.mapToDefenceDetail (defenceRequestType);
    //
    // // Validate domain
    // validateDomain (requestDefenceDetail);
    //
    // // Process validated request
    // defenceResponse = processRequestDefenceDetail (requestDefenceDetail);
    //
    // }
    // catch (final AbstractBusinessException be)
    // {
    // handleBusinessException (be, defenceResponse.getStatus ());
    // }
    // // CHECKSTYLE:OFF
    // catch (final Exception e)
    // // CHECKSTYLE:ON
    // {
    // handleException (e, defenceResponse.getStatus ());
    // }
    // finally
    // {
    // LOGGER.info ("[requestDefenceDetails] completed");
    // }
    //
    // return defenceResponse;
    // }

    /**
     * Log raw request object.
     * 
     * @param defenceDetails request instance.
     */
    // private void logIncomingRequest (final DefenceRequestType defenceDetails)
    // {
    // LOGGER.info ("[logIncomingRequest] - " + defenceDetails);
    // }

    /**
     * Validate to ensure integrity of defence details.
     * 
     * @param defenceDetail defence details
     * @throws AbstractBusinessException business exception
     */
    private void validateDomain (final IRequestDefenceDetail defenceDetail) throws AbstractBusinessException
    {
        LOGGER.debug ("[validateDomain] started");
        VisitableTreeWalker.walk (defenceDetail, "Validator");
        LOGGER.debug ("[validateDomain] finished");
    }

    /**
     * Process request defence detail.
     * 
     * @param requestDefenceDetail defence detail criteria
     * @return DefenceResponseType defense response
     */
    // private DefenceResponseType processRequestDefenceDetail (final IRequestDefenceDetail requestDefenceDetail)
    // {
    // LOGGER.info ("Service called to request defence details");
    // final List<DefenceDetail> list =
    // defenceService.getDefenceDetails (requestDefenceDetail.getFromDate ().toDate (), requestDefenceDetail
    // .getToDate ().toDate ());
    //
    // final DefenceResponseType response = DefenceDetailsToDomainResolver.mapToDefenceRequestType (list);
    //
    // final StatusType status = new StatusType ();
    // response.setStatus (status);
    // status.setCode (StatusCodeType.OK);
    // return response;
    // }

    /**
     * Set the Defence Service.
     * 
     * @param defenceService defence service
     */
    public void setDefenceService (final IDefenceService defenceService)
    {
        this.defenceService = defenceService;
    }

}
