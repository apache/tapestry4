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

package org.apache.tapestry.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.util.StringSplitter;

/**
 * The most basic {@link org.apache.tapestry.record.PropertyPersistenceStrategy}, which stores
 * properties in the HttpSession as attributes.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SessionPropertyPersistenceStrategy implements PropertyPersistenceStrategy
{
    // Really, the name of the servlet; used as a prefix on all HttpSessionAttribute keys
    // to keep things straight if multiple Tapestry apps are deployed
    // in the same WAR.

    private String _applicationId;

    private HttpServletRequest _request;

    private StringSplitter _splitter = new StringSplitter(',');

    public void store(String pageName, String idPath, String propertyName, Object newValue)
    {
        Defense.notNull(pageName, "pageName");
        Defense.notNull(propertyName, "propertyName");

        HttpSession session = _request.getSession(true);

        StringBuffer buffer = new StringBuffer();

        buffer.append(_applicationId);
        buffer.append(",");
        buffer.append(pageName);

        if (idPath != null)
        {
            buffer.append(",");
            buffer.append(idPath);
        }

        buffer.append(",");
        buffer.append(propertyName);

        String key = buffer.toString();

        if (newValue == null)
            session.removeAttribute(key);
        else
            session.setAttribute(key, newValue);
    }

    public Collection getStoredChanges(String pageName, IRequestCycle cycle)
    {
        Defense.notNull(pageName, "pageName");

        HttpSession session = _request.getSession(false);

        if (session == null)
            return Collections.EMPTY_LIST;

        Collection result = new ArrayList();

        String prefix = _applicationId + "," + pageName + ",";

        Enumeration e = session.getAttributeNames();
        while (e.hasMoreElements())
        {
            String key = (String) e.nextElement();

            if (key.startsWith(prefix))
            {
                IPageChange change = buildChange(key, session.getAttribute(key));

                result.add(change);
            }
        }

        return result;
    }

    private IPageChange buildChange(String key, Object value)
    {
        String[] tokens = _splitter.splitToArray(key);

        // Either app-name,page-name,id-path,property
        // or app-name,page-name,property

        String idPath = (tokens.length == 4) ? tokens[2] : null;
        String propertyName = tokens[tokens.length - 1];

        return new PageChange(idPath, propertyName, value);
    }

    public void discardStoredChanges(String pageName, IRequestCycle cycle)
    {
        Defense.notNull(pageName, "pageName");

        HttpSession session = _request.getSession(false);

        if (session == null)
            return;

        String prefix = _applicationId + "," + pageName + ",";

        Enumeration e = session.getAttributeNames();
        while (e.hasMoreElements())
        {
            String key = (String) e.nextElement();

            if (key.startsWith(prefix))
                session.removeAttribute(key);
        }
    }

    public void setApplicationId(String applicationName)
    {
        _applicationId = applicationName;
    }

    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }
}