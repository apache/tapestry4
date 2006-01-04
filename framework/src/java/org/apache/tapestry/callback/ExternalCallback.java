// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;

/**
 * A callback for returning to an {@link org.apache.tapestry.IExternalPage}.
 * <p>
 * Example usage of <tt>ExternalCallback</tt>:
 * <p>
 * The External page ensure a user is authenticated in the
 * {@link org.apache.tapestry.IPage#validate(IRequestCycle)} method. If the user is not
 * authenticated, they are redirected to the Login page, after setting a callback in the Login page.
 * <p>
 * The Login page <tt>formSubmit()</tt> {@link org.apache.tapestry.IActionListener} authenticates
 * the user and then invokes {@link ICallback#performCallback(IRequestCycle)} to the External page.
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     
 *      
 *       
 *        
 *         
 *          
 *           
 *             public class External extends BasePage implements IExternalPage {
 *            
 *                 private Integer _itemId;
 *           
 *                 public void validate(IRequestCycle cycle) throws RequestCycleException {            
 *                     Visit visit = (Visit) getVisit();
 *                 
 *                     if (!visit.isAuthenticated()) {
 *                         Login login = (Login) cycle.getPage(&quot;Login&quot;);
 *           
 *                         login.setCallback
 *                             (new ExternalCallback(this, cycle.getServiceParameters()));
 *                         
 *                         throw new PageRedirectException(login);
 *                     }            
 *                 }
 *            
 *                 public void activateExternalPage(Object[] params, IRequestCycle cycle)
 *                         throws RequestCycleException {            
 *                     _itemId = (Integer) params[0];
 *                 }
 *             }
 *           
 *             public Login extends BasePage {
 *            
 *                 private ICallback _callback;
 *           
 *                 public void setCallback(ICallback _callback) {
 *                     _callback = callback;
 *                 }
 *           
 *                 public void formSubmit(IRequestCycle cycle) {
 *                     // Authentication code
 *                     ..
 *              
 *                     Visit visit = (Visit) getVisit();
 *           
 *                     visit.setAuthenticated(true);
 *             
 *                     if (_callback != null) {
 *                         _callback.performCallback(cycle);
 *                     }
 *                 }
 *             }    
 *             
 *           
 *          
 *         
 *        
 *       
 *      
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * @see org.apache.tapestry.IExternalPage
 * @see org.apache.tapestry.engine.ExternalService
 * @author Malcolm Edgar
 * @since 2.3
 */

public class ExternalCallback implements ICallback
{
    private static final long serialVersionUID = -6783421589702643930L;

    private String _pageName;

    private Object[] _parameters;

    /**
     * Creates a new ExternalCallback for the named <tt>IExternalPage</tt>. The parameters (which
     * may be null) is retained, not copied.
     */

    public ExternalCallback(String pageName, Object[] parameters)
    {
        Defense.notNull(pageName, "pageName");

        _pageName = pageName;
        _parameters = parameters;
    }

    /**
     * Creates a new ExternalCallback for the page. The parameters (which may be null) is retained,
     * not copied.
     */

    public ExternalCallback(IExternalPage page, Object[] parameters)
    {
        Defense.notNull(page, "page");

        _pageName = page.getPageName();
        _parameters = parameters;
    }

    /**
     * Invokes {@link IRequestCycle#setPage(String)} to select the previously identified
     * <tt>IExternalPage</tt> as the response page and activates the page by invoking
     * <tt>activateExternalPage()</tt> with the callback parameters and request cycle.
     */

    public void performCallback(IRequestCycle cycle)
    {
        Defense.notNull(cycle, "cycle");

        try
        {
            IExternalPage page = (IExternalPage) cycle.getPage(_pageName);

            cycle.activate(page);

            page.activateExternalPage(_parameters, cycle);
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(CallbackMessages.pageNotExternal(_pageName), ex);
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("ExternalCallback[");

        buffer.append(_pageName);

        if (_parameters != null)
        {
            for (int i = 0; i < _parameters.length; i++)
            {
                if (i == 0)
                    buffer.append('/');
                else
                    buffer.append(", ");

                buffer.append(_parameters[i]);
            }
        }

        buffer.append(']');

        return buffer.toString();
    }
}