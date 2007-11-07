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
 * This class is a property selection model for an object list. This is used in {@link PropertySelection},
 * MultiplePropertySelection or Palette tapestry components. For example, to use for a Hospital
 * class, and have the labels be the hospital names.
 *
 * <p>
 * <code>
 * List&lt;Hospital&gt; list = ...;
 * return new BeanPropertySelectionModel(hospitals, "name");
 * </code>
 * </p>
 * <p>This will use getName() on the Hospital object, as its display.</p>
 *
 * @author Gabriel Handford
 */
public class BeanPropertySelectionModel implements IPropertySelectionModel, Serializable
{

    /** Comment for <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 3763091973006766644L;
    protected List _list;
    protected String _labelField;

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
     * Get the number of options.
     *
     * @return option count
     */
    public int getOptionCount()
    {
        return _list.size();
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
        return false;
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

        return getOption( Integer.parseInt(value));
    }

    public String toString()
    {
        return "BeanPropertySelectionModel[" +
               "_list=" + _list +
               '\n' +
               ", _labelField='" + _labelField + '\'' +
               '\n' +
               ']';
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BeanPropertySelectionModel)) return false;

        BeanPropertySelectionModel that = (BeanPropertySelectionModel) o;

        if (_labelField != null ? !_labelField.equals(that._labelField) : that._labelField != null) return false;
        if (_list != null ? !_list.equals(that._list) : that._list != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (_list != null ? _list.hashCode() : 0);
        result = 31 * result + (_labelField != null ? _labelField.hashCode() : 0);
        return result;
    }
}
