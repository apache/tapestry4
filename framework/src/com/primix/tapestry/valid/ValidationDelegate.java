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

import com.primix.tapestry.IRender;
import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IResponseWriter;
import com.primix.tapestry.RequestCycleException;
import com.primix.tapestry.form.IFormComponent;
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

public class ValidationDelegate implements IValidationDelegate, IPoolable
{
	private IFormComponent currentComponent;
	private List trackings;
	private Map trackingMap;

	public void clear()
	{
		currentComponent = null;
		trackings = null;
		trackingMap = null;
	}

	public void resetForPool()
	{
		currentComponent = null;

		if (trackings != null)
			trackings.clear();

		if (trackingMap != null)
			trackingMap.clear();
	}

	private boolean inError(IFormComponent component)
	{
		if (trackingMap == null)
			return false;

		String fieldName = component.getName();

		return trackingMap.containsKey(fieldName);
	}

	/**
	 *  If the form component is in error, places a &lt;font color="red"&lt; around it.
	 *  Note: this will only work on the render phase after a rewind, and will be
	 *  confused if components are inside any kind of loop.
	 **/

	public void writeLabelPrefix(
		IFormComponent component,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (inError(component))
		{
			writer.begin("font");
			writer.attribute("color", "red");
		}
	}

	/**
	 *  Closes the &lt;font&gt; element,started by
	 *  {@link #writeLabelPrefix(IFormComponent,IResponseWriter,IRequestCycle)},
	 *  if the form component is in error.
	 *
	 */

	public void writeLabelSuffix(
		IFormComponent component,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (inError(component))
		{
			writer.end();
		}
	}

	/**
	 *  Returns the {@link IFieldTracking} for the current component, if any.
	 *  The {@link IFieldTracking} is created in {@link #record(IRender, ValidationConstraint, String)}
	 *  when an error is recorded for the component.
	 * 
	 *  <p>Components may be rendered multiple times, with multiple names (provided
	 *  by the {@link Form}, care must be taken that this method is invoked
	 *  <em>after</em> the Form has provided a unique name for the component.
	 * 
	 *  @see #setFormComponent(IFormComponent)
	 * 
	 *  @returns the {@link IFieldTracking}, or null if the field has no tracking
	 *  (is not in error).
	 * 
	 **/
	
	protected IFieldTracking getComponentTracking()
	{
	 	if ( trackingMap == null)
	 		return null;  
	 		
	 	return (IFieldTracking)trackingMap.get(currentComponent.getName());
	}

	public void setFormComponent(IFormComponent component)
	{
		currentComponent = component;
	}

	public boolean isInError()
	{
		return getComponentTracking() != null;
	}

	public String getInvalidInput()
	{
		return getComponentTracking().getInvalidInput();
	}

	/**
	 *  Returns all the field trackings.
	 * 
	 **/

	public List getFieldTracking()
	{
		if (trackings == null)
			return null;

		return Collections.unmodifiableList(trackings);
	}

	public void reset()
	{
	    IFieldTracking tracking = getComponentTracking();
	    
		if (tracking != null)
		{
			trackings.remove(tracking);
			trackingMap.remove(tracking.getFieldName());
		}
	}

	/**
	 *  Invokes {@link #record(String, ValidationConstraint, String)}.
	 * 
	 **/
	
	public void record(ValidatorException ex)
	{
		record(ex.getMessage(), ex.getConstraint(), ex.getInvalidInput());
	}

	/**
	 *  Invokes {@link #record(IRender, ValidationConstraint, String)}, after
	 *  wrapping the message parameter in a
	 *  {@link RenderString}.
	 * 
	 **/
	
	public void record(
		String message,
		ValidationConstraint constraint,
		String invalidInput)
	{
		record(new RenderString(message), constraint, invalidInput);
	}

	/**
	 *  Records error information about the currently selected component,
	 *  or records unassociated (with any field) errors.
	 * 
	 *  <p>
	 *  Currently, you may have at most one error per <em>field</em>
	 *  (not difference between field and component), but any number of
	 *  unassociated errors.
	 * 
	 *  <p>
	 *  Subclasses may override the default error message (based on other
	 *  factors, such as the field and constraint) before invoking this
	 *  implementation.
	 * 
	 *  @since 1.0.9
	 **/

	public void record(
		IRender errorRenderer,
		ValidationConstraint constraint,
		String invalidInput)
	{
		IFieldTracking tracking = null;
		
		if (trackings == null)
			trackings = new ArrayList();

		if (trackingMap == null)
			trackingMap = new HashMap();

		if (currentComponent == null)
		{
			tracking = new FieldTracking();
	
			// Add it to the *ahem* field trackings, but not to the
			// map.

			trackings.add(tracking);
		}
	else
	{
		tracking = getComponentTracking();
		
		if (tracking == null)
		{
			String fieldName = currentComponent.getName();

			tracking = new FieldTracking(fieldName, currentComponent);

			trackings.add(tracking);
			trackingMap.put(fieldName, tracking);
		}
	}

		// Note that recording two errors for the same field is not advised; the
		// second will override the first.

		tracking.setInvalidInput(invalidInput);
		tracking.setRenderer(errorRenderer);
		tracking.setConstraint(constraint);
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
		if (isInError())
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
	 *  <p>As of release 1.0.9, this returns an instance of {@link IRender}, not a {@link String}.
	 * 
	 **/

	public IRender getFirstError()
	{
		if (trackings == null)
			return null;

		if (trackings.size() == 0)
			return null;

		IFieldTracking tracking = (IFieldTracking) trackings.get(0);

		return tracking.getRenderer();
	}

	/**
	 *  Checks to see if the field is in error.  This will <em>not</em> work properly
	 *  in a loop, but is only used by {@link FieldLabel}.  Therefore, using {@link FieldLabel}
	 *  in a loop (where the {@link IFormComponent} is renderred more than once) will not provide
	 *  correct results.
	 * 
	 **/

	protected boolean isInError(IFormComponent component)
	{
		if (trackingMap == null)
			return false;

		return trackingMap.containsKey(component.getName());
	}

	/**
	 *  Returns a {@link List} of {@link IFieldTrackings}.  This is the master list
	 *  of trackings, except that it omits and trackings that are not associated
	 *  with a particular field.  May return an empty list, or null.
	 * 
	 *  <p>Order is not determined, though it is likely the order in which components
	 *  are laid out on in the template (this is subject to change).
	 * 
	 **/

	public List getAssociatedTrackings()
	{
		int count = (trackings == null) ? 0 : trackings.size();

		if (count == 0)
			return null;

		List result = new ArrayList(count);

		for (int i = 0; i < count; i++)
		{
			IFieldTracking tracking = (IFieldTracking) trackings.get(i);

			if (tracking.getFieldName() == null)
				continue;

			result.add(tracking);
		}

		return result;
	}

	/**
	 *  Like {@link #getAssociatedTrackings()}, but returns only the unassociated trackings.
	 *  Unassociated trackings are new (in release 1.0.9), and are why
	 *  interface {@link IFieldTracking} is not very well named.
	 * 
	 *  <p>The trackings are returned in an unspecified order, which (for the moment, anyway)
	 *  is the order in which they were added (this could change in the future, or become
	 *  more concrete).
	 * 
	 **/

	public List getUnassociatedTrackings()
	{
		int count = (trackings == null) ? 0 : trackings.size();

		if (count == 0)
			return null;

		List result = new ArrayList(count);

		for (int i = 0; i < count; i++)
		{
			IFieldTracking tracking = (IFieldTracking) trackings.get(i);

			if (tracking.getFieldName() != null)
				continue;

			result.add(tracking);
		}

		return result;
	}

}