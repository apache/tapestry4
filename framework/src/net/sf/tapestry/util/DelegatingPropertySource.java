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
