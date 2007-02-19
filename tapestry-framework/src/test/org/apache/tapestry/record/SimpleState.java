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
package org.apache.tapestry.record;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * Simple object used for testing object property changes.
 */
public class SimpleState implements Serializable
{
    /** serialVersionUID */
    private static final long serialVersionUID = 8466422593705479396L;

    long _id;
    String _name;
    boolean _isValid;
    Date _date;
    Map _keys = new HashMap();
    
    public SimpleState() {}

    public SimpleState(long id, String name)
    {
        _id = id;
        _name = name;
    }
    
    public long getId()
    {
        return _id;
    }
    
    public void setId(long id)
    {
        _id = id;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * @return Returns the isValid.
     */
    public boolean isValid()
    {
        return _isValid;
    }

    /**
     * @param isValid The isValid to set.
     */
    public void setValid(boolean isValid)
    {
        _isValid = isValid;
    }
   
    /**
     * @return Returns the date.
     */
    public Date getDate()
    {
        return _date;
    }
    
    /**
     * @param date The date to set.
     */
    public void setDate(Date date)
    {
        _date = date;
    }
    
    public Map getKeys()
    {
        return _keys;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("id", _id)
        .append("name", _name)
        .append("isValid", _isValid)
        .append("date", _date)
        .toString();
    }
}
