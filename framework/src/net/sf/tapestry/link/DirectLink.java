package net.sf.tapestry.link;

import java.util.List;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  A component for creating a link using the direct service; used for actions that
 *  are not dependant on dynamic page state.
 *
 *  [<a href="../../../../../ComponentReference/DirectLink.html">Component Reference</a>]
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public class DirectLink extends GestureLink implements IDirect
{
    private IBinding _listenerBinding;
    private Object _parameters;
    private IBinding _statefulBinding;

    public void setStatefulBinding(IBinding value)
    {
        _statefulBinding = value;
    }

    public IBinding getStatefulBinding()
    {
        return _statefulBinding;
    }

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.  May be invoked when not renderring.
     *
     **/

    public boolean isStateful()
    {
        if (_statefulBinding == null)
            return true;

        return _statefulBinding.getBoolean();
    }

    /**
     *  Returns {@link IEngineService#DIRECT_SERVICE}.
     *
     **/

    protected String getServiceName()
    {
        return IEngineService.DIRECT_SERVICE;
    }

    protected Object[] getServiceParameters(IRequestCycle cycle)
    {
        return constructServiceParameters(_parameters);
    }

    /**
     *  Converts a service parameters value to an array
     *  of objects.  
     *  This is used by the {@link DirectLink}, {@link ServiceLink}
     *  and {@link ExternalLink}
     *  components.
     *
     *  @param parameterValue the input value which may be
     *  <ul>
     *  <li>null  (returns null)
     *  <li>An array of Object (returns the array)
     *  <li>A {@link List} (returns an array of the values in the List})
     *  <li>A single object (returns the object as a single-element array)
     *  </ul>
     * 
     *  @return An array representation of the input object.
     * 
     *  @since 2.2
     **/

    public static Object[] constructServiceParameters(Object parameterValue)
    {
        if (parameterValue == null)
            return null;

        if (parameterValue instanceof Object[])
            return (Object[]) parameterValue;

        if (parameterValue instanceof List)
        {
            List list = (List) parameterValue;

            return list.toArray();
        }

        return new Object[] { parameterValue };
    }

    /**
     *  Invoked by the direct service to trigger the application-specific
     *  action by notifying the {@link IActionListener listener}.
     *
     *  @throws StaleSessionException if the component is stateful, and
     *  the session is new.
     * 
     **/

    public void trigger(IRequestCycle cycle) throws RequestCycleException
    {
        IActionListener listener = getListener(cycle);

        listener.actionTriggered(this, cycle);
    }

    public IBinding getListenerBinding()
    {
        return _listenerBinding;
    }

    public void setListenerBinding(IBinding value)
    {
        _listenerBinding = value;
    }

    /**
     *  Need to use the listener binding, since this method gets called even when the
     *  component is not rendering.
     * 
     **/

    private IActionListener getListener(IRequestCycle cycle) throws RequestCycleException
    {
        IActionListener result;

        try
        {
            result = (IActionListener) _listenerBinding.getObject("listener", IActionListener.class);

        }
        catch (BindingException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        if (result == null)
            throw new RequiredParameterException(this, "listener", _listenerBinding);

        return result;
    }

    /** @since 2.2 **/

    public Object getParameters()
    {
        return _parameters;
    }

    /** @since 2.2. **/

    public void setParameters(Object context)
    {
        _parameters = context;
    }
}