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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * 
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class RequestDescriptor extends BaseLocatable
{
    private String _servletName;
    private String _servletPath;

    /** Map, on name, to {@link org.apache.tapestry.test.ParameterList}. **/
    private Map _parameters = new HashMap();

    /** Map of {@link ResponseAssertion}. **/
    private List _assertions = new ArrayList();

    public void addAssertion(ResponseAssertion assertion)
    {
        _assertions.add(assertion);
    }

    /**
     * Invokes all assertions for the request.
     */

    public void executeAssertions(ScriptedTestSession session)
    {
        Iterator i = _assertions.iterator();
        while (i.hasNext())
        {
            ResponseAssertion a = (ResponseAssertion) i.next();

            a.execute(session);
        }
    }

    public void addParameter(String name, String value)
    {
        ParameterList pl = (ParameterList) _parameters.get(name);
        if (pl == null)
        {
            pl = new ParameterList();
            _parameters.put(name, pl);
        }

        pl.add(value);
    }

    /**
     * Returns the values for the given parameter name.
     * 
     * @return array of strings, or null if no values have been recorded for
     * the given name
     */
    public String[] getParameterValues(String name)
    {
        ParameterList pl = (ParameterList) _parameters.get(name);

        if (pl == null)
            return null;

        return pl.getValues();
    }

    public String getServletName()
    {
        return _servletName;
    }

    public void setServletName(String string)
    {
        _servletName = string;
    }

    public String getServletPath()
    {
        return _servletPath;
    }

    public void setServletPath(String string)
    {
        _servletPath = string;
    }

    /**
     * Returns names of all parameters. Order is not determinate. May return empty (but not null).
     */
    public String[] getParameterNames()
    {
        Collection c = _parameters.keySet();

        return (String[]) c.toArray(new String[c.size()]);
    }

}
