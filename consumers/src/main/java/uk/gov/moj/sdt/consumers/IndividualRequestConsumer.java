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
package uk.gov.moj.sdt.consumers;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.gov.moj.sdt.consumers.api.IIndividualRequestConsumer;
import uk.gov.moj.sdt.consumers.exception.OutageException;
import uk.gov.moj.sdt.consumers.exception.SoapFaultException;
import uk.gov.moj.sdt.consumers.exception.TimeoutException;
import uk.gov.moj.sdt.domain.api.IIndividualRequest;
import uk.gov.moj.sdt.domain.api.IServiceRouting;
import uk.gov.moj.sdt.domain.api.IServiceType;
import uk.gov.moj.sdt.transformers.api.IConsumerTransformer;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvrequestschema.IndividualRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.targetapp.indvresponseschema.IndividualResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.targetappinternalendpoint.ITargetAppInternalEndpointPortType;

/**
 * Consumer for the Individual Request processing.
 * 
 * @author Manoj Kulkarni
 * 
 */
// CHECKSTYLE:OFF
public class IndividualRequestConsumer extends AbstractWsConsumer implements IIndividualRequestConsumer
{
    // CHECKSTYLE:ON

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog (IndividualRequestConsumer.class);

    /**
     * Consumer transformer for individual request.
     */
    // CHECKSTYLE:OFF
    private IConsumerTransformer<IndividualResponseType, IndividualRequestType, IIndividualRequest, IIndividualRequest> transformer;

    // CHECKSTYLE:ON

    @Override
    public void processIndividualRequest (final IIndividualRequest individualRequest, final long connectionTimeOut,
                                          final long receiveTimeOut) throws OutageException, TimeoutException
    {
        LOGGER.info ("[processIndividualRequest] started");

        try
        {
            // Transform domain object to web service object
            final IndividualRequestType individualRequestType =
                    this.transformer.transformDomainToJaxb (individualRequest);

            // Process and call the end point web service
            final IndividualResponseType responseType =
                    this.invokeTargetAppService (individualRequestType, individualRequest, connectionTimeOut,
                            receiveTimeOut);

            this.transformer.transformJaxbToDomain (responseType, individualRequest);
        }
        catch (final javax.xml.ws.WebServiceException f)
        {
            LOGGER.error (f);
            if (f.getCause () instanceof ConnectException)
            {
                throw new OutageException ("OUTAGE_ERROR", "Could not send message as server cannot be reached.");
            }
            else if (f.getCause () instanceof SocketTimeoutException)
            {
                throw new TimeoutException ("TIMEOUT_ERROR", "Read time out error");
            }
            else if (f.getCause () instanceof org.apache.cxf.binding.soap.SoapFault)
            {
                throw new SoapFaultException ("SOAP_FAULT", f.getMessage ());
            }

        }
        finally
        {
            LOGGER.info ("[processIndividualRequest] completed");
        }

    }

    /**
     * 
     * @param request the individual request JAXB model
     * @param iRequest the individual request domain object
     * @param connectionTimeOut the connection time out from the global parameter value
     * @param receiveTimeOut the receive time out from the global parameter value.
     * @return the IndividualResponseType object after calling the web service of target app.
     */
    private IndividualResponseType invokeTargetAppService (final IndividualRequestType request,
                                                           final IIndividualRequest iRequest,
                                                           final long connectionTimeOut, final long receiveTimeOut)
    {
        final IServiceRouting serviceRouting =
                iRequest.getBulkSubmission ().getTargetApplication ()
                        .getServiceRouting (IServiceType.ServiceTypeName.SUBMIT_INDIVIDUAL);

        final String webServiceEndPoint = serviceRouting.getWebServiceEndpoint ();

        // Get the client interface
        final ITargetAppInternalEndpointPortType client =
                super.createClient (webServiceEndPoint, connectionTimeOut, receiveTimeOut);

        // Call the specific business method for this text - note that a single test can only use one web service
        // business method.
        return client.submitIndividual (request);

    }

    /**
     * 
     * @param transformer the transformer
     */
    // CHECKSTYLE:OFF
    public
            void
            setTransformer (final IConsumerTransformer<IndividualResponseType, IndividualRequestType, IIndividualRequest, IIndividualRequest> transformer)
    {
        this.transformer = transformer;
    }

    // CHECKSTYLE:ON

    /**
     * 
     * @return the transformer for IndividualRequestConsumer
     */
    public IConsumerTransformer<IndividualResponseType, IndividualRequestType, IIndividualRequest, IIndividualRequest>
            getTransformer ()
    {
        return this.transformer;
    }

}
