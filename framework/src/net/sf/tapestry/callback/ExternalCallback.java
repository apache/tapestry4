//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.callback;

import net.sf.tapestry.IExternalPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  A callback for returning to an {@link net.sf.tapestry.IExternalPage}.
 *  <p>
 *  Example usage of <tt>ExternalCallback</tt>: 
 *  <p>
 *  The External page ensure a user is authenticated in the 
 *  {@link net.sf.tapestry.IPage#validate(IRequestCycle)} method. 
 *  If the user is not authenticated, they are redirected to the Login page, after 
 *  setting a callback in the Login page.
 *  <p>
 *  The Login page <tt>formSubmit()</tt> {@link net.sf.tapestry.IActionListener} 
 *  authenticates the user and then invokes {@link ICallback#performCallback(IRequestCycle)} 
 *  to the External page.
 *  <pre>
 *  public class External extends BasePage implements IExternalPage {
 * 
 *      private Integer _itemId;
 *
 *      public void validate(IRequestCycle cycle) throws RequestCycleException {            
 *          Visit visit = (Visit) getVisit();
 *      
 *          if (!visit.isAuthenticated()) {
 *              Login login = (Login) cycle.getPage("Login");
 *
 *              login.setCallback
 *                  (new ExternalCallback(this, cycle.getServiceParameters()));
 *              
 *              throw new PageRedirectException(login);
 *          }            
 *      }
 * 
 *      public void activateExternalPage(Object[] params, IRequestCycle cycle)
 *              throws RequestCycleException {            
 *          _itemId = (Integer) params[0];
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
 *      public void formSubmit(IRequestCycle cycle) throws RequestCycleException {
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
 *  @see net.sf.tapestry.IExternalPage
 *  @see net.sf.tapestry.engine.ExternalService
 *
 *  @version $Id$
 *  @author Malcolm Edgar
 *  @since 2.3
 *
 **/

public class ExternalCallback implements ICallback
{
    private String _pageName;
    private Object[] _parameters;

    /**
     *  Creates a new ExternalCallback for the named <tt>IExternalPage</tt>.  
     *  The parameters (which may be null) is retained, not copied.
     *
     **/

    public ExternalCallback(String pageName, Object[] parameters)
    {
        _pageName = pageName;
        _parameters = parameters;
    }

    /**
     *  Creates a new ExternalCallback for the page.  The parameters
     *  (which may be null) is retained, not copied.
     *
     **/

    public ExternalCallback(IExternalPage page, Object[] parameters)
    {
        _pageName = page.getName();
        _parameters = parameters;
    }

    /**
     *  Invokes {@link IRequestCycle#setPage(String)} to select the previously
     *  identified <tt>IExternalPage</tt> as the response page and activates
     *  the page by invoking <tt>activateExternalPage()</tt> with the callback 
     *  parameters and request cycle.
     *
     **/

    public void performCallback(IRequestCycle cycle) throws RequestCycleException
    {        
        try
        {
            IExternalPage page = (IExternalPage) cycle.getPage(_pageName);
            
            cycle.setPage(page);
    
            page.activateExternalPage(_parameters, cycle);            
        }
        catch (ClassCastException ex)
        {
            throw new RequestCycleException(
                Tapestry.getString("ExternalCallback.page-not-compatible", _pageName),
                null,
                ex);
        }
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("ExternalCallback[");

        buffer.append(_pageName);
        buffer.append('/');

        if (_parameters != null)
        {
            String sep = " ";

            for (int i = 0; i < _parameters.length; i++)
            {
                buffer.append(sep);
                buffer.append(_parameters[i]);

                sep = ", ";
            }
        }

        buffer.append(']');

        return buffer.toString();
    }    
}