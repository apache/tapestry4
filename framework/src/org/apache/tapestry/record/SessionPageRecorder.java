/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.StringSplitter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Simple implementation of {@link org.apache.tapestry.IPageRecorder}
 *  that stores page changes as {@link javax.servlet.http.HttpSession} attributes.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 * 
 **/

public class SessionPageRecorder extends PageRecorder
{
    private static final Log LOG = LogFactory.getLog(SessionPageRecorder.class);

    /**
     *  Dictionary of changes, keyed on an instance of 
     *  {@link ChangeKey}
     *  (which enapsulates component path and property name).  The
     *  value is the new value for the object.
     *  The same information is stored into the
     *  {@link HttpSession}, which is  used as a kind of
     *  write-behind cache.
     *
     **/

    private Map _changes;

    /**
     *  The session into which changes are recorded.
     * 
     *  @since 3.0
     * 
     **/

    private HttpSession _session;

    /**
     *  The fully qualified name of the page being recorded.
     * 
     *  @since 3.0
     * 
     **/

    private String _pageName;

    /**
     *  The prefix (for {@link HttpSession} attributes) used by this
     *  page recorder.
     * 
     **/

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

            String attributeKey =
                constructAttributeKey(key.getComponentPath(), key.getPropertyName());

            if (LOG.isDebugEnabled())
                LOG.debug("Removing session attribute " + attributeKey);

            _session.removeAttribute(attributeKey);
        }
    }

    /**
     *  Simply clears the dirty flag, because there is no external place
     *  to store changed page properties.  Sets the locked flag to prevent
     *  subsequent changes from occuring now.
     *
     **/

    public void commit()
    {
        setDirty(false);
        setLocked(true);
    }

    /**
     *  Returns true if the recorder has any changes recorded.
     *
     **/

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

            PageChange change =
                new PageChange(key.getComponentPath(), key.getPropertyName(), value);

            result.add(change);
        }

        return result;
    }

    protected void recordChange(String componentPath, String propertyName, Object newValue)
    {
        ChangeKey key = new ChangeKey(componentPath, propertyName);

        if (_changes == null)
            _changes = new HashMap();

        // Check the prior value.  If this is not an actual change,
        // then don't bother recording it, or marking this page recorder
        // dirty.

        Object oldValue = _changes.get(key);
        if (newValue == oldValue)
            return;

        try
        {
            if (oldValue != null && oldValue.equals(newValue))
                return;
        }
        catch (Exception ex)
        {
            // Ignore.
        }

        setDirty(true);

        _changes.put(key, newValue);

        // Now, build a key used to store the new value
        // in the HttpSession

        String attributeKey = constructAttributeKey(componentPath, propertyName);

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
            // when they use the same page names.  The second name
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
            LOG.debug(
                count == 0 ? "No recorded changes." : "Restored " + count + " recorded changes.");
    }

}