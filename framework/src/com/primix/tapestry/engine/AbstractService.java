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

package com.primix.tapestry.engine;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.primix.tapestry.Gesture;
import com.primix.tapestry.IEngineService;
import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.RequestContext;
import com.primix.tapestry.util.StringSplitter;
import com.primix.tapestry.util.pool.Pool;

/**
 *  Abstract base class for implementing engine services.  Instances of services
 *  are shared by many engines and threads, so they must be threadsafe.
 * 
 *  <p>For efficiency, this class implements several protected methods for providing
 *  and discarding common objects.  These make use of a {@link Pool} to store values
 *  for later re-use.  After invoking <code>discard()</code>, make no further
 *  changes to the object.  Only invoke <code>discard()</code> on an object
 *  returned by a <code>provide<i>Type</i>()</code> method.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.3
 */

public abstract class AbstractService implements IEngineService
{
    private static final int MAP_SIZE = 3;

    /** @deprecated **/
    
    private Pool helperBeanPool;

    private static StringSplitter splitter = new StringSplitter('/');

    /**
     *  Returns a buffer, cleared and ready to go.  For efficiency, callers should
     *  invoke {@link #discard(StringBuffer)} when done with the buffer.
     * 
     *  @deprecated 
     *
     **/

    protected StringBuffer provideStringBuffer()
    {
        StringBuffer result = (StringBuffer) helperBeanPool.retrieve(StringBuffer.class.getName());

        if (result == null)
            result = new StringBuffer();

        return result;
    }

    private void discard(Object value)
    {
        helperBeanPool.store(value.getClass().getName(), value);
    }

    /**
     * 
     *  @deprecated
     * 
     **/

    protected void discard(StringBuffer buffer)
    {
        buffer.setLength(0);
        discard((Object) buffer);
    }

    /**
     *  @deprecated
     * 
     **/

    protected String[] provideString(int length)
    {
        String key = "java.lang.String[" + length + "]";

        String[] result = (String[]) helperBeanPool.retrieve(key);

        if (result == null)
            result = new String[length];

        return result;
    }

    /**
      *  @deprecated
      * 
      **/

    protected void discard(String[] array)
    {
        for (int i = 0; i < array.length; i++)
            array[i] = null;

        String key = "java.lang.String[" + array.length + "]";

        helperBeanPool.store(key, array);
    }

    /**
     *  Returns an instance of {@link HashMap}.
     * 
     *  @deprecated
     * 
     **/

    protected Map provideMap()
    {
        Map result = (Map) helperBeanPool.retrieve(HashMap.class.getName());

        if (result == null)
            result = new HashMap();

        return result;
    }

    /**
      *  @deprecated
      * 
      **/

    protected void discard(Map map)
    {
        map.clear();

        discard((Object) map);
    }

    /**
     *  Assembles a URL for the service.
     *
     *  @param the path for the servlet for this Tapestry application
     *  @param serviceName the name of the service
     *  @param serviceContext context related to the service itself which is added to the URL as-is
     *  @param otherContext additional context provided by the component; this is application specific
     *  information, and is encoded with {@link URLEncoder#encode(String)} before being added
     *  to the query.
     *
     */

    protected Gesture assembleGesture(
        IRequestCycle cycle,
        String serviceName,
        String[] serviceContext,
        String[] parameters)
    {
        Map map = provideMap();

        map.put(SERVICE_QUERY_PARAMETER_NAME, serviceName);

        if (serviceContext != null && serviceContext.length > 0)
        {
            StringBuffer buffer = provideStringBuffer();

            for (int i = 0; i < serviceContext.length; i++)
            {
                if (i > 0)
                    buffer.append('/');

                buffer.append(serviceContext[i]);
            }

            map.put(CONTEXT_QUERY_PARMETER_NAME, buffer.toString());

            discard(buffer);
        }

        if (parameters != null && parameters.length != 0)
        {
            StringBuffer buffer = provideStringBuffer();

            for (int i = 0; i < parameters.length; i++)
            {
                if (i > 0)
                    buffer.append('/');

                buffer.append(URLEncoder.encode(parameters[i]));
            }

            map.put(PARAMETERS_QUERY_PARAMETER_NAME, buffer.toString());

            discard(buffer);
        }

        Gesture result = new Gesture(cycle.getEngine().getServletPath(), map);

        discard(map);

        return result;
    }

    /**
     *  Returns a {@link StringSplitter} configured to split on slashes.
     *
     */

    protected StringSplitter getSplitter()
    {
        return splitter;
    }

    /**
     *  Returns the service context as an array of Strings.
     *
     */

    protected String[] getServiceContext(RequestContext context)
    {
        String parameter = context.getParameter(CONTEXT_QUERY_PARMETER_NAME);

        return getSplitter().splitToArray(parameter);
    }

    /**
     *  Returns the service parameters as an array of Strings.
     *  The strings will have been passed through
     *  {@link URLDecoder#decode(String)}.
     *
     */

    protected String[] getParameters(RequestContext context)
    {
        String parameter = context.getParameter(PARAMETERS_QUERY_PARAMETER_NAME);

        if (parameter == null)
            return null;

        String[] result = getSplitter().splitToArray(parameter);

        for (int i = 0; i < result.length; i++)
            result[i] = URLDecoder.decode(result[i]);

        return result;
    }

    /**
     *   @deprecated
     * 
     **/
    
    public void setHelperBeanPool(Pool value)
    {
        helperBeanPool = value;
    }

}