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

package org.apache.tapestry.html;

import org.apache.tapestry.event.PageAttachListener;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

/**
 * @author Howard M. Lewis Ship
 */
public class ListenerFixture implements PageAttachListener, PageBeginRenderListener,
        PageDetachListener, PageEndRenderListener, PageValidateListener
{
    private String _method;

    public String getMethod()
    {
        return _method;
    }

    public void reset()
    {
        _method = null;
    }

    public void pageAttached(PageEvent event)
    {
        _method = "pageAttached";
    }

    public void pageBeginRender(PageEvent event)
    {
        _method = "pageBeginRender";
    }

    public void pageDetached(PageEvent event)
    {
        _method = "pageDetached";
    }

    public void pageEndRender(PageEvent event)
    {
        _method = "pageEndRender";
    }

    public void pageValidate(PageEvent event)
    {
        _method = "pageValidate";
    }
}