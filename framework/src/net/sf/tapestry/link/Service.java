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

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;

/**
 *  A component for creating a link for an arbitrary {@link net.sf.tapestry.IEngineService
 *  engine service}.  A Service component can emulate an {@link Action},
 *  {@link Page} or {@link Direct} component, but is most often used in
 *  conjunction with an application-specific service.  
 *
 * <table border=1>
 *  <tr> 
 *  <th>Parameter</th> 
 *  <th>Type</th> 
 *  <th>Direction</th> 
 *  <th>Required</th> 
 *  <th>Default</th> 
 *  <th>Description</th>
 *  </tr>
 *
 * <tr>
 *		<td>service</td>
 *		<td>{@link String}</td>
 *		<td>in</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The name of the service.</td>  </tr>
 *
 * <tr>
 *   <td>disabled</td> 
 *   <td>boolean</td> 
 *   <td>in</td> 
 *  <td>no</td>
 *  <td>false</td>
 *   <td>Controls whether the link is produced.  If disabled, the portion of the template
 *  the link surrounds is still rendered, but not the link itself.
 *  </td></tr>
 *
 *
 *  <tr>
 *  <td>context</td>
 *  <td>String[] <br> List (of String) <br> String <br>Object</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>An array of Strings to be encoded into the URL.  These parameters will
 *  be decoded when the link is triggered.
 *  <p>If the context is simply an Object, then <code>toString()</code> is invoked on
 it.  It is assumed that the listener will be able to convert it back.
 *  <p>In a web application built onto of Enterprise JavaBeans, the context is
 *  often the primary key of some Entity bean; typically such keys are Strings or
 *  Integers (which can be freely converted from String to Integer by the listener).</td>
 * </tr>
 *
 * <tr>
 *		<td>scheme</td>
 *		<td>{@link String}</td>
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
 *		<td>{@link String}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The name of an anchor or element to link to.  The final URL will have '#'
 *   and the anchor appended to it.
 *  </td> </tr>
 *
 *  </table>
 *
 *  <p>Informal parameters are allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Service extends GestureLink
{
    private String service;
    private Object context;
    
    /**
     *  Returns name of the service specified by the service parameter.
     **/

    protected String getServiceName()
    {
        return service;
    }

    /** 
     *  Invokes {@link Direct#constructContext(Object)} to create String[]
     *  from the context parameter (which may be an object, array of Strings or List of Strings).
     * 
     **/
    
    protected String[] getContext(IRequestCycle cycle)
    {
        return Direct.constructContext(context);
        
    }
    
    public Object getContext()
    {
        return context;
    }

    public void setContext(Object context)
    {
        this.context = context;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

}