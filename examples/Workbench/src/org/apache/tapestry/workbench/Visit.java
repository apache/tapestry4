//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.workbench;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Visit implements Serializable
{
    /**
     *  The name of the page for which the corresponding tab
     *  should be visibly active.
     *
     **/

    private String _activeTabName = "Home";

    /**
     *  If true, then a detailed report about the request is appended
     *  to the bottom of each page.
     *
     **/

    private boolean _requestDebug;


    private boolean _disableInspector;

    /**
     *  Used by the Fields demo page.
     *
     **/

    private Integer _intValue;
    private Double _doubleValue;
    private BigDecimal _bigDecimalValue;
    private Long _longValue;
    private Date _dateValue;
    private String _stringValue;
    private String _emailValue;

    public String getActiveTabName()
    {
        return _activeTabName;
    }

    public void setActiveTabName(String value)
    {
        _activeTabName = value;
    }

    public void setRequestDebug(boolean value)
    {
        _requestDebug = value;
    }

    public boolean getRequestDebug()
    {
        return _requestDebug;
    }

    public void setIntValue(Integer value)
    {
        _intValue = value;
    }

    public Integer getIntValue()
    {
        return _intValue;
    }

    public BigDecimal getBigDecimalValue()
    {
        return _bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal value)
    {
        _bigDecimalValue = value;
    }

    public Double getDoubleValue()
    {
        return _doubleValue;
    }

    public void setDoubleValue(Double value)
    {
        _doubleValue = value;
    }

    public Long getLongValue()
    {
        return _longValue;
    }

    public void setLongValue(Long value)
    {
        _longValue = value;
    }

    public Date getDateValue()
    {
        return _dateValue;
    }

    public void setDateValue(Date value)
    {
        _dateValue = value;
    }

    public String getStringValue()
    {
        return _stringValue;
    }

    public void setStringValue(String value)
    {
        _stringValue = value;
    }
    
    public String getEmailValue()
    {
        return _emailValue;
    }

    public void setEmailValue(String value)
    {
        _emailValue = value;
    }
    
    public boolean getDisableInspector()
    {
        return _disableInspector;
    }

    public void setDisableInspector(boolean disableInspector)
    {
        _disableInspector = disableInspector;
    }

}