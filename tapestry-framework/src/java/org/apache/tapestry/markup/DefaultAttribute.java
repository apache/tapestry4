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
package org.apache.tapestry.markup;

import java.io.PrintWriter;


/**
 * Used to hold markup attribute data for writing in a specific format.
 * 
 * @author jkuhnert
 */
public class DefaultAttribute implements Attribute
{
    private String _value;
    private boolean _raw;
    
    public DefaultAttribute(String value, boolean raw)
    {
        _value = value;
        _raw = raw;
    }
    
    public String getValue()
    {
        return _value;
    }
    
    void append(Object value)
    {
        if (value == null)
            return;
        
        _value += " " + value;
    }
    
    void setRaw(boolean raw)
    {
        _raw = raw;
    }
    
    public boolean isRaw()
    {
        return _raw;
    }
    
    void print(String name, PrintWriter writer, MarkupFilter filter)
    {
        writer.print(' ');
        writer.print(name);
        writer.print("=\"");
        
        if (_raw && _value != null) {
            
            writer.write(_value);
            
        } else if (_value != null) {
            
            char[] data = _value.toCharArray();
            filter.print(writer, data, 0, data.length, true);
        }
        
        writer.print('"');
    }
    
    public String toString()
    {
        return _value;
    }
}
