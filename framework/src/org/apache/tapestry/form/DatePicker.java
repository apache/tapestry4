/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.form;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.html.Body;

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

public abstract class DatePicker extends AbstractFormComponent
{
    private String _name;

    public abstract String getFormat();

    public abstract Date getValue();

    public abstract void setValue(Date value);

    public abstract boolean isDisabled();

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

		updateDelegate(form);
		
        _name = form.getElementId(this);

        String format = getFormat();

        if (format == null)
            format = "dd MMM yyyy";

        DateFormat formatter = new SimpleDateFormat(format);

        boolean disabled = isDisabled();

        if (!cycle.isRewinding())
        {
            Body body = Body.get(cycle);

            IAsset script = getAsset("script");

            body.includeScript(script.buildURL(cycle));

            Date value = getValue();

            writer.beginEmpty("input");
            writer.attribute("type", "text");
            writer.attribute("name", _name);
            writer.attribute("maxlength", format.length());
            writer.attribute("size", format.length());

            if (value != null)
                writer.attribute("value", formatter.format(value));

            if (disabled)
                writer.attribute("disabled", "disabled");

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", _name + "$millis");

            if (value == null)
                writer.attribute("value", "");
            else
                writer.attribute("value", Long.toString(value.getTime()));

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", _name + "$format");
            writer.attribute("value", format);

            writer.beginEmpty("input");
            writer.attribute("type", "button");
            writer.attribute("name", _name + "$button");

            if (disabled)
                writer.attribute("disabled", "disabled");

            writer.attribute("value", "V");
            writer.attribute("onClick", "javascript:goCalendar(this);");
        }

        if (form.isRewinding())
        {
            if (disabled)
                return;

            String textValue = cycle.getRequestContext().getParameter(_name);

            if (Tapestry.isNull(textValue))
                return;

            try
            {
                Date value = formatter.parse(textValue);

                setValue(value);
            }
            catch (ParseException ex)
            {
            }
        }

    }

    public String getName()
    {
        return _name;
    }

}
