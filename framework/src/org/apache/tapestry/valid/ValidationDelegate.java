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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IFormComponent;

/**
 *  A base implementation of {@link IValidationDelegate} that can be used
 *  as a helper bean.  This class is often subclassed, typically to override presentation
 *  details.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public class ValidationDelegate implements IValidationDelegate
{
    private IFormComponent _currentComponent;
    private List _trackings;

    /**
     *  A Map of Maps, keyed on the name of the Form.  Each inner map contains
     *  the trackings for one form, keyed on component name.  Care must
     *  be taken, because the inner Map is not always present.
     * 
     **/

    private Map _trackingMap;

    public void clear()
    {
        _currentComponent = null;
        _trackings = null;
        _trackingMap = null;
    }

    /**
     *  If the form component is in error, places a &lt;font color="red"&lt; around it.
     *  Note: this will only work on the render phase after a rewind, and will be
     *  confused if components are inside any kind of loop.
     **/

    public void writeLabelPrefix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError(component))
        {
            writer.begin("font");
            writer.attribute("color", "red");
        }
    }

    /**
     *  Closes the &lt;font&gt; element,started by
     *  {@link #writeLabelPrefix(IFormComponent,IMarkupWriter,IRequestCycle)},
     *  if the form component is in error.
     *
     **/

    public void writeLabelSuffix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        if (isInError(component))
        {
            writer.end();
        }
    }

    /**
     *  Returns the {@link IFieldTracking} for the current component, if any.
     *  The {@link IFieldTracking} is usually created in 
     *  {@link #recordInput(String)} or, failing that,
     *  in {@link #record(IRender, ValidationConstraint)}.
     * 
     *  <p>Components may be rendered multiple times, with multiple names (provided
     *  by the {@link org.apache.tapestry.form.Form}, care must be taken that this method is invoked
     *  <em>after</em> the Form has provided a unique 
     *  {@link IFormComponent#getName()} for the component.
     * 
     *  @see #setFormComponent(IFormComponent)
     * 
     *  @return the {@link FieldTracking}, or null if the field has no tracking.
     * 
     **/

    protected FieldTracking getComponentTracking()
    {
        if (_trackingMap == null)
            return null;

        String formName = _currentComponent.getForm().getName();

        Map formMap = (Map) _trackingMap.get(formName);

        if (formMap == null)
            return null;

        return (FieldTracking) formMap.get(_currentComponent.getName());
    }

    public void setFormComponent(IFormComponent component)
    {
        _currentComponent = component;
    }

    public boolean isInError()
    {
        IFieldTracking tracking = getComponentTracking();

        return tracking != null && tracking.isInError();
    }

    public String getFieldInputValue()
    {
        IFieldTracking tracking = getComponentTracking();

        return tracking == null ? null : tracking.getInput();
    }

    /**
     *  Returns all the field trackings as an unmodifiable List.
     * 
     **/

    public List getFieldTracking()
    {
        if (Tapestry.size(_trackings) == 0)
            return null;

        return Collections.unmodifiableList(_trackings);
    }

    public void reset()
    {
        IFieldTracking tracking = getComponentTracking();

        if (tracking != null)
        {
            _trackings.remove(tracking);

            String formName = tracking.getComponent().getForm().getName();

            Map formMap = (Map) _trackingMap.get(formName);

            if (formMap != null)
                formMap.remove(tracking.getFieldName());
        }
    }

    /**
     *  Invokes {@link #record(String, ValidationConstraint)}, or
     *  {@link #record(IRender, ValidationConstraint)} if the 
     *  {@link ValidatorException#getErrorRenderer() error renderer property}
     *  is not null.
     * 
     **/

    public void record(ValidatorException ex)
    {
        IRender errorRenderer = ex.getErrorRenderer();

        if (errorRenderer == null)
            record(ex.getMessage(), ex.getConstraint());
        else
            record(errorRenderer, ex.getConstraint());
    }

    /**
     *  Invokes {@link #record(IRender, ValidationConstraint)}, after
     *  wrapping the message parameter in a
     *  {@link RenderString}.
     * 
     **/

    public void record(String message, ValidationConstraint constraint)
    {
        record(new RenderString(message), constraint);
    }

    /**
     *  Records error information about the currently selected component,
     *  or records unassociated (with any field) errors.
     * 
     *  <p>
     *  Currently, you may have at most one error per <em>field</em>
     *  (note the difference between field and component), but any number of
     *  unassociated errors.
     * 
     *  <p>
     *  Subclasses may override the default error message (based on other
     *  factors, such as the field and constraint) before invoking this
     *  implementation.
     * 
     *  @since 1.0.9
     **/

    public void record(IRender errorRenderer, ValidationConstraint constraint)
    {
        FieldTracking tracking = findCurrentTracking();

        // Note that recording two errors for the same field is not advised; the
        // second will override the first.

        tracking.setErrorRenderer(errorRenderer);
        tracking.setConstraint(constraint);
    }

    public void recordFieldInputValue(String input)
    {
        FieldTracking tracking = findCurrentTracking();

        tracking.setInput(input);
    }

    /**
     *  Finds or creates the field tracking for the
     *  {@link #setFormComponent(IFormComponent) current component.
     *  If no current component, an unassociated error is created
     *  and returned.
     * 
     *  @since 2.4
     * 
     **/

    protected FieldTracking findCurrentTracking()
    {
        FieldTracking result = null;

        if (_trackings == null)
            _trackings = new ArrayList();

        if (_trackingMap == null)
            _trackingMap = new HashMap();

        if (_currentComponent == null)
        {
            result = new FieldTracking();

            // Add it to the field trackings, but not to the
            // map.

            _trackings.add(result);
        }
        else
        {
            result = getComponentTracking();

            if (result == null)
            {
                String formName = _currentComponent.getForm().getName();

                Map formMap = (Map) _trackingMap.get(formName);

                if (formMap == null)
                {
                    formMap = new HashMap();
                    _trackingMap.put(formName, formMap);
                }

                String fieldName = _currentComponent.getName();

                result = new FieldTracking(fieldName, _currentComponent);

                _trackings.add(result);
                formMap.put(fieldName, result);
            }
        }

        return result;
    }

    /**
     *  Does nothing.  Override in a subclass to decoreate
     *  fields.
     * 
     **/

    public void writePrefix(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
        throws RequestCycleException
    {
    }

    /**
     *  Does nothing.  Override in a subclass to decorate fields.
     * 
     **/

    public void writeAttributes(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
        throws RequestCycleException
    {
    }

    /**
     *  Default implementation; if the current field is in error,
     *  then a suffix is written.  The suffix is:
     *  <code>&amp;nbsp;&lt;font color="red"&gt;**&lt;/font&gt;</code>.
     * 
     **/

    public void writeSuffix(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
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
        return getFirstError() != null;
    }

    /**
     *  A convienience, as most pages just show the first error on the page.
     * 
     *  <p>As of release 1.0.9, this returns an instance of {@link IRender}, not a {@link String}.
     * 
     **/

    public IRender getFirstError()
    {
        if (Tapestry.size(_trackings) == 0)
            return null;

        Iterator i = _trackings.iterator();

        while (i.hasNext())
        {
            IFieldTracking tracking = (IFieldTracking) i.next();

            if (tracking.isInError())
                return tracking.getErrorRenderer();
        }

        return null;
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
        if (_trackingMap == null)
            return false;

        String formName = component.getForm().getName();
        Map formMap = (Map) _trackingMap.get(formName);

        if (formMap == null)
            return false;

        IFieldTracking tracking = (IFieldTracking) formMap.get(component.getName());

        return tracking != null && tracking.isInError();
    }

    /**
     *  Returns a {@link List} of {@link IFieldTracking}s.  This is the master list
     *  of trackings, except that it omits and trackings that are not associated
     *  with a particular field.  May return an empty list, or null.
     * 
     *  <p>Order is not determined, though it is likely the order in which components
     *  are laid out on in the template (this is subject to change).
     * 
     **/

    public List getAssociatedTrackings()
    {
        int count = Tapestry.size(_trackings);

        if (count == 0)
            return null;

        List result = new ArrayList(count);

        for (int i = 0; i < count; i++)
        {
            IFieldTracking tracking = (IFieldTracking) _trackings.get(i);

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
        int count = Tapestry.size(_trackings);

        if (count == 0)
            return null;

        List result = new ArrayList(count);

        for (int i = 0; i < count; i++)
        {
            IFieldTracking tracking = (IFieldTracking) _trackings.get(i);

            if (tracking.getFieldName() != null)
                continue;

            result.add(tracking);
        }

        return result;
    }
};