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

package org.apache.tapestry.junit.mock.c6;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 *  More testing of persistent component properties.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class Nested extends BaseComponent implements PageDetachListener
{
    private  String _message;
    
    public void initialize()
    {
        _message = "Nested";
    }
        
    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String message)
    {
        _message = message;
        
        fireObservedChange("message", message);
    }

    public void updateMessage(IRequestCycle cycle)
    {
        setMessage("Changed");
    }
    
    public void finishLoad()
    {
        initialize();
    }

    public void pageDetached(PageEvent event)
    {
        initialize();
    }

}
