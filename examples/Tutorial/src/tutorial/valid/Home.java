/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package tutorial.valid;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.valid.*;
import com.primix.tapestry.util.*;
import java.math.*;
import net.sf.tapestry.contrib.palette.*;

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class Home extends BasePage
{
	private String error;
	private IValidationDelegate delegate;
	
	public static final int MIN_INT = 5;
	public static final int MAX_INT = 30;
	
	public static final double MIN_DOUBLE = 3.14;
	public static final double MAX_DOUBLE = 202.54;
	
	public static final BigDecimal MIN_DECIMAL = new BigDecimal(22. / 7.);
	public static final BigDecimal MAX_DECIMAL = new BigDecimal("2001");
	
	public static final BigInteger MIN_BIGINT = new BigInteger("5");
	public static final BigInteger MAX_BIGINT = new BigInteger("9745789589234578979");
	
	private SortMode sort = SortMode.NONE;
	
	private IPropertySelectionModel sortModel;
	
	private class PrivateDelegate extends BaseValidationDelegate
	{
		/**
		 *  Updates the page's error property to the provided error message.
		 *  Only the first invocation (per request cycle) matters, the others
		 *  are ignored.
		 *
		 */
		
		public void invalidField(IValidatingTextField field, 
				ValidationConstraint contraint,
				String defaultErrorMessage)
		{
			if (error == null)
				error = defaultErrorMessage;
		}
		
	}
	
	public void detach()
	{
		error = null;
		sort = SortMode.NONE;
		
		super.detach();
	}
	
	public String getError()
	{
		return error;
	}
	
	public void setError(String value)
	{
		error = value;
	}
	
	public IValidationDelegate getDelegate()
	{
		if (delegate == null)
			delegate = new PrivateDelegate();
		
		return delegate;
	}
	
	public void formListener(IRequestCycle cycle)
	{
		if (error == null)
			cycle.setPage("Show");
	}
	
	private IPropertySelectionModel colorModel;
	
	private String[] colors = 
	{
		"Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet"
	};
	
	public IPropertySelectionModel getColorModel()
	{
		if (colorModel == null)
			colorModel = new StringPropertySelectionModel(colors);
		
		return colorModel;
	}
	
	public void setSort(SortMode value)
	{
		sort = value;
		
		fireObservedChange("sort", value);
	}
	
	public SortMode getSort()
	{
		return sort;
	}
	
	public IPropertySelectionModel getSortModel()
	{
		if (sortModel == null)
		{
			ResourceBundle bundle = 
				ResourceBundle.getBundle("tutorial.valid.SortModeStrings",
					getLocale());
			
			Enum[] options = new Enum[]
			{
				SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER
			};
			
			sortModel = new EnumPropertySelectionModel(options, bundle);
		}
	
		return sortModel;
	}
}
