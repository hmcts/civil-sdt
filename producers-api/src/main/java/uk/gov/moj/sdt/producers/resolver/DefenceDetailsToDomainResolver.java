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
package uk.gov.moj.sdt.producers.resolver;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.LocalDate;

import uk.gov.moj.sdt.domain.RequestDefenceDetail;
import uk.gov.moj.sdt.domain.TargetApplication;
import uk.gov.moj.sdt.domain.api.IRequestDefenceDetail;
import uk.gov.moj.sdt.domain.api.ITargetApplication;
import uk.gov.moj.sdt.mcol.domain.DefenceDetail;
import uk.gov.moj.sdt.mcol.domain.Defendant;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackrequestschema.CriteriaType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackrequestschema.DefenceRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema.DefenceDetailType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema.DefenceDetailsType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema.DefenceResponseType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema.DefendantType;
import uk.gov.moj.sdt.ws._2013.sdt.defencefeedbackresponseschema.ResponseType;

/**
 * Maps incoming request objects to equivalent domain objects.
 * 
 * @author d130680
 * 
 */
public final class DefenceDetailsToDomainResolver
{

    /**
     * Private constructor.
     */
    private DefenceDetailsToDomainResolver ()
    {

    }

    /**
     * Maps the JAXB defence request type object to a defence request domain object.
     * 
     * @param defenceRequest jaxb object
     * @return domain object
     */
    public static IRequestDefenceDetail mapToDefenceDetail (final DefenceRequestType defenceRequest)
    {

        final HeaderType headerType = defenceRequest.getHeader ();
        final CriteriaType criteriaType = defenceRequest.getCriteria ();
        final ITargetApplication targetApplication = new TargetApplication ();
        final IRequestDefenceDetail requestDefenceDetail = new RequestDefenceDetail ();

        requestDefenceDetail.setFromDate (new LocalDate (criteriaType.getMcolDefence ().getFromDate ()));
        requestDefenceDetail.setToDate (new LocalDate (criteriaType.getMcolDefence ().getToDate ()));

        requestDefenceDetail.setSdtCustomerId (headerType.getSdtCustomerId ().intValue ());
        targetApplication.setTargetApplicationCode (headerType.getTargetApplicationId ().value ());

        requestDefenceDetail.setTargetApplication (targetApplication);
        return requestDefenceDetail;
    }

    /**
     * Maps defence request domain object maps to JAXB defence request type.
     * 
     * @param mockDefenceDetailList mock mcol defence details objects
     * @return DefenceResponseType jaxb object
     */
    public static DefenceResponseType mapToDefenceRequestType (final List<DefenceDetail> mockDefenceDetailList)
    {
        final DefenceResponseType defenceResponseType = new DefenceResponseType ();

        //Extract the mock defefence details
        final DefenceDetailsType defenceDetailsType = new DefenceDetailsType ();

        for (DefenceDetail mockDefenceDetail : mockDefenceDetailList)
        {
            final DefenceDetailType defenceDetailType = new DefenceDetailType ();
            final DefenceDetailType.Defendants defendants = new DefenceDetailType.Defendants ();

            defenceDetailType.setClaimNumber (mockDefenceDetail.getClaimNumber ());

            //copy the mock defendant to DefendantType
            for (Defendant mockDefendant : mockDefenceDetail.getDefendants ())
            {
                final DefendantType defendantType = new DefendantType ();
                defendantType.setDefendantId (mockDefendant.getDefendantId ());
                final Calendar calendar = new GregorianCalendar ();
                calendar.setTime (mockDefendant.getFiledDate ());
                defendantType.setFiledDate (calendar);
                defendantType.setResponseType (ResponseType.valueOf (mockDefendant.getResponse ()));
                
                defendants.getDefendant ().add (defendantType);

               
            }
            defenceDetailType.setDefendants (defendants);
            defenceDetailsType.getDefenceDetail ().add (defenceDetailType);
        }
        // Add to the response
        defenceResponseType.setDefenceDetails (defenceDetailsType);
        
        return defenceResponseType;
    }

}
