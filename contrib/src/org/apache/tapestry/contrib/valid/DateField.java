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

package org.apache.tapestry.contrib.valid;

import java.text.DateFormat;
import java.util.Date;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.valid.DateValidator;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.ValidField;

/**
 *
 *  Backwards compatible version of the 1.0.7 DateField component.
 *
 * <table border=1>
 * <tr>
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td>
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *      <td>date</td>
 *      <td>java.util.Date</td>
 *      <td>R / W</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The date property to edit.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>required</td>
 *      <td>boolean</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>no</td>
 *      <td>If true, then a value must be entered.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>minimum</td>
 *      <td>java.util.Date</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>If provided, the date entered must be equal to or later than the
 *  provided minimum date.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>maximum</td>
 *      <td>java.util.Date</td>
 *      <td>R</td>
 *      <td>no</td>
 *		<td>&nbsp;</td>
 *      <td>If provided, the date entered must be less than or equal to the
 *  provided maximum date.</td>
 * </tr>
 *
 *  <tr>
 *      <td>displayName</td>
 *      <td>String</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>A textual name for the field that is used when formulating error messages.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *		<td>format</td>
 *		<td>{@link DateFormat}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>Default format <code>MM/dd/yyyy</code></td>
 *		<td>The format used to display and parse dates.</td>
 *	</tr>
 *
 *  <tr>
 *		<td>displayFormat</td>
 *		<td>{@link String}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td><code>MM/DD/YYYY</code></td>
 *		<td>The format string presented to the user if the date entered is in an 
 *   incorrect format. e.g. the format object throws a ParseException.</td>
 *	</tr>
 *
 *  </table>
 *
 *  <p>Informal parameters are allowed.  A body is not allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 *  @see ValidField
 * 
 **/

public abstract class DateField extends ValidField
{
	public abstract IBinding getValueBinding();
	public abstract void setValueBinding(IBinding valueBinding);
	
    private IBinding minimumBinding;
    private IBinding maximumBinding;
    private IBinding requiredBinding;
    private IBinding formatBinding;
    private IBinding displayFormatBinding;

    /** Returns the valueBinding. **/

    public IBinding getDateBinding()
    {
        return getValueBinding();
    }

    /** Updates the valueBinding. **/

    public void setDateBinding(IBinding value)
    {
        setValueBinding(value);
    }

    /**
     *  Overrides {@link ValidField#getValidator()} to construct a validator
     *  on-the-fly.
     * 
     **/

    public IValidator getValidator()
    {
        DateValidator validator = new DateValidator();

        if (minimumBinding != null)
        {
            Date minimum = (Date) minimumBinding.getObject("minimum", Date.class);
            validator.setMinimum(minimum);
        }

        if (maximumBinding != null)
        {
            Date maximum = (Date) maximumBinding.getObject("maximum", Date.class);
            validator.setMaximum(maximum);
        }

        if (requiredBinding != null)
        {
            boolean required = requiredBinding.getBoolean();
            validator.setRequired(required);
        }

        if (formatBinding != null)
        {
            DateFormat format =
                (DateFormat) formatBinding.getObject("format", DateFormat.class);
            validator.setFormat(format);
        }

        if (displayFormatBinding != null)
        {
            String displayFormat =
                (String) displayFormatBinding.getObject("displayFormat", String.class);
            validator.setDisplayFormat(displayFormat);
        }

        return validator;
    }

    public IBinding getRequiredBinding()
    {
        return requiredBinding;
    }

    public void setRequiredBinding(IBinding requiredBinding)
    {
        this.requiredBinding = requiredBinding;
    }

    public IBinding getFormatBinding()
    {
        return formatBinding;
    }

    public void setFormatBinding(IBinding formatBinding)
    {
        this.formatBinding = formatBinding;
    }
    
    public IBinding getDisplayFormatBinding()
    {
    	return displayFormatBinding;
    }
    
    public void setDisplayFormatBinding(IBinding displayFormatBinding)
    {
    	this.displayFormatBinding = displayFormatBinding;
    }
    
    public IBinding getMinimumBinding() {
        return minimumBinding;
    }
    
    public void setMinimumBinding(IBinding value)
    {
        this.minimumBinding = value;
    }
    
    public IBinding getMaximumBinding()
    {
        return maximumBinding;
    }
    
    public void setMaximumBinding(IBinding value)
    {
        this.maximumBinding = value;
    }

}