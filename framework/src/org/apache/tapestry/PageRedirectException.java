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
 *  Exception thrown by a {@link IComponent component} or {@link org.apache.tapestry.engine.IEngineService}
 *  that wishes to force the application to a particular page.  This is often used
 *  to protect a sensitive page until the user is authenticated.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class PageRedirectException extends ApplicationRuntimeException
{
    private String _targetPageName;

    public PageRedirectException(String targetPageName)
    {
        this(targetPageName, null, null, targetPageName);
    }

    public PageRedirectException(IPage page)
    {
        this(page.getPageName());
    }

    public PageRedirectException(
        String message,
        Object component,
        Throwable rootCause,
        String targetPageName)
    {
        super(message, component, null, rootCause);

        _targetPageName = targetPageName;
    }

    public String getTargetPageName()
    {
        return _targetPageName;
    }
}