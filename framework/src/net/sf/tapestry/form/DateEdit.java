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

package net.sf.tapestry.form;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 * Provides a Form <tt>java.sql.Date</tt> field component for selecting dates.
 *
 *  [<a href="../../../../../ComponentReference/DateEdit.html">Component Reference</a>]
 *
 * @author Paul Geertz
 * @author Malcolm Edgar
 * @version $Id$
 */
public class DateEdit extends BaseComponent {
    
    private static final String CLASSNAME = DateEdit.class.getName();

    private Date _value = new Date((new java.util.Date()).getTime());
    private IBinding _valueBinding;
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private boolean _firstTime;

    public IBinding getValueBinding() 
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding value) 
    {
        _valueBinding = value;
    }

    public Date readValue() 
    {
        return (Date) (_valueBinding.getObject());
    }

    public void updateValue(Date value) 
    {
        _valueBinding.setObject(value);
    }

    public String getText() 
    {
        return _dateFormat.format(_value);
    }

    public void setText(String text) 
    {
        // ignore
    }

    /**
     *  @return a <tt>String</tt> long value for the date
     * 
     **/
    public String getLongValue() 
    {
        return "" + _value.getTime();
    }

    /**
     *  <tt>setLongValue</tt> sets the long value of the date
     *
     *  @param v a <tt>String</tt> value
     * 
     **/
    public void setLongValue(String v) {
        setValue(new Date(Long.parseLong(v)));
    }

    public Date getValue() 
    {
        return _value;
    }

    public void setValue(Date value) 
    {
        _value = value;
        updateValue(_value);
    }

    public void setFormat(String format) 
    {
        _dateFormat = new SimpleDateFormat(format);
    }

    public String getFormat() 
    {
        return _dateFormat.toPattern();
    }

    /**
     *  <tt>isFirstTime</tt> checks if this is the first time
     *  the component is in the page
     *
     *  @return a <tt>boolean</tt> value
     * 
     **/
    public boolean isFirstTime() 
    {
        return _firstTime;        
    }

    protected void prepareForRender(IRequestCycle cycle) throws RequestCycleException 
    {
        if (cycle.getAttribute(CLASSNAME) == null) 
        {
            cycle.setAttribute(CLASSNAME, Boolean.TRUE);
            _firstTime = true;
        }        
        super.prepareForRender(cycle);
    }
}
