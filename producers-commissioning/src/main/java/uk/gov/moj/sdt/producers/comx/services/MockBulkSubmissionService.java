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
package uk.gov.moj.sdt.producers.comx.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.domain.api.IBulkSubmission;
import uk.gov.moj.sdt.services.api.IBulkSubmissionService;
import uk.gov.moj.sdt.services.utils.api.ISdtBulkReferenceGenerator;
import uk.gov.moj.sdt.utils.mbeans.SdtMetricsMBean;

/**
 * Mock Bulk Submission Service, this class is only here so the commissioning app can start up without throwing an
 * error due to missing implementation. Since we are mocking at DAO level and this class doesn't return anything there
 * is nothing to do.
 * 
 * @author d130680
 * 
 */
public class MockBulkSubmissionService implements IBulkSubmissionService
{
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (MockBulkSubmissionService.class);

    /**
     * SDT Bulk reference generator.
     */
    private ISdtBulkReferenceGenerator sdtBulkReferenceGenerator;

    @Override
    public void saveBulkSubmission (final IBulkSubmission bulkSubmission)
    {
        // Set the SDT Bulk Reference for the handler's transformer to use
        bulkSubmission.setSdtBulkReference (sdtBulkReferenceGenerator.getSdtBulkReference (bulkSubmission
                .getTargetApplication ().getTargetApplicationCode ()));

        // Update last seen bulk reference.
        SdtMetricsMBean.getMetrics ().setLastBulkSubmitRef (bulkSubmission.getSdtBulkReference ());
    }

    /**
     * Set SDT Bulk Reference Generator.
     * 
     * @param sdtBulkReferenceGenerator sdt bulk reference generator
     */
    public void setSdtBulkReferenceGenerator (final ISdtBulkReferenceGenerator sdtBulkReferenceGenerator)
    {
        this.sdtBulkReferenceGenerator = sdtBulkReferenceGenerator;
    }
}
