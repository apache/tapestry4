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

package org.apache.tapestry.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.StringSplitter;

/**
 * Simple implementation of {@link org.apache.tapestry.engine.IPageRecorder}that stores page
 * changes as {@link javax.servlet.http.HttpSession}attributes.
 * 
 * @author Howard Ship
 * @version $Id$
 */

public class SessionPageRecorder extends PageRecorder
{
    private static final Log LOG = LogFactory.getLog(SessionPageRecorder.class);

    /**
     * Dictionary of changes, keyed on an instance of {@link ChangeKey}(which enapsulates component
     * path and property name). The value is the new value for the object. The same information is
     * stored into the {@link HttpSession}, which is used as a kind of write-behind cache.
     */

    private Map _changes;

    /**
     * The session into which changes are recorded.
     * 
     * @since 3.0
     */

    private HttpSession _session;

    /**
     * The fully qualified name of the page being recorded.
     * 
     * @since 3.0
     */

    private String _pageName;

    /**
     * The prefix (for {@link HttpSession}attributes) used by this page recorder.
     */

    private String _attributePrefix;

    public void initialize(String pageName, IRequestCycle cycle)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Initializing for " + pageName);

        RequestContext context = cycle.getRequestContext();

        _pageName = pageName;
        _session = context.getSession();

        _attributePrefix = context.getServlet().getServletName() + "/" + _pageName + "/";

        restorePageChanges();
    }

    public void discard()
    {
        if (Tapestry.isEmpty(_changes))
            return;

        Iterator i = _changes.keySet().iterator();

        while (i.hasNext())
        {
            ChangeKey key = (ChangeKey) i.next();

            String attributeKey = constructAttributeKey(key.getComponentPath(), key
                    .getPropertyName());

            if (LOG.isDebugEnabled())
                LOG.debug("Removing session attribute " + attributeKey);

            _session.removeAttribute(attributeKey);
        }
    }

    /**
     * Simply clears the dirty flag, because there is no external place to store changed page
     * properties. Sets the locked flag to prevent subsequent changes from occuring now.
     */

    public void commit()
    {
        setDirty(false);
        setLocked(true);
    }

    /**
     * Returns true if the recorder has any changes recorded.
     */

    public boolean getHasChanges()
    {
        if (_changes == null)
            return false;

        return (_changes.size() > 0);
    }

    public Collection getChanges()
    {
        if (_changes == null)
            return Collections.EMPTY_LIST;

        int count = _changes.size();
        Collection result = new ArrayList(count);

        Iterator i = _changes.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            ChangeKey key = (ChangeKey) entry.getKey();

            Object value = entry.getValue();

            PageChange change = new PageChange(key.getComponentPath(), key.getPropertyName(), value);

            result.add(change);
        }

        return result;
    }

    protected void recordChange(String componentPath, String propertyName, Object newValue)
    {
        ChangeKey key = new ChangeKey(componentPath, propertyName);

        if (_changes == null)
            _changes = new HashMap();

        setDirty(true);

        _changes.put(key, newValue);

        // Now, build a key used to store the new value
        // in the HttpSession

        String attributeKey = constructAttributeKey(componentPath, propertyName);

        if (newValue == null)
            _session.removeAttribute(attributeKey);
        else
            _session.setAttribute(attributeKey, newValue);

        if (LOG.isDebugEnabled())
            LOG.debug("Stored session attribute " + attributeKey + " = " + newValue);
    }

    private String constructAttributeKey(String componentPath, String propertyName)
    {
        StringBuffer buffer = new StringBuffer(_attributePrefix);

        if (componentPath != null)
        {
            buffer.append(componentPath);
            buffer.append('/');
        }

        buffer.append(propertyName);

        return buffer.toString();
    }

    private void restorePageChanges()
    {
        int count = 0;
        Enumeration e = _session.getAttributeNames();
        StringSplitter splitter = null;

        while (e.hasMoreElements())
        {
            String key = (String) e.nextElement();

            if (!key.startsWith(_attributePrefix))
                continue;

            if (LOG.isDebugEnabled())
                LOG.debug("Restoring page change from session attribute " + key);

            if (_changes == null)
            {
                _changes = new HashMap();

                splitter = new StringSplitter('/');
            }

            String[] names = splitter.splitToArray(key);

            // The first name is the servlet name, which allows
            // multiple Tapestry apps to share a HttpSession, even
            // when they use the same page names. The second name
            // is the page name, which we already know.

            int i = 2;

            String componentPath = (names.length == 4) ? names[i++] : null;
            String propertyName = names[i++];
            Object value = _session.getAttribute(key);

            ChangeKey changeKey = new ChangeKey(componentPath, propertyName);

            _changes.put(changeKey, value);

            count++;
        }

        if (LOG.isDebugEnabled())
            LOG.debug(count == 0 ? "No recorded changes." : "Restored " + count
                    + " recorded changes.");
    }

}