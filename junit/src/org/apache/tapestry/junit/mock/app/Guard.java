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

package org.apache.tapestry.junit.mock.app;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.html.BasePage;

/**
 *  Guard page for the Protected page.  Protected checks the "visited" property of this page and,
 *  if false, redirects to this page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class Guard extends BasePage
{
    private ICallback _callback;
    private boolean _visited;
    
    public void detach()
    {
        _callback = null;
        _visited = false;
        
        super.detach();
    }
    
    public ICallback getCallback()
    {
        return _callback;
    }

    public boolean isVisited()
    {
        return _visited;
    }

    public void setCallback(ICallback callback)
    {
        _callback = callback;
        fireObservedChange("callback", callback);
    }

    public void setVisited(boolean visited)
    {
        _visited = visited;
        fireObservedChange("visited", visited);
    }
    
    public void linkClicked(IRequestCycle cycle)
    {
        setVisited(true);
        
        ICallback callback = _callback;
        
        setCallback(null);
        
        callback.performCallback(cycle);
    }

}
