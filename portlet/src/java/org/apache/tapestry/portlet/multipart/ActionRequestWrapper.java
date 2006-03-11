// Copyright 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.portlet.multipart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

/**
 * @author Raphael Jean
 *
 */
public class ActionRequestWrapper extends Object implements ActionRequest 
{
	private ActionRequest wrapped;
	
    public ActionRequestWrapper(ActionRequest request)
    {
        wrapped = request;
    }
    
	public InputStream getPortletInputStream() throws IOException {
		return wrapped.getPortletInputStream();
	}

	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		wrapped.setCharacterEncoding(arg0);
	}

	public BufferedReader getReader() throws UnsupportedEncodingException,
			IOException {
		return wrapped.getReader();
	}

	public String getCharacterEncoding() {
		return wrapped.getCharacterEncoding();
	}

	public String getContentType() {
		return wrapped.getContentType();
	}

	public int getContentLength() {
		return wrapped.getContentLength();
	}

	public boolean isWindowStateAllowed(WindowState arg0) {
		return wrapped.isWindowStateAllowed(arg0);
	}

	public boolean isPortletModeAllowed(PortletMode arg0) {
		return wrapped.isPortletModeAllowed(arg0);
	}

	public PortletMode getPortletMode() {
		return wrapped.getPortletMode();
	}

	public WindowState getWindowState() {
		return wrapped.getWindowState();
	}

	public PortletPreferences getPreferences() {
		return wrapped.getPreferences();
	}

	public PortletSession getPortletSession() {
		return wrapped.getPortletSession();
	}

	public PortletSession getPortletSession(boolean arg0) {
		return wrapped.getPortletSession(arg0);
	}

	public String getProperty(String arg0) {
		return wrapped.getProperty(arg0);
	}

	public Enumeration getProperties(String arg0) {
		return wrapped.getProperties(arg0);
	}

	public Enumeration getPropertyNames() {
		return wrapped.getPropertyNames();
	}

	public PortalContext getPortalContext() {
		return wrapped.getPortalContext();
	}

	public String getAuthType() {
		return wrapped.getAuthType();
	}

	public String getContextPath() {
		return wrapped.getContextPath();
	}

	public String getRemoteUser() {
		return wrapped.getRemoteUser();
	}

	public Principal getUserPrincipal() {
		return wrapped.getUserPrincipal();
	}

	public boolean isUserInRole(String arg0) {
		return wrapped.isUserInRole(arg0);
	}

	public Object getAttribute(String arg0) {
		return wrapped.getAttribute(arg0);
	}

	public Enumeration getAttributeNames() {
		return wrapped.getAttributeNames();
	}

	public String getParameter(String arg0) {
		return wrapped.getParameter(arg0);
	}

	public Enumeration getParameterNames() {
		return wrapped.getParameterNames();
	}

	public String[] getParameterValues(String arg0) {
		return wrapped.getParameterValues(arg0);
	}

	public Map getParameterMap() {
		return wrapped.getParameterMap();
	}

	public boolean isSecure() {
		return wrapped.isSecure();
	}

	public void setAttribute(String arg0, Object arg1) {
		wrapped.setAttribute(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
		wrapped.removeAttribute(arg0);
	}

	public String getRequestedSessionId() {
		return wrapped.getRequestedSessionId();
	}

	public boolean isRequestedSessionIdValid() {
		return wrapped.isRequestedSessionIdValid();
	}

	public String getResponseContentType() {
		return wrapped.getResponseContentType();
	}

	public Enumeration getResponseContentTypes() {
		return wrapped.getResponseContentTypes();
	}

	public Locale getLocale() {
		return wrapped.getLocale();
	}

	public Enumeration getLocales() {
		return wrapped.getLocales();
	}

	public String getScheme() {
		return wrapped.getScheme();
	}

	public String getServerName() {
		return wrapped.getServerName();
	}

	public int getServerPort() {
		return wrapped.getServerPort();
	}

	protected ActionRequest getRequest() {
		return wrapped;
	}

}