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

package tutorial.workbench;

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