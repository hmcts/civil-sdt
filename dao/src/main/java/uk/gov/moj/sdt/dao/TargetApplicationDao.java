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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import uk.gov.moj.sdt.dao.api.ITargetApplicationDao;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.ITargetApplication;

/**
 * Implements specific DAO functionality based on {@link ITargetApplicationDao}. This is a derived DAO extending
 * {@link GenericDao} which provides generic Hibernate access. This specific DAO exists in order to construct domain
 * specific selections where column matches are needed on columns other than the id. For each domain specific query, it
 * constructs an array of {@link org.hibernate.criterion.Criterion} which are passed to the generic method
 * {@link uk.gov.moj.sdt.dao.GenericDao#query(Class, org.hibernate.criterion.Criterion...)}.
 * 
 * @author Son Loi
 */
public class TargetApplicationDao extends GenericDao implements ITargetApplicationDao
{
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (TargetApplicationDao.class);

    /**
     * Default constructor for {@link GenericDaoTest}.
     */
    public TargetApplicationDao ()
    {
        super ();
    }

    @Override
    public List<ITargetApplication> getTargetApplication (final IBulkCustomer bulkCustomer) throws DataAccessException
    {
        // TODO - Implement this method.
        return null;
    }
}
