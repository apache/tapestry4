// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.contrib.inspector;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * Component of the {@link Inspector} page used to display the properties of the
 * {@link org.apache.tapestry.IEngine} as well as a serialized view of it. Also, the
 * {@link org.apache.tapestry.request.RequestContext} is dumped out.
 * 
 * @author Howard Lewis Ship
 */

public abstract class ShowEngine extends BaseComponent implements PageBeginRenderListener
{
    // Injected
    public abstract WebRequest getRequest();

    // Transient
    public abstract void setSessionAttributeNames(List names);

    // Transient
    public abstract void setSession(WebSession session);

    public void pageBeginRender(PageEvent event)
    {
        WebSession session = getRequest().getSession(false);

        setSession(session);

        setSessionAttributeNames(session == null ? Collections.EMPTY_LIST : session
                .getAttributeNames());
    }

}