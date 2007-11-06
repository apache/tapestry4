// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.form;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class is a property selection model for an object list. This is used in PropertySelection,
 * MultiplePropertySelection or Palette tapestry components. For example, to use for a Hospital
 * class, and have the labels be the hospital names. <code>
 * List&lt;Hospital&gt; list = ...;
 * return new BeanPropertySelectionModel(hospitals, "name");
 * </code>
 * This will use getName() on the Hospital object, as its display.
 *
 * @author Gabriel Handford
 */
public class BeanPropertySelectionModel implements IPropertySelectionModel, Serializable
{

    /** Comment for <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 3763091973006766644L;
    protected List _list;
    protected String _labelField;
    protected String _nullLabel;

    /**
     * Build an empty property selection model.
     */
    public BeanPropertySelectionModel()
    {
        this(Arrays.asList(new Object[0]), null);
    }

    /**
     * Build a bean property selection model.
     *
     * @param list
     *            The list
     * @param labelField
     *            The label field
     */
    public BeanPropertySelectionModel(List list, String labelField)
    {
        _list = list;
        _labelField = labelField;
    }

    /**
     * Build a bean property selection model.
     *
     * @param c
     *          Collection
     * @param labelField
     *          The label field
     */
    public BeanPropertySelectionModel(Collection c, String labelField)
    {
        _list = new ArrayList(c);
        _labelField = labelField;
    }

    /**
     * Same as {@link #BeanPropertySelectionModel(java.util.List, String)} - with the added
     * functionality of using the specified <code>nullLabel</code> field as a pseudo item in
     * the list of options that stores a null value.   This is useful for situations where you
     * want to present a "Choose.." option or similar invalid option to prompt users for input.
     *
     * @param list
     *          The list of options.
     * @param labelField
     *          The string expression to be used on each object to get the label value
     *          for the option - such as "user.name".
     * @param nullLabel
     *          Will be treated as a pseudo option that always resolves to a null value but
     *          is properly displayed in the options list as the first element.
     */
    public BeanPropertySelectionModel(List list, String labelField, String nullLabel)
    {
        this(list, labelField);

        _nullLabel = nullLabel;
    }

    /**
     * Get the number of options.
     *
     * @return option count
     */
    public int getOptionCount()
    {
        return _nullLabel != null ? _list.size() + 1 : _list.size();
    }

    /**
     * Get the option at index.
     *
     * @param index
     *            Index
     * @return object Object at index
     */
    public Object getOption(int index)
    {
        if (_nullLabel != null && index == 0)
        {
            return null;
        }

        if (_nullLabel != null)
            index--;
        
        if (index > (_list.size() - 1))
        {
            return null;
        }

        return _list.get(index);
    }

    /**
     * Get the label at index.
     *
     * @param index
     *            Index
     * @return label Label at index
     */
    public String getLabel(int index)
    {
        if (index == 0 && _nullLabel != null)
        {
            return _nullLabel;
        }

        if (_nullLabel != null)
            index--;
        
        Object obj = _list.get(index);
        
        try
        {
            return BeanUtils.getProperty(obj, _labelField);
        } catch (Exception e)
        {
            throw new RuntimeException("Error getting property", e);
        }
    }

    /**
     * Get the value at index.
     *
     * @param index
     *            Index
     * @return value Value at index
     */
    public String getValue(int index)
    {
        return String.valueOf(index);
    }

    public boolean isDisabled(int index)
    {
        return index == 0 && _nullLabel != null;
    }

    /**
     * Translate value to object.
     *
     * @param value
     *            Value
     * @return object Object from value
     */
    public Object translateValue(String value)
    {
        if (value == null)
        {
            return null;
        }

        int index = Integer.parseInt(value);
        if (index == 0 && _nullLabel != null)
        {
            return null;
        }

        return getOption(index);
    }
}
