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

package net.sf.tapestry.link;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.StaleSessionException;
import net.sf.tapestry.Tapestry;
import org.apache.log4j.Category;

/**
 *  A component for creating a link using the direct service; used for actions that
 *  are not dependant on dynamic page state.
 *
 *
 *  <table border=1>
 *  <tr> <th>Parameter</th> 
 *  <th>Type</th> 
 *  <th>Direction</th> 
 *  <th>Required</th> 
 *  <th>Default</th>
 *  <th>Description</th>
 *  </tr>
 * 
 * <tr>
 *  <td>listener</td> 
 *  <td>{@link IActionListener}</td>
 *  <td>in</td>
 *  <td>yes</td> 
 *  <td>&nbsp;</td>
 *  <td>Specifies an object that is notified when the link is clicked.</td> </tr>
 *
 *  <tr>
 *  <td>parameters</td>
 *  <td>Object[] <br> List (of Object) <br>Object</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>An array of Strings to be encoded into the URL.  These parameters will
 *  be decoded when the link is triggered.
 *  <p>If the context is simply an Object, then <code>toString()</code> is invoked on
 *  it.  It is assumed that the listener will be able to convert it back.
 *  <p>In a web application built onto of Enterprise JavaBeans, the context is
 *  often the primary key of some Entity bean; typically such keys are Strings or
 *  Integers (which can be freely converted from String to Integer by the listener).
 * 
 *  <p>
 *  A listener method can retrieve the parameters
 *  using {@link net.sf.tapestry.IRequestCycle#getServiceParameters()}.
 * 
 *  <p>
 *  Prior to release 2.2, the parameters were always type String.
 *  They may now be of any type; type will be maintained
 *  when the parameters are later retrieved by a listener.  See
 *  {@link net.sf.tapestry.util.io.DataSqueezer} for more details.
 * 
 * </td>
 *  </tr>
 *
  *  <tr>
 *  <td>context</td>
 *  <td>Object[] <br> List (of Object) <br>Object</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>A deprecated name for <b>parameters</b>, this will generate warnings
 *  in 2.2 and be removed in a later release.
 *  </td>
 *  </tr> 
 * <tr>
 *   <td>disabled</td> 
 *   <td>boolean</td> 
 *   <td>in</td> 
 *   <td>no</td> 
 *   <td>false</td>
 *   <td>Controls whether the link is produced.  If disabled, the portion of the template
 *  the link surrounds is still rendered, but not the link itself.
 *  </td></tr>
 *
 *
 * <tr>
 *	<td>stateful</td>
 *  <td>boolean</td>
 *	<td>in</td>
 *	<td>no</td>
 *	<td>true</td>
 *	<td>If true (the default), then the component requires an active (i.e., non-new)
 *  {@link HttpSession} when triggered.  Failing that, it throws a {@link net.sf.tapestry.StaleLinkException}.
 *  If false, then no check is necessary.  The latter works well with links that
 *  encode all necessary state inside the URL itself.</td>
 * </tr>
 *
 * <tr>
 *		<td>scheme</td>
 *		<td>java.lang.String</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If specified, then a longer URL (including scheme, server and possibly port)
 * is generated using the specified scheme. Server is determined fromt he incoming request,
 * and port is deterimined from the port paramter or the incoming request.
 *  </td>
 * </tr>
 *
 * <tr>
 *		<td>port</td>
 *		<td>int</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If specified, then a longer URL (including scheme, server and port)
 *  is generated using the specified port.  The server is determined from the incoming
 *  request, the scheme from the scheme paramter or the incoming request.
 * </td>
 * </tr>
 *
 * <tr>
 *		<td>anchor</td>
 *		<td>java.lang.String</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The name of an anchor or element to link to.  The final URL will have '#'
 *   and the anchor appended to it.
 * </td> </tr>
 *
 * </table>
 *
 * <p>Informal parameters are allowed.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public class Direct extends GestureLink implements IDirect
{
    private static final Category CAT = Category.getInstance(Direct.class);

    private IBinding _listenerBinding;
    private Object _parameters;
    private IBinding _statefulBinding;
    private boolean _warning = true;

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
        return constructContext(_parameters);
    }

    /**
     *  Converts a binding to a context (an array of Strings).
     *  This is used by the {@link Direct} and {@link Service}
     *  components.
     *
     **/

    public static Object[] constructContext(Object contextValue)
    {
        if (contextValue == null)
            return null;

        if (contextValue instanceof Object[])
            return (Object[]) contextValue;

        if (contextValue instanceof List)
        {
            List list = (List) contextValue;

            return list.toArray();
        }

        return new Object[] { contextValue };
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
        if (isStateful())
        {
            HttpSession session = cycle.getRequestContext().getSession();

            if (session == null || session.isNew())
                throw new StaleSessionException();
        }

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

    /**
     *  @deprecated To be removed in 2.3, use {@link #getParameters().
     * 
     **/

    public Object getContext()
    {
        return getParameters();
    }

    /**
     *  @deprecated To be removed in 2.3, use {@link #setParameters(Object)}.
     * 
     **/

    public void setContext(Object context)
    {
        if (_warning)
        {
            CAT.warn(Tapestry.getString("deprecated-component-param", getExtendedId(), "context", "parameters"));

            _warning = false;
        }

        setParameters(context);
    }
}