/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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