//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
            char sep = ' ';

            for (int i = 0; i < _parameters.length; i++)
            {
                buffer.append(sep);
                buffer.append(_parameters[i]);

                sep = '/';
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