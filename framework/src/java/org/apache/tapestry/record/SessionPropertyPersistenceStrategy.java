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
import java.util.Iterator;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * The most basic {@link org.apache.tapestry.record.PropertyPersistenceStrategy}, which stores
 * properties in the HttpSession as attributes.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class SessionPropertyPersistenceStrategy implements PropertyPersistenceStrategy
{
    // Really, the name of the servlet; used as a prefix on all HttpSessionAttribute keys
    // to keep things straight if multiple Tapestry apps are deployed
    // in the same WAR.

    private String _applicationId;

    private WebRequest _request;

    public void store(String pageName, String idPath, String propertyName, Object newValue)
    {
        Defense.notNull(pageName, "pageName");
        Defense.notNull(propertyName, "propertyName");

        WebSession session = _request.getSession(true);

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

        session.setAttribute(key, newValue);
    }

    public Collection getStoredChanges(String pageName, IRequestCycle cycle)
    {
        Defense.notNull(pageName, "pageName");

        WebSession session = _request.getSession(false);

        if (session == null)
            return Collections.EMPTY_LIST;

        Collection result = new ArrayList();

        String prefix = _applicationId + "," + pageName + ",";

        Iterator i = session.getAttributeNames().iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();

            if (key.startsWith(prefix))
            {
                PropertyChange change = buildChange(key, session.getAttribute(key));

                result.add(change);
            }
        }

        return result;
    }

    private PropertyChange buildChange(String key, Object value)
    {
        String[] tokens = TapestryUtils.split(key);

        // Either app-name,page-name,id-path,property
        // or app-name,page-name,property

        String idPath = (tokens.length == 4) ? tokens[2] : null;
        String propertyName = tokens[tokens.length - 1];

        return new PropertyChangeImpl(idPath, propertyName, value);
    }

    public void discardStoredChanges(String pageName, IRequestCycle cycle)
    {
        Defense.notNull(pageName, "pageName");

        WebSession session = _request.getSession(false);

        if (session == null)
            return;

        String prefix = _applicationId + "," + pageName + ",";

        Iterator i = session.getAttributeNames().iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();

            if (key.startsWith(prefix))
                session.setAttribute(key, null);
        }
    }

    /**
     * Does nothing; session persistence does not make use of query parameters.
     */

    public void addParametersForPersistentProperties(ServiceEncoding encoding, IRequestCycle cycle, boolean post)
    {
    }

    public void setApplicationId(String applicationName)
    {
        _applicationId = applicationName;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }
}