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

package net.sf.tapestry.inspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.valid.IField;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.valid.ValidationDelegate;
import net.sf.tapestry.valid.ValidatorException;
import org.apache.log4j.Category;

/**
 *  Component of the {@link Inspector} page used control log4j logging
 *  behavior.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 **/

public class ShowLogging extends BaseComponent implements PageDetachListener
{
    private Category category;
    private String newCategory;
    private IValidationDelegate validationDelegate;
    private IPropertySelectionModel rootPriorityModel;
    private IPropertySelectionModel priorityModel;

    /**
     *  Registers this component as a {@link PageDetachListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        getPage().addPageDetachListener(this);
    }

    /**
     *  Clears out everything when the page is detached.
     *
     *  @since 1.0.5
     *
     **/

    public void pageDetached(PageEvent event)
    {
        category = null;
        newCategory = null;
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
     **/

    public List getCategoryNames()
    {
        List result = new ArrayList();
        Enumeration e;

        e = Category.getCurrentCategories();
        while (e.hasMoreElements())
        {
            Category c = (Category) e.nextElement();

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
     *  Returns a {@link IPropertySelectionModel} for {@link org.apache.log4j.Priority}
     *  that does not allow a null value to be selected.
     *
     **/

    public IPropertySelectionModel getRootPriorityModel()
    {
        if (rootPriorityModel == null)
            rootPriorityModel = new PriorityModel(false);

        return rootPriorityModel;
    }

    /**
     *  Returns a {@link IPropertySelectionModel} for 
     *  {@link org.apache.log4j.Priority}
     *  include a null option.
     *
     **/

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

        IValidationDelegate delegate =
            (IValidationDelegate) getBeans().getBean("delegate");

        if (delegate.getHasErrors())
            return;

        if (Category.exists(newCategory) == null)
        {
            // Force the new category into existence

            Category.getInstance(newCategory);
            // Clear the field
            newCategory = null;

            return;
        }

        IField field = (IField) getComponent("inputNewCategory");

        delegate.setFormComponent(field);
        delegate.record(
            new ValidatorException(
                "Category " + newCategory + " already exists.",
                null,
                newCategory));

    }
}