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
package uk.gov.moj.sdt.transformers;

import java.lang.reflect.Constructor;

import org.junit.Assert;
import org.junit.Test;

import uk.gov.moj.sdt.domain.BulkCustomer;
import uk.gov.moj.sdt.domain.SubmitQueryRequest;
import uk.gov.moj.sdt.domain.api.IBulkCustomer;
import uk.gov.moj.sdt.domain.api.ISubmitQueryRequest;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.HeaderType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryrequestschema.SubmitQueryRequestType;
import uk.gov.moj.sdt.ws._2013.sdt.submitqueryresponseschema.SubmitQueryResponseType;

/**
 * Unit tests for SubmitQueryTransformer.
 * 
 * @author d130680
 * 
 */
public class SubmitQueryTransformerTest extends AbstractSdtUnitTestBase
{
    /**
     * Submit query transformer.
     */
    private SubmitQueryTransformer transformer;

    /**
     * Set up variables for the test.
     */
    public void setUpLocalTests ()
    {
        Constructor<SubmitQueryTransformer> c;
        try
        {
            // Make the constructor visible so we can get a new instance of it.
            c = SubmitQueryTransformer.class.getDeclaredConstructor ();
            c.setAccessible (true);
            transformer = c.newInstance ();
        }
        catch (final Exception e)
        {
            e.printStackTrace ();
        }

    }

    /**
     * Test the transformation from jaxb to domain object.
     */
    @Test
    public void testTransformJaxbToDomain ()
    {

        // Set up the jaxb object to transform
        final SubmitQueryRequestType jaxb = new SubmitQueryRequestType ();
        final long sdtCustomerId = 123;
        final String targetApplicationId = "mcol";

        // Set the header
        final HeaderType header = new HeaderType ();
        header.setSdtCustomerId (sdtCustomerId);
        header.setTargetApplicationId (targetApplicationId);
        jaxb.setHeader (header);

        // Call the transformer
        final ISubmitQueryRequest domain = transformer.transformJaxbToDomain (jaxb);

        // Test the jaxb object has been transformed domain object
        Assert.assertEquals ("SDT Customer ID does not match", sdtCustomerId, domain.getBulkCustomer ()
                .getSdtCustomerId ());
        Assert.assertEquals ("Target Application ID does not match", targetApplicationId, domain
                .getTargetApplication ().getTargetApplicationCode ());

    }

    /**
     * Test the from transformation domain to jaxb object.
     */
    @Test
    public void testTransformDomainToJaxb ()
    {
        // Set up the domain object to transform
        final int sdtCustomerId = 123;
        final int resultCount = 15;

        // Create the domain object
        final ISubmitQueryRequest domain = new SubmitQueryRequest ();

        final IBulkCustomer bulkCustomer = new BulkCustomer ();
        bulkCustomer.setSdtCustomerId (sdtCustomerId);

        domain.setBulkCustomer (bulkCustomer);
        domain.setResultCount (resultCount);

        // Call the transformer
        final SubmitQueryResponseType jaxb = transformer.transformDomainToJaxb (domain);

        // Test the domain object has been transformed to a jaxb object
        Assert.assertEquals ("SDT Customer ID does not match", sdtCustomerId, jaxb.getSdtCustomerId ());
        Assert.assertEquals ("Result count does not match", resultCount, jaxb.getResultCount ().longValue ());
        Assert.assertEquals ("SDT Service does not match", AbstractTransformer.SDT_SERVICE, jaxb.getSdtService ());
        Assert.assertNotNull ("ResultsType should not be null", jaxb.getResults ());
    }
}
