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

package net.sf.tapestry.link;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.engine.ExternalService;

/**
 *  A component for creating a link to {@link IExternalPage} using the 
 * {@link ExternalService}.
 *
 *  [<a href="../../../../../ComponentReference/ExternalLink.html">Component Reference</a>]
 *
 * @see IExternalPage
 * @see ExternalService
 *
 * @author Malcolm Edgar
 * @version $Id$
 *
 **/

public class ExternalLink extends GestureLink
{
    private Object _parameters;

    private String _targetPage;

    /**
     *  Returns {@link IEngineService#EXTERNAL_SERVICE}.
     *
     **/

    protected String getServiceName()
    {
        return IEngineService.EXTERNAL_SERVICE;
    }

    protected Object[] getServiceParameters(IRequestCycle cycle)
    {
        Object[] serviceParameters = null;
        
        if (_parameters == null) 
        {
            serviceParameters = new Object[] { _targetPage };
        } 
        else  if (_parameters instanceof Object[]) 
        {
            Object[] parameters = (Object[]) _parameters;
            
            serviceParameters =  new Object[parameters.length + 1];           
            serviceParameters[0] = _targetPage;
    
            System.arraycopy(parameters, 0, serviceParameters, 1, parameters.length);
        } 
        else if (_parameters instanceof List)
        {
            List serviceList = new ArrayList();
            serviceList.add(_targetPage);
            serviceList.addAll((List) _parameters);
            serviceParameters = serviceList.toArray();
        }
        else 
        {
            serviceParameters = new Object[] { _targetPage, _parameters };
        }

        return serviceParameters;        
    }

    public Object getParameters()
    {
        return _parameters;
    }

    public void setParameters(Object parameters)
    {
        _parameters = parameters;
    }

    public String getTargetPage()
    {
        return _targetPage;
    }

    public void setTargetPage(String targetPage)
    {
        _targetPage = targetPage;
    }

}

