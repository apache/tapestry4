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


/**
 *  Component of the {@link Inspector} page used control log4j logging
 *  behavior.
 *
 *  @version $Id$
 *  @author Howard Ship
 *  @since 0.2.9
 */
 
package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.valid.*;
import java.util.*;
import org.apache.log4j.*;

public class ShowLogging extends BaseComponent
implements ILifecycle
{
	private List categories;
	private String error;
	private String newCategory;
	private IValidationDelegate validationDelegate;
	private IPropertySelectionModel rootPriorityModel;
	private IPropertySelectionModel priorityModel;

	public void reset()
	{
		categories = null;
		error = null;
		newCategory = null;
	}

	private static class CategoryComparator 
	implements Comparator
	{
		public int compare(Object left,
			Object right)
		{
			Category cLeft = (Category)left;
			Category cRight = (Category)right;

			return cLeft.getName().compareTo(cRight.getName());
		}
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

	/**
	 *  Returns a {@link List} of {@link Category categories}, ordered by
	 *  category name.
	 *
	 */

	public List getCategories()
	{
		if (categories == null)
			categories = buildCategories();

		return categories;	
	}

	private List buildCategories()
	{
		List result = new ArrayList();
		Enumeration e;

		e = Category.getCurrentCategories();
		while (e.hasMoreElements())
		{
			result.add(e.nextElement());
		}

		Collections.sort(result, new CategoryComparator());

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

	public IActionListener getPriorityFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			throws RequestCycleException
			{
				// Do nothing.  This will redisplay the logging page after the
				// priorities are updated.
			}
		};
	}

	public IActionListener getNewCategoryFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			throws RequestCycleException
			{
				processNewCategory();
			}
		};
	}

	private void processNewCategory()
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

		// Force a refresh on the list of categories, now that we've
		// added a new one.

		categories = null;

		// Clear the field
		newCategory = null;
		field.refresh();

	}
}
