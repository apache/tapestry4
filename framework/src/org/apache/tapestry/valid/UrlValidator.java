//  Copyright 2004 The Apache Software Foundation
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
import org.apache.tapestry.util.StringSplitter;

/**
 *
 *  @version $Id$
 *  @since 3.0
 *
 **/
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
			new URL(url);
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
	 * @param protocols comma separated list of allowed protocols
	 */
	public void setAllowedProtocols(String protocols) {
		StringSplitter spliter = new StringSplitter(',');
		//String[] aProtocols = protocols.split(","); //$NON-NLS-1$
		String[] aProtocols = spliter.splitToArray(protocols); //$NON-NLS-1$
		_allowedProtocols = new Vector();
		for (int i = 0; i < aProtocols.length; i++) {
			_allowedProtocols.add(aProtocols[i]);
		}
	}

}