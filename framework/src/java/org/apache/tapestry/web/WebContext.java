// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.web;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import org.apache.tapestry.describe.Describable;

/**
 * A representation of a set of servlets (or portlets) packaged together as a web application
 * archive. Attributes stored within the context are global to all 'lets (but not distributed across
 * a server cluster).
 * 
 * @author Howard M. Lewis Ship
 */
public interface WebContext extends AttributeHolder, InitializationParameterHolder, Describable
{
    /**
     * Returns a URL to the resource that is mapped to a specified path. The path must begin with a
     * "/" and is interpreted as relative to the current context root.
     */

    URL getResource(String path);

    /**
     * Returns the MIME type of the specified file, or null if the MIME type is not known.
     */
    String getMimeType(String resourcePath);
    
    /**
     * Returns a directory-like listing of all the paths to resources within 
     * the web application whose longest sub-path matches the supplied 
     * path argument. Paths indicating subdirectory paths end with a slash (/). 
     * The returned paths are all relative to the root of the web application 
     * and have a leading '/'. 
     * @param path  partial path used to match the resources, which must start with a '/'
     * @return a Set containing the directory listing, or null if there are no resources 
     * in the web application whose path begins with the supplied path.
     */
    Set getResourcePaths(String path);
    
    /**
     * Returns the resource located at the named path as an <code>InputStream</code>
     * object.
     * @param path a <code>String</code> specifying the path to the resource
     * @return the <code>InputStream</code> returned to the servlet, 
     *         or <code>null</code> if no resource exists at the specified path
     */
    InputStream getResourceAsStream(String path);
    
    /**
     * Returns a <code>String</code> containing the real path for a given virtual path. 
     * For example, the path "/index.html" returns the absolute file path 
     * on the server's filesystem would be served by a request for 
     * "http://host/contextPath/index.html", where contextPath is the 
     * context path of this WebContext.
     * 
     * @param path a <code>String</code> specifying a virtual path
     * @return a <code>String</code> specifying the real path, or <code>null</code> if the 
     *         translation cannot be performed
     */
    String getRealPath(String path);
}
