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

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  A component for creating a navigation link to another page, 
 *  using the page service.
 *
 *  [<a href="../../../../../ComponentReference/Page.html">Component Reference</a>]
 *
 * @author Howard Ship
 * @version $Id$
 *
 **/

public class Page extends GestureLink
{
    private String _targetPage;

    /** @since 2.2 **/

    private INamespace _targetNamespace;

    /**
     *  Returns {@link IEngineService#PAGE_SERVICE}.
     *
     **/

    protected String getServiceName()
    {
        return IEngineService.PAGE_SERVICE;
    }

    /**
     *  Returns a single-element String array; the lone element is the
     *  name of the page, retrieved from the 'page' parameter.
     *
     **/

    protected Object[] getServiceParameters(IRequestCycle cycle) throws RequestCycleException
    {
        String parameter = null;
        String prefix = null;

        if (_targetNamespace != null)
            prefix = _targetNamespace.getExtendedId();

        if (prefix != null)
            parameter = prefix + ":" + _targetPage;
        else
            parameter = _targetPage;

        return new String[] { parameter };
    }

    public String getTargetPage()
    {
        return _targetPage;
    }

    public void setTargetPage(String targetPage)
    {
        _targetPage = targetPage;
    }

    /** @since 2.2 **/

    public INamespace getTargetNamespace()
    {
        return _targetNamespace;
    }

    /** @since 2.2 **/

    public void setTargetNamespace(INamespace targetNamespace)
    {
        _targetNamespace = targetNamespace;
    }

}