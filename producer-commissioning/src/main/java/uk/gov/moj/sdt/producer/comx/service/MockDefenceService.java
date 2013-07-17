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
package uk.gov.moj.sdt.producer.comx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import uk.gov.moj.sdt.mcol.domain.DefenceDetail;
import uk.gov.moj.sdt.mcol.domain.Defendant;
import uk.gov.moj.sdt.service.api.IDefenceService;
import uk.gov.moj.sdt.ws._2013.mcol.submitqueryresponseschema.ResponseType;

/**
 * Implementation for mock MCOL services.
 * 
 * @author d130680
 * 
 */
public class MockDefenceService implements IDefenceService
{

    /**
     * Returns a static list of defence details.
     * 
     * @param fromDate from date
     * @param toDate to date
     * @return list of defence details
     */
    public List<DefenceDetail> getDefenceDetails (final Date fromDate, final Date toDate)
    {

        final List<DefenceDetail> mockDefenceDetails = new ArrayList<DefenceDetail> ();

        // List 1
        DefenceDetail defenceDetail = new DefenceDetail ();
        defenceDetail.setClaimNumber ("00P0X");

        List<Defendant> defendantList = new ArrayList<Defendant> ();

        Defendant defendant = new Defendant ();
        defendant.setDefendantId ("100");
        // CHECKSTYLE:OFF
        defendant.setFiledDate (new GregorianCalendar (2011, 4, 13).getTime ());
        // CHECKSTYLE:ON
        defendant.setResponse (ResponseType.DC.value ());
        defendantList.add (defendant);

        defendant = new Defendant ();
        defendant.setDefendantId ("101");
        // CHECKSTYLE:OFF
        defendant.setFiledDate (new GregorianCalendar (2011, 7, 8).getTime ());
        // CHECKSTYLE:ON
        defendant.setResponse (ResponseType.DE.value ());
        defendantList.add (defendant);

        defenceDetail.setDefendants (defendantList);

        mockDefenceDetails.add (defenceDetail);

        // List 2
        defendant = new Defendant ();
        defenceDetail = new DefenceDetail ();
        defenceDetail.setClaimNumber ("10P0X");
        defendantList = new ArrayList<Defendant> ();

        defendant.setDefendantId ("200");
        // CHECKSTYLE:OFF
        defendant.setFiledDate (new GregorianCalendar (2012, 1, 29).getTime ());
        // CHECKSTYLE:ON
        defendant.setResponse (ResponseType.PA.value ());
        defendantList.add (defendant);

        defenceDetail.setDefendants (defendantList);

        mockDefenceDetails.add (defenceDetail);

        // List 3
        defendant = new Defendant ();
        defenceDetail = new DefenceDetail ();
        defenceDetail.setClaimNumber ("30P0X");
        defendantList = new ArrayList<Defendant> ();

        defendant.setDefendantId ("300");
        // CHECKSTYLE:OFF
        defendant.setFiledDate (new GregorianCalendar (2013, 0, 1).getTime ());
        // CHECKSTYLE:ON
        defendant.setResponse (ResponseType.DE.value ());
        defendantList.add (defendant);

        defendant = new Defendant ();
        defendant.setDefendantId ("301");
        // CHECKSTYLE:OFF
        defendant.setFiledDate (new GregorianCalendar (2013, 1, 5).getTime ());
        // CHECKSTYLE:ON
        defendant.setResponse (ResponseType.PA.value ());
        defendantList.add (defendant);

        defenceDetail.setDefendants (defendantList);

        mockDefenceDetails.add (defenceDetail);

        return mockDefenceDetails;
    }
}
