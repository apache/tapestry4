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

import org.apache.tapestry.html.BasePage;

/**
 *  Displays the results passed as a parameter to {@link org.apache.tapestry.junit.mock.app.ProtectedLink} 
 *  page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class ProtectedLinkResult extends BasePage
{
    private Object[] _parameters;
    
    public void detach()
    {
        _parameters = null;
        
        super.detach();
    }
    
    
    public Object[] getParameters()
    {
        return _parameters;
    }

    public void setParameters(Object[] parameters)
    {
        _parameters = parameters;
    }
}
