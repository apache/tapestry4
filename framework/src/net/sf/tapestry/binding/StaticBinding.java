package net.sf.tapestry.binding;

/**
 * Stores a static (invariant) String as the value.
 *
 * <p>It may be useful to cache static bindings the way {@link FieldBinding}s are cached.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class StaticBinding extends AbstractBinding
{
    private String _value;
    private boolean _parsedInt;
    private int _intValue;
    private boolean _parsedDouble;
    private double _doubleValue;

    public StaticBinding(String value)
    {
        this._value = value;
    }

    /**
     *  Interprets the static value as an integer.
     *
     **/

    public int getInt()
    {
        if (!_parsedInt)
        {
            _intValue = Integer.parseInt(_value);
            _parsedInt = true;
        }

        return _intValue;
    }

    /**
     *  Interprets the static value as a double.
     *
     **/

    public double getDouble()
    {
        if (!_parsedDouble)
        {
            _doubleValue = Double.parseDouble(_value);
            _parsedDouble = true;
        }

        return _doubleValue;
    }

    public String getString()
    {
        return _value;
    }

    public Object getObject()
    {
        return _value;
    }

    public String toString()
    {
        return "StaticBinding[" + _value + "]";
    }
}