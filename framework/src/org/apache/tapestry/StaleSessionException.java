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
 *  the {@link javax.servlet.http.HttpSession}
 *  has timed out (and been replaced by a new, empty
 *  one).
 *
 *  <p>The application should redirect to the stale-session page.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class StaleSessionException extends ApplicationRuntimeException
{
    private transient IPage _page;
    private String _pageName;

    public StaleSessionException()
    {
        this(null, null);
    }

    public StaleSessionException(String message, IPage page)
    {
        super(message, page, null, null);
        _page = page;

        if (page != null)
            _pageName = page.getPageName();
    }

    public String getPageName()
    {
        return _pageName;
    }

    /**
     *  Returns the page referenced by the service URL, if known, or null otherwise.
     *
     **/

    public IPage getPage()
    {
        return _page;
    }
}