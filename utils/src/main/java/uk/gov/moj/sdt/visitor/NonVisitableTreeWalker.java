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
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */
package uk.gov.moj.sdt.visitor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.moj.sdt.utils.visitor.api.IVisitor;

/**
 * Implementation of NonVisitableTreeWalker.
 * 
 * @author Robin Compston
 * 
 */
public final class NonVisitableTreeWalker
{
    /**
     * Static logging object.
     */
    private static final Logger LOG = LoggerFactory.getLogger (NonVisitableTreeWalker.class);

    /**
     * Collection of all objects in tree which have already been processed..
     */
    private Set<Object> alreadyProcessed = new HashSet<Object> ();

    /**
     * Default private constructor for utility class.
     */
    private NonVisitableTreeWalker ()
    {
    }

    /**
     * Walk a list containing objects applying the corresponding {@link IVisitor} to each of them.
     * 
     * @param target the list to walk.
     * @param visitorSuffix the suffix which by convention is appended to the target class name in order to form the
     *            class name of the visitor class.
     */
    private void walkList (final List<?> target, final String visitorSuffix)
    {
        // Iterate over list and walk its elements.
        for (int i = 0; i < target.size (); i++)
        {
            this.walkTree (target.get (i), visitorSuffix);
        }
    }

    /**
     * Walk a map containing objects applying the corresponding {@link IVisitor} to each of them.
     * 
     * @param target the map to walk.
     * @param visitorSuffix the suffix which by convention is appended to the target class name in order to form the
     *            class name of the visitor class.
     */
    private void walkMap (final Map<?, ?> target, final String visitorSuffix)
    {
        // Iterate over map and walk its elements.
        final Set<?> keys = target.keySet ();
        for (final Iterator<?> iter = keys.iterator (); iter.hasNext ();)
        {
            final Object key = iter.next ();
            this.walkTree (target.get (key), visitorSuffix);
        }
    }

    /**
     * Walk a set containing objects applying the corresponding {@link IVisitor} to each of them.
     * 
     * @param target the set to walk.
     * @param visitorSuffix the suffix which by convention is appended to the target class name in order to form the
     *            class name of the visitor class.
     */
    private void walkSet (final Set<?> target, final String visitorSuffix)
    {
        // Iterate over map and walk its elements.
        for (final Iterator<?> iter = target.iterator (); iter.hasNext ();)
        {
            final Object object = iter.next ();
            this.walkTree (object, visitorSuffix);
        }
    }

    /**
     * Walk a tree containing objects applying the corresponding {@link IVisitor} to each of them.
     * 
     * @param <ObjectType> of object to walk.
     * @param target the top level object of the object tree to walk.
     * @param visitorSuffix the suffix which by convention is appended to the target class name in order to form the
     *            class name of the visitor class.
     */
    public <ObjectType extends Object> void walkTree (final ObjectType target, final String visitorSuffix)
    {
        // Get fully qualified class name of target.
        final String targetClassName = target.getClass ().getSimpleName ();

        // Add suffix to form visitor class name.
        final String visitorClassName = targetClassName + visitorSuffix;

        // Get IVisitor registered when visitor object instantiated.
        final IVisitor visitor = AbstractVisitor.getVisitor (visitorClassName);

        // Only apply visitor to target if a visitor exists for the type of the target class.
        if (visitor == null)
        {
            // Do not go on if this target does not have a visitor (namedd according to cenvention.
            return;
        }

        // Call the target with this visitor to execute the pattern.
        visitor.visit (target);

        // Recursively call all nested beans.
        Method method = null;
        try
        {
            final BeanInfo beanInfo = Introspector.getBeanInfo (target.getClass ());
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors ();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
            {
                method = propertyDescriptor.getReadMethod ();

                // If no read method, ignore.
                if (method == null)
                {
                    continue;
                }

                final Object nestedObject = method.invoke (target, (Object[]) null);

                // Check if this object has already been seen - if so ignore it.
                if (alreadyProcessed.contains (nestedObject))
                {
                    continue;
                }

                // Record this object as now processed.
                alreadyProcessed.add (nestedObject);

                if (nestedObject instanceof List<?>)
                {
                    this.walkList ((List<?>) nestedObject, visitorSuffix);
                }
                else if (nestedObject instanceof Map<?, ?>)
                {
                    this.walkMap ((Map<?, ?>) nestedObject, visitorSuffix);
                }
                else if (nestedObject instanceof Set<?>)
                {
                    this.walkSet ((Set<?>) nestedObject, visitorSuffix);
                }
                else if (nestedObject != null)
                {
                    this.walkTree (nestedObject, visitorSuffix);
                }
            }
        }
        catch (final IntrospectionException e)
        {
            LOG.error ("Failure to get bean info from " + target.getClass ().getName (), e);
            throw new UnsupportedOperationException ("Failure to get bean info from " + target.getClass ().getName (),
                    e);
        }
        catch (final InvocationTargetException e)
        {
            LOG.error ("Failure to INvoke method " + method.getName (), e);
            throw new UnsupportedOperationException ("Failure to get bean info from " + target.getClass ().getName (),
                    e);
        }
        catch (final IllegalAccessException e)
        {
            LOG.error ("Failure to get bean info from " + target.getClass ().getName (), e);
            throw new UnsupportedOperationException ("Failure to get bean info from " + target.getClass ().getName (),
                    e);
        }
    }

    /**
     * Walk a tree containing objects applying the corresponding {@link IVisitor} to each of them.
     * 
     * @param <ObjectType> of object to walk.
     * @param target the top level object of the object tree to walk.
     * @param visitorSuffix the suffix which by convention is appended to the target class name in order to form the
     *            class name of the visitor class.
     */
    public static <ObjectType extends Object> void walk (final ObjectType target, final String visitorSuffix)
    {
        // Create instance for this thread and call it.
        final NonVisitableTreeWalker nonVisitableTreeWalker = new NonVisitableTreeWalker ();
        nonVisitableTreeWalker.walkTree (target, visitorSuffix);
    }
}