/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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