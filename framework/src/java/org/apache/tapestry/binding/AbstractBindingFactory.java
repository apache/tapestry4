package org.apache.tapestry.binding;

import org.apache.tapestry.coerce.ValueConverter;

/**
 * @author Howard M. Lewis Ship
 */
public abstract class AbstractBindingFactory implements BindingFactory
{

    private ValueConverter _valueConverter;

    public ValueConverter getValueConverter()
    {
        return _valueConverter;
    }

    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }

}