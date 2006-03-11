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

import java.util.Iterator;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.web.WebSession;

/**
 * Utility methods to support implementations of
 * {@link org.apache.tapestry.record.PropertyPersistenceStrategy}. This consists of code refactored
 * out of {@link org.apache.tapestry.record.SessionPropertyPersistenceStrategy} to support other,
 * similar, persistence types with different rules for how long values are stored in the session.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RecordUtils
{
    /**
     * Builds a {@link PropertyChange} instance for the given key and value pulled from the
     * {@link org.apache.tapestry.web.WebSession}.
     * 
     * @param key
     *            a key, previously created by
     *            {@link #buildChangeKey(String, String, String, String, String)}, consisting of a
     *            strategy id, application id, page name, id path (optional), and a property name,
     *            all seperated by commas.
     * @param value
     *            the value stored in the session with this key
     * @return a {@link PropertyChange} storing the property name and id path (if any), and the
     *         value
     */
    public static PropertyChange buildChange(String key, Object value)
    {
        String[] tokens = TapestryUtils.split(key);

        // Either strategy-id, app-name,page-name,id-path,property
        // or strategy-id,app-name,page-name,property

        String idPath = (tokens.length == 5) ? tokens[3] : null;
        String propertyName = tokens[tokens.length - 1];

        return new PropertyChangeImpl(idPath, propertyName, value);
    }

    /**
     * Iterates over the attributes stored in the session, invoking a callback on each one that
     * matches the given prefix, applicationid and page name. This is used to operate over all
     * stored data for a particular combination of strategy, applicationId and page.
     * 
     * @param strategyId
     *            a unique identifier for a particular implementation of
     *            {@link PropertyPersistenceStrategy}
     * @param applicationId
     *            a unique id for the application
     * @param pageName
     *            the name of the page
     * @param session
     *            the session to search
     * @param callback
     *            the callback to invoke on each matching attibute name
     */
    public static void iterateOverMatchingAttributes(String strategyId, String applicationId,
            String pageName, WebSession session, WebSessionAttributeCallback callback)
    {
        Defense.notNull(strategyId, "strategyId");
        Defense.notNull(applicationId, "applicationId");
        Defense.notNull(pageName, "pageName");
        Defense.notNull(session, "session");

        String prefix = strategyId + "," + applicationId + "," + pageName + ",";

        Iterator i = session.getAttributeNames().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            if (name.startsWith(prefix))
                callback.handleAttribute(session, name);
        }
    }

    /**
     * Builds a change key, used to identify the change within the {@link WebSession}. A change key
     * can be used as a session attribute name, without reasonable fear of conflict.
     * 
     * @param strategyId
     *            a unique identifier for a particular implementation of
     *            {@link PropertyPersistenceStrategy}
     * @param applicationId
     *            a unique identifier for the application
     * @param pageName
     *            the name of the page containing the change
     * @param idPath
     *            the id path of the component within the page containing the page, possibly null
     * @param propertyName
     *            the name of the property
     * @return the above values, seperated by commas (well, no comma between the prefix and the
     *         application id)
     */
    public static String buildChangeKey(String strategyId, String applicationId, String pageName,
            String idPath, String propertyName)
    {
        Defense.notNull(strategyId, "strategyId");
        Defense.notNull(applicationId, "applicationId");
        Defense.notNull(pageName, "pageName");
        Defense.notNull(propertyName, "propertyName");

        StringBuffer buffer = new StringBuffer(strategyId);

        buffer.append(",");
        buffer.append(applicationId);
        buffer.append(",");
        buffer.append(pageName);

        if (idPath != null)
        {
            buffer.append(",");
            buffer.append(idPath);
        }

        buffer.append(",");
        buffer.append(propertyName);

        return buffer.toString();
    }
}
