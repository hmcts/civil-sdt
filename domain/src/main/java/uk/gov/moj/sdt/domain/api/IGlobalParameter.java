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

package uk.gov.moj.sdt.domain.api;

/**
 * Interface for all classes implementing {@link IGlobalParameter}.
 * 
 * @author Manoj Kulkarni.
 * 
 */
public interface IGlobalParameter
{

    /**
     * Get the parameter name.
     * 
     * @return parameter name
     */
    String getName ();

    /**
     * Set the parameter name.
     * 
     * @param name parameter name
     */
    void setName (final String name);

    /**
     * Get the parameter value.
     * 
     * @return parameter value
     */
    String getValue ();

    /**
     * Set the parameter value.
     * 
     * @param value parameter value
     */
    void setValue (final String value);

    /**
     * Get the parameter description.
     * 
     * @return parameter description
     */
    String getDescription ();

    /**
     * Set the parameter description.
     * 
     * @param description parameter description
     */
    void setDescription (final String description);

}