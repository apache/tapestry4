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

package org.apache.tapestry;

/**
 *  Exception thrown by an {@link org.apache.tapestry.engine.IEngineService} when it discovers that
 *  the an action link was for an out-of-date version of the page.
 *
 *  <p>The application should redirect to the StaleLink page.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class StaleLinkException extends ApplicationRuntimeException
{
    private transient IPage _page;
    private String _pageName;
    private String _targetIdPath;
    private String _targetActionId;

    public StaleLinkException()
    {
        super(null, null, null, null);
    }

    /**
     *  Constructor used when the action id is found, but the target id path
     *  did not match the actual id path.
     *
     **/

    public StaleLinkException(IComponent component, String targetActionId, String targetIdPath)
    {
        super(
            Tapestry.format(
                "StaleLinkException.action-mismatch",
                new String[] { targetActionId, component.getIdPath(), targetIdPath }),
            component,
            null,
            null);

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
            Tapestry.format(
                "StaleLinkException.component-mismatch",
                targetActionId,
                targetIdPath),
            page);

        _targetActionId = targetActionId;
        _targetIdPath = targetIdPath;
    }

    public StaleLinkException(String message, IComponent component)
    {
        super(message, component, null, null);
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
    
    public String getTargetActionId()
    {
        return _targetActionId;
    }

    public String getTargetIdPath()
    {
        return _targetIdPath;
    }

}