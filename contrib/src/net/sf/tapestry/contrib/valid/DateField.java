/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.contrib.valid;

import com.primix.tapestry.*;

import com.primix.tapestry.form.*;
import net.sf.tapestry.valid.DateValidator;
import net.sf.tapestry.valid.IValidator;
import net.sf.tapestry.valid.ValidField;
import net.sf.tapestry.valid.ValidatorException;
import com.primix.tapestry.event.*;

import net.sf.tapestry.*;

import java.util.*;
import java.text.*;

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
 *  </table>
 *
 *  <p>Informal parameters are allowed.  A body is not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 *  @see ValidField
 */

public class DateField extends ValidField
{
	private IBinding minimumBinding;
	private IBinding maximumBinding;
	private IBinding requiredBinding;
	private IBinding formatBinding;

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
			Date minimum = (Date)minimumBinding.getObject("minimum", Date.class);
			validator.setMinimum(minimum);
		}
		
		if (maximumBinding != null)
		{
			Date maximum = (Date)maximumBinding.getObject("maximum", Date.class);
			validator.setMaximum(maximum);
		}
		
		if (requiredBinding != null)
		{
			boolean required = requiredBinding.getBoolean();
			validator.setRequired(required);
		}
		
		if (formatBinding != null)
		{
			DateFormat format = (DateFormat)formatBinding.getObject("format", DateFormat.class);
			validator.setFormat(format);
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

}