/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
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

package com.primix.tapestry.valid;

import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IResponseWriter;
import com.primix.tapestry.RequestCycleException;
import com.primix.tapestry.util.pool.*;
import java.util.*;

/**
 *  A base implementation of {@link IValidationDelegate} that can be used
 *  as a helper bean.  This class is often subclassed, typically to override presentation
 *  details.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 */

public class ValidationDelegate
	implements IValidationDelegate, IPoolable
{
	private IField currentField;
	protected IFieldTracking currentTracking;
	private List trackings;
	private Map trackingMap;

	public void resetForPool()
	{
		currentField = null;
		currentTracking = null;
		
		if (trackings != null)
			trackings.clear();
			
		if (trackingMap != null)
			trackingMap.clear();
	}

	private boolean inError(IField field)
	{
		if (trackingMap == null)
			return false;
			
		String fieldName = field.getName();
		
		return trackingMap.containsKey(fieldName);
	}

	/**
	 *  If the field is in error, places a &lt;font color="read"&lt; around it.
	 *  Note: this will only work on the render phase after a rewind, and will be
	 *  confused if components are inside any kind of loop.
	 **/
	
	public void writeLabelPrefix(
		IField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (inError(field))
		{
			writer.begin("font");
			writer.attribute("color", "red");
		}
	}

	/**
	 *  Closes the &lt;font&gt; element,started by
	 *  {@link #writeLabelPrefix(IField,IResponseWriter,IRequestCycle)},
	 *  if the field is in error.
	 *
	 */

	public void writeLabelSuffix(
		IField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (inError(field))
		{
			writer.end();
		}
	}
	
	public void setField(IField field)
	{
		currentField = field;
		currentTracking = null;
		
		if (trackingMap != null)
			currentTracking = (FieldTracking)trackingMap.get(field.getName());			
	}

	public boolean isInError()
	{
		return currentTracking != null;
	}

	public String getInvalidInput()
	{
		if (currentTracking == null)
			return null;
			
		return currentTracking.getInvalidInput();
	}

	public List getFieldTracking()
	{
		if (trackings == null)
			return null;
			
		return Collections.unmodifiableList(trackings);
	}

	public void reset()
	{
		if (currentTracking != null)
		{
			trackings.remove(currentTracking);
			trackingMap.remove(currentTracking.getFieldName());
		}
	}

	public void record(ValidatorException ex)
	{
		record(ex.getMessage(), ex.getConstraint(), ex.getInvalidInput());
	}


	/**
	 *  Records the information provided as a {@link ValidatorException}.
	 *  Subclasses may override the default error message (based on other
	 *  factors, such as the field and constraint) before invoking this
	 *  implementation.
	 * 
	 **/
	
	protected void record(String errorMessage, ValidationConstraint constraint, String invalidInput)
	{
		if (trackings == null)
			trackings = new ArrayList();
			
		if (trackingMap == null)
			trackingMap = new HashMap();
		
		if (currentTracking == null)
		{
			String fieldName = currentField.getName();
			
			currentTracking = new FieldTracking(fieldName, currentField);
			
			trackings.add(currentTracking);
			trackingMap.put(fieldName, currentTracking);
		}
		
		currentTracking.setInvalidInput(invalidInput);
		currentTracking.setErrorMessage(errorMessage);
		currentTracking.setConstraint(constraint);
	}

	public void writePrefix(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
	}

	public void writeAttributes(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
	}

	public void writeSuffix(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		if (currentTracking != null)
		{
			writer.printRaw("&nbsp;");
			writer.begin("font");
			writer.attribute("color", "red");
			writer.print("**");
			writer.end();
		}
	}
	
	
	public boolean getHasErrors()
	{
		return trackings != null && trackings.size() > 0;
	}
	
	/**
	 *  A convienience, as most pages just show the first error on the page.
	 * 
	 **/
	
	public String getFirstError()
	{
		if (trackings == null)
			return null;
			
		if (trackings.size() == 0)
			return null;
			
		IFieldTracking tracking = (IFieldTracking)trackings.get(0);
		
		return tracking.getErrorMessage();
	}

	/**
	 *  Checks to see if the field is in error.  This will <em>not</em> work properly
	 *  in a loop, but is only used by {@link FieldLabel}.  Therefore, using {@link FieldLabel}
	 *  in a loop (where the {@link IField} is renderred more than once) will not provide
	 *  correct results.
	 * 
	 **/

	protected boolean isInError(IField field)
	{
		if (trackingMap == null)
			return false;
			
		return trackingMap.containsKey(field.getName());
	}
}