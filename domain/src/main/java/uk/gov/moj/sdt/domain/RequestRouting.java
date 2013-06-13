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

package uk.gov.moj.sdt.domain;

/**
 * SDT will maintain routing information for target applications based on the Request Type Tag. 
 * 
 * @author d130680
 *
 */
public class RequestRouting {
	
	/**
	 * Target application.
	 */
	private TargetApplication targetApplication;
	/**
	 * Request type.
	 */
	private RequestType requestType;
	/**
	 * Web service endpoint.
	 */
	private String webServiceEndpoint;
	/**
	 * Hibernate version number.
	 */
	private int version;
	
	/**
	 * Get the Target Application.
	 * @return target application
	 */
	public TargetApplication getTargetApplication() {
		return targetApplication;
	}
	
	/**
	 * Set the Target Application.
	 * @param targetApplication target application
	 */
	public void setTargetApplication(final TargetApplication targetApplication) {
		this.targetApplication = targetApplication;
	}
	
	/**
	 * Get the Request Type.
	 * @return request type
	 */
	public RequestType getRequestType() {
		return requestType;
	}
	
	/**
	 * Set the Request Type.
	 * @param requestType request type
	 */
	public void setRequestType(final RequestType requestType) {
		this.requestType = requestType;
	}
	
	/**
	 * Get web service end point.
	 * @return web service end point
	 */
	public String getWebServiceEndpoint() {
		return webServiceEndpoint;
	}
	
	/**
	 * Set web service end point.
	 * @param webServiceEndpoint web service end point
	 */
	public void setWebServiceEndpoint(final String webServiceEndpoint) {
		this.webServiceEndpoint = webServiceEndpoint;
	}
	
	/**
	 * Get Hibernate version id.
	 * @return Hibernate version id
	 */	
	public int getVersion() {
		return version;
	}


}
