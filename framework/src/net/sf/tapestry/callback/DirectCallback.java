package net.sf.tapestry.callback;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Simple callback for re-invoking a {@link IDirect} component trigger..
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 *
 **/

public class DirectCallback implements ICallback
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -8888847655917503471L;

    private String _pageName;
    private String _componentIdPath;
    private Object[] _parameters;

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("DirectCallback[");

        buffer.append(_pageName);
        buffer.append('/');
        buffer.append(_componentIdPath);

        if (_parameters != null)
        {
            String sep = " ";

            for (int i = 0; i < _parameters.length; i++)
            {
                buffer.append(sep);
                buffer.append(_parameters[i]);

                sep = ", ";
            }
        }

        buffer.append(']');

        return buffer.toString();

    }

    /**
     *  Creates a new DirectCallback for the component.  The parameters
     *  (which may be null) is retained, not copied.
     *
     **/

    public DirectCallback(IDirect component, Object[] parameters)
    {
        _pageName = component.getPage().getName();
        _componentIdPath = component.getIdPath();
        _parameters = parameters;
    }

    /**
     *  Locates the {@link IDirect} component that was previously identified
     *  (and whose page and id path were stored).
     *  Invokes {@link IRequestCycle#setServiceParameters(Object[])} to
     *  restore the service parameters, then
     *  invokes {@link IDirect#trigger(IRequestCycle)} on the component.
     *
     **/

    public void performCallback(IRequestCycle cycle) throws RequestCycleException
    {
        IPage page = cycle.getPage(_pageName);
        IComponent component = page.getNestedComponent(_componentIdPath);
        IDirect direct = null;

        try
        {
            direct = (IDirect) component;
        }
        catch (ClassCastException ex)
        {
            throw new RequestCycleException(
                Tapestry.getString("DirectCallback.wrong-type", component.getExtendedId()),
                component,
                ex);
        }

        cycle.setServiceParameters(_parameters);
        direct.trigger(cycle);
    }
}