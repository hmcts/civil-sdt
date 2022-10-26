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
 * $Id: ServiceTypeValidator.java 17075 2013-09-18 14:58:39Z kulkarnim $
 * $LastChangedRevision: 17075 $
 * $LastChangedDate: 2013-09-18 15:58:39 +0100 (Wed, 18 Sep 2013) $
 * $LastChangedBy: kulkarnim $ */
package uk.gov.moj.sdt.validators;

import uk.gov.moj.sdt.domain.api.IErrorLog;
import uk.gov.moj.sdt.utils.visitor.api.ITree;
import uk.gov.moj.sdt.validators.api.IErrorLogValidator;

/**
 * Implementation of {@link ErrorLogValidator}.
 *
 * @author Son Loi
 */
public class ErrorLogValidator extends AbstractSdtValidator implements IErrorLogValidator {

    /**
     * No-argument Constructor.
     */
    public ErrorLogValidator() {
    }

    @Override
    public void visit(final IErrorLog errorLog, final ITree tree) {
        // Do nothing since no validation is required
    }

}
