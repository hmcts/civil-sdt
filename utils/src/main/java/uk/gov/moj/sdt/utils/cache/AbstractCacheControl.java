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
package uk.gov.moj.sdt.utils.cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.utils.cache.api.ICacheable;

/**
 * An abstract class to provide a cache/uncache facility to classes that cache data from the database.
 * 
 * @author Robin Compston
 * 
 */

public abstract class AbstractCacheControl implements ICacheable
{
    /**
     * Static logging object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (AbstractCacheControl.class);

    /**
     * Value which determines the current value of a flag which controls whether individual {@link AbstractCacheControl}
     * instances need to be uncached.
     */
    private static int cacheResetControl;

    /**
     * The cache variable that holds the global parameters as a key-value pair for this singleton.
     */
    private Map<String, String> cacheMap = new HashMap<String, String>();

    /**
     * Value of the cache count for this {@link AbstractCacheControl} instance need to be uncached.
     */
    private int localCacheResetControl;

    /**
     * Get the map holding cached values.
     * 
     * @return the map holding cached values.
     */
    protected Map<String, String> getCacheMap ()
    {
        return cacheMap;
    }

    /**
     * Load the cache with appropriate source data for this cache object. Each implementing class must load its own
     * specific data. Note that the map itself is an instance variable and will already exist.
     */
    protected abstract void loadCache ();

    /**
     * Clears the cache so that fresh values can be loaded from source.
     */
    protected void resetCache ()
    {
        synchronized (this.getCacheMap ())
        {
            // Clear map but do not destroy it. 
            this.getCacheMap ().clear ();
        }
    }

    /**
     * Check whether an uncache is required.
     * 
     * @return true - uncache required, false - uncache not required.
     */
    protected boolean uncacheRequired ()
    {
        if (this.localCacheResetControl < AbstractCacheControl.cacheResetControl)
        {
            LOG.debug ("Uncaching " + this.getClass ().getCanonicalName ());

            // Bring current value up to date and retport uncache required.
            this.localCacheResetControl = AbstractCacheControl.cacheResetControl;

            return true;
        }

        return false;
    }

    /**
     * Mark all cache classes extending {@link AbstractCacheControl} as needing to uncache any cached content. It is the
     * responsibility of the implementation to notice this has been called and discard any cached items, forcing a
     * refresh from source. This is currently called from an MBean.
     */
    public static void setUncache ()
    {
        LOG.debug ("Uncached requested on all classes implmenting ICacheable.");

        // Increment global flag to tell all instances they need to uncache.
        AbstractCacheControl.cacheResetControl++;

        return;
    }
}
