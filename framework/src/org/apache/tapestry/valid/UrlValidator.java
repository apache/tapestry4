/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.valid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;

public class UrlValidator extends BaseValidator {
	private int _minimumLength;
	private String _minimumLengthMessage;
	private String _invalidUrlFormatMessage;
	private String _disallowedProtocolMessage;
	private Collection _allowedProtocols;

	private String _scriptPath = "/org/apache/tapestry/valid/UrlValidator.script"; //$NON-NLS-1$

	public UrlValidator() {
	}

	private UrlValidator(boolean required) {
		super(required);
	}

	public String toString(IFormComponent field, Object value) {
		if (value == null)
			return null;

		return value.toString();
	}

	public Object toObject(IFormComponent field, String input)
		throws ValidatorException {
		if (checkRequired(field, input))
			return null;

		if (_minimumLength > 0 && input.length() < _minimumLength)
			throw new ValidatorException(
				buildMinimumLengthMessage(field),
				ValidationConstraint.MINIMUM_WIDTH);

		if (!isValidUrl(input))
			throw new ValidatorException(
				buildInvalidUrlFormatMessage(field),
				ValidationConstraint.URL_FORMAT);

		if (!isAllowedProtocol(input)) {
			throw new ValidatorException(
				buildDisallowedProtocolMessage(field),
				ValidationConstraint.DISALLOWED_PROTOCOL);
		}

		return input;
	}

	public int getMinimumLength() {
		return _minimumLength;
	}

	public void setMinimumLength(int minimumLength) {
		_minimumLength = minimumLength;
	}

	public void renderValidatorContribution(
		IFormComponent field,
		IMarkupWriter writer,
		IRequestCycle cycle) {
		if (!isClientScriptingEnabled())
			return;

		Map symbols = new HashMap();

		if (isRequired())
			symbols.put("requiredMessage", buildRequiredMessage(field)); //$NON-NLS-1$

		if (_minimumLength > 0)
			symbols.put("minimumLengthMessage", //$NON-NLS-1$
			buildMinimumLengthMessage(field));

		symbols.put("urlFormatMessage", buildInvalidUrlFormatMessage(field)); //$NON-NLS-1$

		symbols.put("urlDisallowedProtocolMessage", //$NON-NLS-1$
		buildDisallowedProtocolMessage(field));

		symbols.put("urlRegexpProtocols", buildUrlRegexpProtocols()); //$NON-NLS-1$

		processValidatorScript(_scriptPath, cycle, field, symbols);
	}

	private String buildUrlRegexpProtocols() {
		if(_allowedProtocols == null) {
			return null;
		}
		String regexp = "/("; //$NON-NLS-1$
		Iterator iter = _allowedProtocols.iterator();
		while (iter.hasNext()) {
			String protocol = (String) iter.next();
			regexp += protocol;
			if (iter.hasNext()) {
				regexp += "|"; //$NON-NLS-1$
			}
		}
		regexp += "):///"; //$NON-NLS-1$
		return regexp;
	}

	public String getScriptPath() {
		return _scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		_scriptPath = scriptPath;
	}

	protected boolean isValidUrl(String url) {
		boolean bIsValid;
		try {
			URL oUrl = new URL(url);
			bIsValid = true;
		} catch (MalformedURLException mue) {
			bIsValid = false;
		}
		return bIsValid;
	}

	protected boolean isAllowedProtocol(String url) {
		boolean bIsAllowed = false;
		if (_allowedProtocols != null) {
			URL oUrl;
			try {
				oUrl = new URL(url);
			} catch (MalformedURLException e) {
				return false;
			}
			String actualProtocol = oUrl.getProtocol();
			Iterator iter = _allowedProtocols.iterator();
			while (iter.hasNext()) {
				String protocol = (String) iter.next();
				if (protocol.equals(actualProtocol)) {
					bIsAllowed = true;
					break;
				}
			}
		} else {
			bIsAllowed = true;
		}
		return bIsAllowed;
	}

	public String getInvalidUrlFormatMessage() {
		return _invalidUrlFormatMessage;
	}

	public String getMinimumLengthMessage() {
		return _minimumLengthMessage;
	}

	public void setInvalidUrlFormatMessage(String string) {
		_invalidUrlFormatMessage = string;
	}

	public String getDisallowedProtocolMessage() {
		return _disallowedProtocolMessage;
	}

	public void setDisallowedProtocolMessage(String string) {
		_disallowedProtocolMessage = string;
	}

	public void setMinimumLengthMessage(String string) {
		_minimumLengthMessage = string;
	}

	protected String buildMinimumLengthMessage(IFormComponent field) {
			String pattern = getPattern(_minimumLengthMessage, "field-too-short", //$NON-NLS-1$
	field.getPage().getLocale());

		return formatString(
			pattern,
			Integer.toString(_minimumLength),
			field.getDisplayName());
	}

	protected String buildInvalidUrlFormatMessage(IFormComponent field) {
			String pattern = getPattern(_invalidUrlFormatMessage, "invalid-url-format", //$NON-NLS-1$
	field.getPage().getLocale());

		return formatString(pattern, field.getDisplayName());
	}

	protected String buildDisallowedProtocolMessage(IFormComponent field) {
		if(_allowedProtocols == null) {
			return null;
		}
			String pattern = getPattern(_disallowedProtocolMessage, "disallowed-protocol", //$NON-NLS-1$
	field.getPage().getLocale());

		String allowedProtocols = ""; //$NON-NLS-1$
		Iterator iter = _allowedProtocols.iterator();
		while (iter.hasNext()) {
			String protocol = (String) iter.next();
			if (!allowedProtocols.equals("")) { //$NON-NLS-1$
				if(iter.hasNext()) {
					allowedProtocols += ", "; //$NON-NLS-1$
				} else {
					allowedProtocols += " or "; //$NON-NLS-1$
				}
			}
			allowedProtocols += protocol;			
		}

		return formatString(pattern, allowedProtocols);
	}

	protected String getPattern(String override, String key, Locale locale) {
		if (override != null)
			return override;

		ResourceBundle strings;
		String string;
		try {
				strings = ResourceBundle.getBundle("net.sf.cendil.tapestry.valid.ValidationStrings", //$NON-NLS-1$
	locale);
			string = strings.getString(key);
		} catch (Exception exc) {
				strings = ResourceBundle.getBundle("org.apache.tapestry.valid.ValidationStrings", //$NON-NLS-1$
	locale);
			string = strings.getString(key);
		}

		return string;
	}

	/**
	 * @param collection
	 */
	public void setAllowedProtocols(String protocols) {
		String[] aProtocols = protocols.split(","); //$NON-NLS-1$
		_allowedProtocols = new Vector();
		for (int i = 0; i < aProtocols.length; i++) {
			_allowedProtocols.add(aProtocols[i]);
		}
	}

}