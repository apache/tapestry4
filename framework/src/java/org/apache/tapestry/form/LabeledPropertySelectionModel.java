// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.form;

/**
 * Decorates an underlying {@link IPropertySelectionModel}adding an initial
 * property. The label, option, and value of the initial property are
 * configurable.
 * 
 * @author Paul Ferraro
 * @since 3.1
 */
public class LabeledPropertySelectionModel implements IPropertySelectionModel
{
    private IPropertySelectionModel _model;
    private String _label = "";
    private Object _option = null;
    private String _value = "";

    /**
     * Constructs a new LabeledPropertySelectionModel using an empty model and
     * default label, option, and value. Default constructor is made available
     * so that this model may be specified as a component helper bean.
     */
    public LabeledPropertySelectionModel()
    {
        this(EMPTY_MODEL);
    }

    /**
     * Constructs a new LabeledPropertySelectionModel using the specified model
     * and default label, option, and value.
     * @param model the underlying model to decorate
     */
    public LabeledPropertySelectionModel(IPropertySelectionModel model)
    {
        _model = model;
    }

    /**
     * Constructs a new LabeledPropertySelectionModel using the specified model
     * and label, and default option and value.
     * @param model the underlying model to decorate
     * @param label the label of the initial property
     */
    public LabeledPropertySelectionModel(IPropertySelectionModel model,
            String label)
    {
        this(model);

        _label = label;
    }

    /**
     * Constructs a new LabeledPropertySelectionModel using the specified model,
     * label, and option; and default value.
     * @param model the underlying model to decorate
     * @param label the label of the initial property
     * @param option the option value of the initial property
     */
    public LabeledPropertySelectionModel(IPropertySelectionModel model,
            String label, Object option)
    {
        this(model, label);

        _option = option;
    }

    /**
     * Constructs a new LabeledPropertySelectionModel using the specified model,
     * label, option, and value.
     * @param model the underlying model to decorate
     * @param label the label of the initial property
     * @param option the option value of the initial property
     * @param value the value of the initial property
     */
    public LabeledPropertySelectionModel(IPropertySelectionModel model,
            String label, Object option, String value)
    {
        this(model, label, option);

        _value = value;
    }

    /**
     * Returns the underlying IPropertySelectionModel
     * @return the underlying IPropertySelectionModel
     */
    public IPropertySelectionModel getModel()
    {
        return _model;
    }
    
    /**
     * Sets the underlying IPropertySelectionModel
     * @param model the IPropertySelectionModel to set 
     */
    public void setModel(IPropertySelectionModel model)
    {
        _model = model;
    }
    
    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
     */
    public int getOptionCount()
    {
        return _model.getOptionCount() + 1;
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
     */
    public Object getOption(int index)
    {
        return (index == 0) ? _option : _model.getOption(index - 1);
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getLabel(int)
     */
    public String getLabel(int index)
    {
        return (index == 0) ? _label : _model.getLabel(index - 1);
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
     */
    public String getValue(int index)
    {
        return (index == 0) ? _value : _model.getValue(index - 1);
    }

    /**
     * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
     */
    public Object translateValue(String value)
    {
        return value.equals(_value) ? _option : _model.translateValue(value);
    }

    /**
     * Returns the label of the initial IPropertySelectionModel option
     * @return a IPropertySelectionModel option label
     */
    public String getLabel()
    {
        return _label;
    }

    /**
     * Sets the label of the initial IPropertySelectionModel option
     * @param label a IPropertySelectionModel option label
     */
    public void setLabel(String label)
    {
        _label = label;
    }

    /**
     * Returns the value of the initial IPropertySelectionModel option
     * @return a IPropertySelectionModel option value
     */
    public String getValue()
    {
        return _value;
    }

    /**
     * Sets the value of the initial IPropertySelectionModel option
     * @param value a IPropertySelectionModel option value
     */
    public void setValue(String value)
    {
        _value = value;
    }

    /**
     * Returns the initial option
     * @return a PropertySelectionModel option
     */
    public Object getOption()
    {
        return _option;
    }

    /**
     * Sets the initial IPropertySelectionModel option
     * @param option a IPropertySelectionModel option
     */
    public void setOption(Object option)
    {
        _option = option;
    }

    /**
     * Empty model implementation. Avoids NullPointerExceptions when default
     * constructor is used.
     */
    private static final IPropertySelectionModel EMPTY_MODEL = new IPropertySelectionModel()
    {
        /**
         * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
         */
        public int getOptionCount()
        {
            return 0;
        }

        /**
         * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
         */
        public Object getOption(int index)
        {
            return null;
        }

        /**
         * @see org.apache.tapestry.form.IPropertySelectionModel#getLabel(int)
         */
        public String getLabel(int index)
        {
            return null;
        }

        /**
         * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
         */
        public String getValue(int index)
        {
            return null;
        }

        /**
         * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
         */
        public Object translateValue(String value)
        {
            return null;
        }
    };
}
