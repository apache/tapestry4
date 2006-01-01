// Copyright 2004, 2005, 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.engine;

/**
 * Contains the information needed to encode a request for a service; the servlet path plus and
 * query parameters. The service encoding is passed to each
 * {@link org.apache.tapestry.engine.ServiceEncoder}&nbsp;, which is allowed to modify the encoding
 * (typically, by changing the servlet path and settting query parameters to null). From this
 * modified encoding, an {@link org.apache.tapestry.engine.ILink}can be constructed.
 * <p>
 * Additionally, when a request is dispatched by Tapestry, an SRE is also created and passed to each
 * {@link org.apache.tapestry.engine.ServiceEncoder}&nbsp;for decoding. Here, the query parameters
 * that may have been nulled out by the encoding are restored.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.services.ServiceConstants
 */
public interface ServiceEncoding
{

    /**
     * Returns the value for the named parameter. If multiple values are stored for the query
     * parameter, only the first is returned.
     * 
     * @parameter name the name of the query parameter to access
     * @return the value, or null if no such query parameter exists
     */

    public String getParameterValue(String name);

    /**
     * Returns the value for the named parameter.
     * 
     * @parameter name the name of the query parameter to access
     * @return the values, or null if no such query parameter exists
     */
    public String[] getParameterValues(String name);

    /**
     * Updates the servlet path for the encoding. In some cases, this is a combination of the
     * servlet and additional path info.
     */

    public void setServletPath(String servletPath);

    /**
     * Sets the value for the named query parameter to the provided string.
     * 
     * @param name
     *            the name of the parameter to set.
     * @param value
     *            the new value, which may be null.
     */
    public void setParameterValue(String name, String value);

    /**
     * Sets the values for a named query parameter.
     */

    public void setParameterValues(String name, String[] values);

    /**
     * Returns the servlet path for the request. This is the portion of the URL recognized as the
     * servlet. When the URL pattern (in web.xml) ends in a "*" (such as "/book/*"), this method
     * will return the matched servlet portion ("/book/") and {#link #getPathInfo} will return the
     * rest of the URL.
     */

    public String getServletPath();

    /**
     * Returns the portion of the URL after the servlet itself.
     * 
     * @return pathInfo if path info was supplied in the request, or null otherwise.
     */
    public String getPathInfo();

    /**
     * Returns an array of parameter names. The names are returned in alphabetically sorted order.
     * This list includes all parameter names, even those for which the stored value is null.
     */

    public String[] getParameterNames();
}