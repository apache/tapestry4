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
package org.apache.tapestry.form;


/**
 * Implementation of a property model that works off of native
 * java enum types.
 * 
 * <p>
 *  The enum label/values are all translated by calling Enum.toString() on your individual 
 *  enum types, so it may be a good idea to provide a toString() method in your Enums if the
 *  types aren't what you would prefer to display.
 * </p>
 */
public class EnumPropertySelectionModel implements IPropertySelectionModel
{
    private Enum[] _set;
    
    public EnumPropertySelectionModel(Enum[] set)
    {
        _set = set;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getLabel(int index)
    {
        if (index == 0)
            return "None";
        
        return _set[index - 1].toString();
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getOption(int index)
    {
        if (index == 0)
            return null;
        
        return _set[index - 1];
    }

    /**
     * {@inheritDoc}
     */
    public int getOptionCount()
    {
        return _set.length + 1;
    }

    /**
     * {@inheritDoc}
     */
    public String getValue(int index)
    {
        if (index == 0)
            return "None";
        
        return _set[index - 1].toString();
    }
    
    public boolean isDisabled(int index)
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object translateValue(String value)
    {
        for (Enum e : _set) {
            if (e.toString().equals(value))
                return e;
        }
        
        return null;
    }
}
