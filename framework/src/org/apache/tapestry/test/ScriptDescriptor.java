//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.BaseLocatable;

/**
 * Top level object for test scripts.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class ScriptDescriptor extends BaseLocatable
{
    private String _contextName;
    private String _rootDirectory;

    private Map _servletDescriptors = new HashMap();
    private List _requests = new ArrayList();

    private ServletDescriptor _defaultServletDescriptor;

    public String getContextName()
    {
        return _contextName;
    }

    public String getRootDirectory()
    {
        return _rootDirectory;
    }

    public void setContextName(String string)
    {
        _contextName = string;
    }

    public void setRootDirectory(String string)
    {
        _rootDirectory = string;
    }

    /**
     * Gets the named servlet descriptor, or returns null.
     */
    public ServletDescriptor getServletDescriptor(String name)
    {
        return (ServletDescriptor) _servletDescriptors.get(name);
    }

    /**
     * Adds a new ServletDescriptor to the script.
     * 
     * @throws ApplicationRuntimeException if the descriptor's name
     * duplicates an existing descriptor
     */
    public void addServletDescriptor(ServletDescriptor sd)
    {
        String name = sd.getName();

        ServletDescriptor existing = getServletDescriptor(name);

        if (existing != null)
            throw new ApplicationRuntimeException(
                "Servlet descriptor '"
                    + name
                    + "' (at "
                    + sd.getLocation()
                    + ") conflicts with prior instance at "
                    + existing.getLocation()
                    + ".",
                sd.getLocation(),
                null);

        _servletDescriptors.put(name, sd);

        if (_defaultServletDescriptor == null)
            _defaultServletDescriptor = sd;
    }

    public void addRequestDescriptor(RequestDescriptor rd)
    {
        _requests.add(rd);
    }

    /**
     * @return List of {@link RequestDescriptor}
     * @see #addRequestDescriptor(RequestDescriptor)
     */
    public List getRequestDescriptors()
    {
        return _requests;
    }
    
    public ServletDescriptor getDefaultServletDescriptor()
    {
        return _defaultServletDescriptor;
    }

}
