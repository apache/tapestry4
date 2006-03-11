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

import org.apache.hivemind.util.Defense;
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
    public static final String STRATEGY_ID = "session";

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

        String attributeName = RecordUtils.buildChangeKey(
                STRATEGY_ID,
                _applicationId,
                pageName,
                idPath,
                propertyName);

        session.setAttribute(attributeName, newValue);
    }

    public Collection getStoredChanges(String pageName)
    {
        Defense.notNull(pageName, "pageName");

        WebSession session = _request.getSession(false);

        if (session == null)
            return Collections.EMPTY_LIST;

        final Collection result = new ArrayList();

        WebSessionAttributeCallback callback = new WebSessionAttributeCallback()
        {
            public void handleAttribute(WebSession session, String name)
            {
                PropertyChange change = RecordUtils.buildChange(name, session.getAttribute(name));

                result.add(change);
            }
        };

        RecordUtils.iterateOverMatchingAttributes(
                STRATEGY_ID,
                _applicationId,
                pageName,
                session,
                callback);

        return result;
    }

    public void discardStoredChanges(String pageName)
    {
        WebSession session = _request.getSession(false);

        if (session == null)
            return;

        WebSessionAttributeCallback callback = new WebSessionAttributeCallback()
        {
            public void handleAttribute(WebSession session, String name)
            {
                session.setAttribute(name, null);
            }
        };

        RecordUtils.iterateOverMatchingAttributes(
                STRATEGY_ID,
                _applicationId,
                pageName,
                session,
                callback);
    }

    /**
     * Does nothing; session persistence does not make use of query parameters.
     */

    public void addParametersForPersistentProperties(ServiceEncoding encoding, boolean post)
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