/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.form;

import com.primix.tapestry.util.Enum;

/**
 *  Lists different types of JavaScript events that can be associated
 *  with a {@link Form} via {@link Form#addEventHandler(FormEventType, String)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 **/

public class FormEventType extends Enum
{
	/**
	 *  Form event triggered when the form is submitted.  Allows an event handler
	 *  to perform any final changes before the results are posted to the server.
	 *
	 *  <p>The JavaScript method should return <code>true</code> or
	 * <code>false</code>.  If there are multiple event handlers for the form
	 * they will be combined using the binary and operator (<code>&amp;&amp;</code>).
	 *
	 **/

	public static final FormEventType SUBMIT =
		new FormEventType("SUBMIT", "onsubmit");

	/**
	 *  Form event triggered when the form is reset; this allows an event handler
	 *  to deal with any special cases related to resetting.
	 *
	 **/

	public static final FormEventType RESET = new FormEventType("RESET", "onreset");

	private String propertyName;

	private FormEventType(String enumerationId, String propertyName)
	{
		super(enumerationId);

		this.propertyName = propertyName;
	}

	/** 
	 *  Returns the DOM property corresponding to event type.
	 *
	 **/

	public String getPropertyName()
	{
		return propertyName;
	}

	/**
	 *  Returns true if multiple functions should be combined
	 *  with the <code>&amp;&amp;</code> operator.  Otherwise,
	 *  the event handler functions are simply invoked
	 *  sequentially (as a series of JavaScript statements).
	 *
	 **/

	public boolean getCombineWithAnd()
	{
		return this == FormEventType.SUBMIT;
	}
}