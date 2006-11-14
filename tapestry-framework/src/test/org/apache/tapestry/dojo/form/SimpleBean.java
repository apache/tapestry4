// Copyright Jul 30, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;


/**
 * Simple bean style class to test {@link DefaultAutocompleteModel}.
 * 
 * @author jkuhnert
 */
public class SimpleBean
{

    private Integer _id;
    
    private String _label;
    
    private int _value;
    
    public SimpleBean(Integer id, String label, int value)
    {
        _id = id;
        _label = label;
        _value = value;
    }
    
    /**
     * @return the id
     */
    public Integer getId()
    {
        return _id;
    }
    
    /**
     * @return the label
     */
    public String getLabel()
    {
        return _label;
    }
    
    /**
     * Returns the value stored.
     * 
     * @return The value stored.
     */
    public int getValue()
    {
        return _value;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((_id == null) ? 0 : _id.hashCode());
        return result;
    }
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SimpleBean other = (SimpleBean) obj;
        if (_id == null) {
            if (other._id != null) return false;
        } else if (!_id.equals(other._id)) return false;
        return true;
    }
}
