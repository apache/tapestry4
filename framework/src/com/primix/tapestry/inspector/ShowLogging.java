/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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


package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.valid.*;
import java.util.*;
import org.apache.log4j.*;

/**
 *  Component of the {@link Inspector} page used control log4j logging
 *  behavior.
 *
 *  @version $Id$
 *  @author Howard Ship
 *  @since 0.2.9
 */

public class ShowLogging extends BaseComponent
	implements ILifecycle
{
	private Category category;
	private String error;
	private String newCategory;
	private IValidationDelegate validationDelegate;
	private IPropertySelectionModel rootPriorityModel;
	private IPropertySelectionModel priorityModel;
	
	public void reset()
	{
		category = null;
		error = null;
		newCategory = null;
	}
	
	private class ValidationDelegate
		extends BaseValidationDelegate
	{
		public void invalidField(IValidatingTextField field,
				ValidationConstraint constraint,
				String defaultErrorMessage)
		{
			if (error == null)
				error = defaultErrorMessage;
		}
		
		public void writeErrorSuffix(IValidatingTextField field,
				IResponseWriter writer,
				IRequestCycle cycle)
		{
			writer.begin("span");
			writer.attribute("class", "error");
			writer.print("**");
			writer.end();
		}
		
	}
	
	public String getError()
	{
		return error;
	}
	
	public void setError(String value)
	{
		error = value;
	}
	
	public String getNewCategory()
	{
		return newCategory;
	}
	
	public void setNewCategory(String value)
	{
		newCategory = value;
	}
	
	public void setCategoryName(String value)
	{
		category = Category.getInstance(value);
	}
	
	public Category getCategory()
	{
		return category;
	}
	
	/**
	 *  Returns a sorted list of all known categorys.
	 *
	 */
	
	public List getCategoryNames()
	{
		List result = new ArrayList();
		Enumeration e;
		
		e = Category.getCurrentCategories();
		while (e.hasMoreElements())
		{
			Category c = (Category)e.nextElement();
			
			result.add(c.getName());
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public Category getRootCategory()
	{
		return Category.getRoot();
	}
	
	/**
	 *  Returns a {@link IPropertySelectionModel} for {@link Priority}
	 *  that does not allow a null value to be selected.
	 *
	 */
	
	public IPropertySelectionModel getRootPriorityModel()
	{
		if (rootPriorityModel == null)
			rootPriorityModel = new PriorityModel(false);
		
		return rootPriorityModel;	
	}
	
	/**
	 *  Returns a {@link IPropertySelectionModel} for {@link Priority}
	 *  include a null option.
	 *
	 */
	
	public IPropertySelectionModel getPriorityModel()
	{
		if (priorityModel == null)
			priorityModel = new PriorityModel();
		
		return priorityModel;	
	}
	
	public IValidationDelegate getValidationDelegate()
	{
		if (validationDelegate == null)
			validationDelegate = new ValidationDelegate();
		
		return validationDelegate;
	}	
	
	public void priorityChange(IRequestCycle cycle)
	{
		// Do nothing.  This will redisplay the logging page after the
		// priorities are updated.
	}
	
	public void addNewCategory(IRequestCycle cycle)
	{
		// If the validating text field has an error, then go no further.
		
		if (error != null)
			return;
		
		IValidatingTextField field =
			(IValidatingTextField)getComponent("inputNewCategory");
		
		if (Category.exists(newCategory) != null)
		{
			error = "Category " + newCategory + " already exists.";
			field.setError(true);
			return;
		}
		
		// Force the new category into existence
		
		Category.getInstance(newCategory);
		
		// Clear the field
		newCategory = null;
		field.refresh();
		
	}
}
