/* Copyrights and Licenses
 * 
 * Copyright (c) 2012-2014 by the Ministry of Justice. All rights reserved.
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
package uk.gov.moj.sdt.utils.transaction.synchronizer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import uk.gov.moj.sdt.utils.SdtContext;
import uk.gov.moj.sdt.utils.transaction.synchronizer.api.IMessageSynchronizer;

/**
 * Implementation of the IMessageSynchronizer interface.
 * 
 * @author Manoj Kulkarni
 * 
 */
public class MessageSynchronizer extends TransactionSynchronizationAdapter implements IMessageSynchronizer
{

    /**
     * Logger for this class.
     */
    private static final Log LOGGER = LogFactory.getLog (MessageSynchronizer.class);

    @Override
    public void execute (final Runnable command)
    {
        LOGGER.debug ("Submitting new command");
        if ( !TransactionSynchronizationManager.isSynchronizationActive ())
        {
            LOGGER.info ("Transaction synchronization is NOT ACTIVE. Executing command [" + command + "] right now");
            command.run ();
            return;
        }

        if (SdtContext.getContext ().addSynchronisationTask (command))
        {
            TransactionSynchronizationManager.registerSynchronization (this);
        }

    }

    @Override
    public void afterCommit ()
    {
        final List<Runnable> synchronisationTasks = SdtContext.getContext ().getSynchronisationTasks ();
        if (LOGGER.isDebugEnabled ())
        {
            LOGGER.debug ("Transaction successfully committed, so executing " + synchronisationTasks.size () +
                    " tasks in the list");
        }
        for (Runnable runnableTask : synchronisationTasks)
        {
            LOGGER.debug ("Executing task " + runnableTask);
            runnableTask.run ();
        }
    }

    @Override
    public void afterCompletion (final int status)
    {
        LOGGER.debug ("Transaction completed with status " + (status == STATUS_COMMITTED ? "Committed" : "Rollback"));
        SdtContext.getContext ().clearSynchronisationTasks ();
    }

    @Override
    public void synchronizeTask (final Runnable task)
    {
        // Delegate processing to the execute method.
        this.execute (task);

    }

}
