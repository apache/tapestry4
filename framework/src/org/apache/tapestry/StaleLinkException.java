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

package org.apache.tapestry;

/**
 *  Exception thrown by an {@link IEngineService} when it discovers that
 *  the an action link was for an out-of-date version of the page.
 *
 *  <p>The application should redirect to the StaleLink page.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class StaleLinkException extends RequestCycleException
{
    private transient IPage _page;
    private String _pageName;
    private String _targetIdPath;
    private String _targetActionId;

    public StaleLinkException()
    {
        super();
    }

    /**
     *  Constructor used when the action id is found, but the target id path
     *  did not match the actual id path.
     *
     **/

    public StaleLinkException(IComponent component, String targetActionId, String targetIdPath)
    {
        super(
            Tapestry.getString(
                "StaleLinkException.action-mismatch",
                new String[] { targetActionId, component.getIdPath(), targetIdPath }),
            component);

        _page = component.getPage();
        _pageName = _page.getPageName();
        
        _targetActionId = targetActionId;
        _targetIdPath = targetIdPath;
    }

    /**
     *  Constructor used when the target action id is not found.
     *
     **/

    public StaleLinkException(IPage page, String targetActionId, String targetIdPath)
    {
        this(
            Tapestry.getString("StaleLinkException.component-mismatch", targetActionId, targetIdPath),
            page);

        _targetActionId = targetActionId;
        _targetIdPath = targetIdPath;
    }

    public StaleLinkException(String message, IComponent component)
    {
        super(message, component);
    }

    public StaleLinkException(String message, IPage page)
    {

        super(message, null);
        _page = page;

        if (page != null)
            _pageName = page.getPageName();
    }

    public String getPageName()
    {
        return _pageName;
    }

    /**
     *  Returns the page referenced by the service URL, if known, 
     *  or null otherwise.
     *
     **/

    public IPage getPage()
    {
        return _page;
    }
}