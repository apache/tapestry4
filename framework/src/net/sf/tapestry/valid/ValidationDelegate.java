package net.sf.tapestry.valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.util.pool.IPoolable;

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

public class ValidationDelegate implements IValidationDelegate, IPoolable
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

    public void resetForPool()
    {
        _currentComponent = null;

        if (_trackings != null)
            _trackings.clear();

        if (_trackingMap != null)
            _trackingMap.clear();
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
     *  The {@link IFieldTracking} is created in {@link #record(IRender, ValidationConstraint, String)}
     *  when an error is recorded for the component.
     * 
     *  <p>Components may be rendered multiple times, with multiple names (provided
     *  by the {@link net.sf.tapestry.form.Form}, care must be taken that this method is invoked
     *  <em>after</em> the Form has provided a unique name for the component.
     * 
     *  @see #setFormComponent(IFormComponent)
     * 
     *  @return the {@link IFieldTracking}, or null if the field has no tracking
     *  (is not in error).
     * 
     **/

    protected IFieldTracking getComponentTracking()
    {
        if (_trackingMap == null)
            return null;
    
        String formName = _currentComponent.getForm().getName();
        
        Map formMap = (Map)_trackingMap.get(formName);
        
        if (formMap == null)
            return null;
        
        return (IFieldTracking) formMap.get(_currentComponent.getName());
    }

    public void setFormComponent(IFormComponent component)
    {
        _currentComponent = component;
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
     *  Returns all the field trackings as an unmodifiable List.
     * 
     **/

    public List getFieldTracking()
    {
        if (_trackings == null)
            return null;

        return Collections.unmodifiableList(_trackings);
    }

    public void reset()
    {
        IFieldTracking tracking = getComponentTracking();

        if (tracking != null)
        {
            _trackings.remove(tracking);
            
            String formName = tracking.getFormComponent().getForm().getName();
            
            Map formMap = (Map)_trackingMap.get(formName);
            
            if (formMap != null)
                formMap.remove(tracking.getFieldName());
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

        if (_trackings == null)
            _trackings = new ArrayList();

        if (_trackingMap == null)
            _trackingMap = new HashMap();

        if (_currentComponent == null)
        {
            tracking = new FieldTracking();

            // Add it to the *ahem* field trackings, but not to the
            // map.

            _trackings.add(tracking);
        }
        else
        {
            tracking = getComponentTracking();

            if (tracking == null)
            {
                String formName = _currentComponent.getForm().getName();
                
                Map formMap = (Map)_trackingMap.get(formName);
                
                if (formMap == null)
                {
                    formMap = new HashMap();
                    _trackingMap.put(formName, formMap);
                }
                
                String fieldName = _currentComponent.getName();

                tracking = new FieldTracking(fieldName, _currentComponent);

                _trackings.add(tracking);
                formMap.put(fieldName, tracking);
            }
        }

        // Note that recording two errors for the same field is not advised; the
        // second will override the first.

        tracking.setInvalidInput(invalidInput);
        tracking.setRenderer(errorRenderer);
        tracking.setConstraint(constraint);
    }

    public void writePrefix(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle)
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
        return _trackings != null && _trackings.size() > 0;
    }

    /**
     *  A convienience, as most pages just show the first error on the page.
     * 
     *  <p>As of release 1.0.9, this returns an instance of {@link IRender}, not a {@link String}.
     * 
     **/

    public IRender getFirstError()
    {
        if (_trackings == null)
            return null;

        if (_trackings.size() == 0)
            return null;

        IFieldTracking tracking = (IFieldTracking) _trackings.get(0);

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
        if (_trackingMap == null)
            return false;

        String formName = component.getForm().getName();
        Map formMap = (Map)_trackingMap.get(formName);
        
        if (formMap == null)
            return false;

        return formMap.containsKey(component.getName());
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
        int count = (_trackings == null) ? 0 : _trackings.size();

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
        int count = (_trackings == null) ? 0 : _trackings.size();

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

}