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
package uk.gov.moj.sdt.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.moj.sdt.dao.api.IGenericDao;
import uk.gov.moj.sdt.domain.api.IErrorMessage;
import uk.gov.moj.sdt.test.utils.AbstractIntegrationTest;
import uk.gov.moj.sdt.test.utils.DBUnitUtility;

/**
 * Test {@link ErrorMessageDao} query methods.
 *
 * @author Robin Compston
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
        "classpath*:/uk/gov/moj/sdt/domain/**/spring*.xml", "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
public class ErrorMessageDaoTest extends AbstractIntegrationTest {

    /**
     * Logger object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorMessageDaoTest.class);

    /**
     * Default constructor for {@link BulkCustomerDaoTest}.
     */
    public ErrorMessageDaoTest() {
        super();
        DBUnitUtility.loadDatabase(getClass(), false);
    }

    /**
     * Tests {@link uk.gov.moj.sdt.dao.IGenericDao} query.
     */
    @Test
    public void testGetAllErrorMessages() {
        final IGenericDao genericDao =
                (IGenericDao) this.applicationContext.getBean("uk.gov.moj.sdt.dao.api.IGenericDao");

        final IErrorMessage[] errorMessages = genericDao.query(IErrorMessage.class);

        LOGGER.debug("Retrieved " + errorMessages.length + " error message(s).");

        Assert.assertEquals(2, errorMessages.length);

        return;
    }
}
