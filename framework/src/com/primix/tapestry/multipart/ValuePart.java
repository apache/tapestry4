/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship 
 *
 * mailto:hship@users.sf.net
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.primix.tapestry.multipart;

import java.io.*;
import java.util.*;

import com.primix.tapestry.IUploadFile;

/**
 *  A portion of a multipart request that stores a value, or values, for
 *  a parameter.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 *
 **/

public class ValuePart implements IPart
{
    private int count;
    // Stores either String or List of String
    private Object value;
    
    public ValuePart(String value)
    {
        count = 1;
        this.value = value;
    }
    
    public int getCount()
    {
        return count;
    }
    
    /**
     *  Returns the value, or the first value (if multi-valued).
     * 
     **/
    
    public String getValue()
    {
        if (count == 1)
            return (String)value;
        
        List l = (List)value;
        
        return (String)l.get(0);
    }
    
    /**
     *  Returns the values as an array of strings.  If there is only one value,
     *  it is returned wrapped as a single element array.
     * 
     **/
    
    public String[] getValues()
    {
        if (count == 1)
            return new String[] { (String)value };
            
        List l = (List)value;
        
        return (String[]) l.toArray(new String[count]);
    }
    
    public void add(String newValue)
    {
        if (count == 1)
        {
            List l = new ArrayList();
            l.add(value);
            l.add(newValue);
            
            value = l;
            count++;
            return;
        }
        
        List l = (List)value;
        l.add(newValue);
        count++;
    }
    
    /**
     *  Does nothing.
     * 
     **/
    
    public void cleanup()
    {
    }
}
