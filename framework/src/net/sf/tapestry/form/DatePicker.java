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
package net.sf.tapestry.form;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;

/**
 * Provides a Form <tt>java.util.Date</tt> field component for selecting dates.
 *
 *  [<a href="../../../../../ComponentReference/DatePicker.html">Component Reference</a>]
 *
 * @author Paul Geerts
 * @author Malcolm Edgar
 * @version $Id$
 * @since 2.2
 * 
 **/

public class DatePicker extends BaseComponent
{
    private IBinding _valueBinding;
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private boolean _disabled;

    public String getFormat()
    {
        return _dateFormat.toPattern();
    }

    public void setFormat(String format)
    {
        _dateFormat = new SimpleDateFormat(format);
    }

    public String getTimeMillis()
    {
        Date date = getValue();

        if (date == null)
            return "";

        return Long.toString(date.getTime());
    }

    public void setTimeMillis(String value)
    {
    }

    public String getText()
    {
        Date date = getValue();

        if (date == null)
            return "";

        return _dateFormat.format(date);
    }

    public void setText(String text)
    {
        if (text.length() >= 6)
        {
            try
            {
                setValue(_dateFormat.parse(text));
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
        return (Date) _valueBinding.getObject();
    }

    public void setValue(Date value)
    {
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

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }        
}
