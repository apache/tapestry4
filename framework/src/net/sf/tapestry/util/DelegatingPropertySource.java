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
package net.sf.tapestry.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.IPropertySource;

/**
 *  An implementation of {@link net.sf.tapestry.IPropertySource}
 *  that delegates to a list of other implementations.  This makes
 *  it possible to create a search path for property values.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class DelegatingPropertySource implements IPropertySource
{
    private List _sources = new ArrayList();
    
    public DelegatingPropertySource()
    {
    }
    
    public DelegatingPropertySource(IPropertySource delegate)
    {
        addSource(delegate);
    }
    
    /**
     *  Adds another source to the list of delegate property sources.
     *  This is typically only done during initialization
     *  of this DelegatingPropertySource.
     * 
     **/
    
    public void addSource(IPropertySource source)
    {
        _sources.add(source);
    }
    
    /**
     *  Re-invokes the method on each delegate property source, 
     *  in order, return the first non-null value found.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        String result = null;
        int count = _sources.size();
        
        for (int i = 0; i < count; i++)
        {
            IPropertySource source = (IPropertySource)_sources.get(i);
            
            result = source.getPropertyValue(propertyName);
            
            if (result != null)
                break;
        }
        
        return result;        
    }

}
