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
package uk.gov.moj.sdt.utils.mbeans;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import uk.gov.moj.sdt.utils.mbeans.api.ISdtManagementMBean;

/**
 * A class to manage the SDT application via mbean commands. Implements the following commands:
 * 
 * 1. Uncache all cacheable domain objects and force reload from database.
 * 2. Set message driven bean pool size for individual request JMS queue.
 * 3. Requeue old individual requests which have not yet been sent to target application.
 * 
 * @author Robin Compston
 * 
 */

public final class SdtManagementMBean implements ISdtManagementMBean
{
    /**
     * Static logging object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger (SdtManagementMBean.class);

    /**
     * The current value of a flag which controls whether individual {@link AbstractCacheControl} instances need to be
     * uncached. This number is incremented on each uncache, telling caching objects that their cache is stale and needs
     * to be discarded and reloaded. They look at this value and compare it with their own copy of it each time the
     * cache is accessed.
     */
    private int cacheResetControl;

    /**
     * Map of all message driven bean message listener containers, defining the MDB pool size.
     */
    private Map<String, DefaultMessageListenerContainer> containerMap =
            new HashMap<String, DefaultMessageListenerContainer> ();

    /**
     * Constructor for {@link SdtManagementMBean}. This is called by Spring and should become the bean that all
     * subsequent executes management commands.
     */
    public SdtManagementMBean ()
    {
    }

    @Override
    public int getCacheResetControl ()
    {
        return this.cacheResetControl;
    }

    @Override
    public void setMessageListenerContainer (final DefaultMessageListenerContainer messageListenerContainer)
    {
        containerMap.put (messageListenerContainer.getDestinationName (), messageListenerContainer);
    }

    @Override
    public void uncache ()
    {
        this.cacheResetControl++;
    }

    @Override
    public void setMdbPoolSize (final String queueName, final int poolSize)
    {

    }

    @Override
    public void requeueOldIndividualRequests (final int minimumAgeInMinutes)
    {

    }
}
