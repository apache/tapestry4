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

package org.apache.tapestry.callback;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 *  Simple callback for returning to a page.
 *  <p>
 *  Example usage of <tt>PageCallback</tt>:
 *  <p>
 *  The Home page ensure a user is 
 *  authenticated in the {@link org.apache.tapestry.IPage#validate(IRequestCycle)} 
 *  method.  If the user is not authenticated, they are redirected to the Login 
 *  page, after setting a callback in the Login page.
 *  <p>
 *  The Login page <tt>formSubmit()</tt> {@link org.apache.tapestry.IActionListener} 
 *  authenticates the user and then invokes {@link ICallback#performCallback(IRequestCycle)} 
 *  to the Home page.
 *  <pre>
 *  public class Home extends BasePage {
 * 
 *      public void validate(IRequestCycle cycle) {            
 *          Visit visit = (Visit) getVisit();
 *      
 *          if (!visit.isAuthenticated()) {
 *              Login login = (Login) cycle.getPage("Login");
 *
 *              login.setCallback(new PageCallback(this));
 *              
 *              throw new PageRedirectException(login);
 *          }            
 *      }
 *  }
 *
 *  public Login extends BasePage {
 * 
 *      private ICallback _callback;
 *
 *      public void setCallback(ICallback _callback) {
 *          _callback = callback;
 *      }
 *
 *      public void formSubmit(IRequestCycle cycle) {
 *          // Authentication code
 *          ..
 *   
 *          Visit visit = (Visit) getVisit();
 *
 *          visit.setAuthenticated(true);
 *  
 *          if (_callback != null) {
 *              _callback.performCallback(cycle);
 *          }
 *      }
 *  }    
 *  </pre>
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 *
 **/

public class PageCallback implements ICallback
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -3286806776105690068L;

    private String _pageName;

    public PageCallback(String pageName)
    {
        _pageName = pageName;
    }

    public PageCallback(IPage page)
    {
        this(page.getPageName());
    }

    public String toString()
    {
        return "PageCallback[" + _pageName + "]";
    }

    /**
     *  Invokes {@link IRequestCycle#activate(String)} to select the previously
     *  identified page as the response page.
     *
     **/

    public void performCallback(IRequestCycle cycle)
    {
        cycle.activate(_pageName);
    }
}