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

package org.apache.tapestry.junit.mock.c5;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;

/**
 *  An actual, reusable kind of component for the test suite.  Works like
 *  a {@link org.apache.tapestry.components.RenderBlock}, but specified the 
 *  {@link org.apache.tapestry.components.Block} to render in terms of
 *  a page name and a nested component id.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class PageBlock extends BaseComponent
{
    private String _targetPageName;
    private String _targetBlockId = "block";
    
    public String getTargetBlockId()
    {
        return _targetBlockId;
    }

    public String getTargetPageName()
    {
        return _targetPageName;
    }

    public void setTargetBlockId(String targetBlockId)
    {
        _targetBlockId = targetBlockId;
    }

    public void setTargetPageName(String targetPageName)
    {
        _targetPageName = targetPageName;
    }

    /**
     *  Used the targetPageName and targetBlockId (which defaults to "block")
     *  to obtain a reference to a Block instance, which is returned.
     * 
     **/
    
    public Block getTargetBlock()
    {
        IRequestCycle cycle = getPage().getRequestCycle();
        IPage targetPage = cycle.getPage(_targetPageName);
        
        return (Block)targetPage.getNestedComponent(_targetBlockId);        
    }
}
