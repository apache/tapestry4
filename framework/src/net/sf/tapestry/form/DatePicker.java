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
import java.text.ParseException;
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
 *  [<a href="../../../../../ComponentReference/DatePicker.html">Component Reference</a>]
 *
 * @author Paul Geertz
 * @author Malcolm Edgar
 * @version $Id$
 */
public class DatePicker extends BaseComponent
{
    private static final SimpleDateFormat JS_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
    private static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String FIRST_USE_ATTRIBUTE_KEY = "net.sf.tapestry.DatePicker-first";

    private Date _value = new Date(System.currentTimeMillis());
    private IBinding _valueBinding;
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private boolean _firstTime;

    public String getFormat()
    {
        return _dateFormat.toPattern();
    }

    public void setFormat(String format)
    {
        _dateFormat = new SimpleDateFormat(format);
    }

    /**
     *  Return a JavaScript Date format <tt>String</tt> value "MMM, dd, yyyy"
     * 
     *  @return a JavaScript Date format <tt>String</tt> value []
     * 
     **/
    public String getJavaScriptValue()
    {
        return (_value != null) ? JS_DATE_FORMAT.format(_value) : "";
    }

    public void setJavaScriptValue(String v)
    {        
    }

    public String getText()
    {
        return (_value != null) ? _dateFormat.format(_value) : "";
    }

    public void setText(String text)
    {
        if (text.length() >= 6) 
        {
            try 
            {
                java.util.Date date = _dateFormat.parse(text);    
                setValue(Date.valueOf(SQL_DATE_FORMAT.format(date)));
            }
            catch (ParseException pe)
            {
                setValue(null);    
            }
        }
        else
        {
            setValue(null);    
        }
    }

    public Date getValue()
    {
        return _value;
    }

    public void setValue(Date value)
    {
        _value = value;
        _valueBinding.setObject(value);
    }

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    /**
     *  <tt>isFirstTime</tt> checks if this is the first time
     *  the component is rendered on the page (this affects the
     *  client-side JavaScript emitted to support the component; subsequent
     *  instances of the component share resources created by the
     *  first instance).
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
        _firstTime = (cycle.getAttribute(FIRST_USE_ATTRIBUTE_KEY) == null);

        if (_firstTime)
            cycle.setAttribute(FIRST_USE_ATTRIBUTE_KEY, Boolean.TRUE);
            
        super.prepareForRender(cycle);
    }
}
