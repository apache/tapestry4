package net.sf.tapestry.binding;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.NullValueForBindingException;
import net.sf.tapestry.ReadOnlyBindingException;
import net.sf.tapestry.Tapestry;

/**
 *  Base class for {@link IBinding} implementations.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class AbstractBinding implements IBinding
{
    /**
     *  Cooerces the raw value into a true or false, according to the
     *  rules set by {@link Tapestry#evaluateBoolean(Object)}.
     *
     **/

    public boolean getBoolean()
    {
        return Tapestry.evaluateBoolean(getObject());
    }

    public int getInt()
    {
        Object raw;

        raw = getObject();
        if (raw == null)
            throw new NullValueForBindingException(this);

        if (raw instanceof Number)
        {
            return ((Number) raw).intValue();
        }

        if (raw instanceof Boolean)
        {
            return ((Boolean) raw).booleanValue() ? 1 : 0;
        }

        // Save parsing for last.  This may also throw a number format exception.

        return Integer.parseInt((String) raw);
    }

    public double getDouble()
    {
        Object raw;

        raw = getObject();
        if (raw == null)
            throw new NullValueForBindingException(this);

        if (raw instanceof Number)
        {
            return ((Number) raw).doubleValue();
        }

        if (raw instanceof Boolean)
        {
            return ((Boolean) raw).booleanValue() ? 1 : 0;
        }

        // Save parsing for last.  This may also throw a number format exception.

        return Double.parseDouble((String) raw);
    }

    /**
     *  Gets the value for the binding.  If null, returns null,
     *  otherwise, returns the String (<code>toString()</code>) version of
     *  the value.
     *
     **/

    public String getString()
    {
        Object value;

        value = getObject();
        if (value == null)
            return null;

        return value.toString();
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setBoolean(boolean value)
    {
        throw new ReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setInt(int value)
    {
        throw new ReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setDouble(double value)
    {
        throw new ReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setString(String value)
    {
        throw new ReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setObject(Object value)
    {
        throw new ReadOnlyBindingException(this);
    }

	/**
	 *  Default implementation: returns true.
	 * 
	 *  @since 2.0.3
	 * 
	 **/
	
	public boolean isInvariant()
	{
	    return true;
	}

    public Object getObject(String parameterName, Class type)
    {
        Object result = getObject();

        if (result == null)
            return result;

        if (type.isAssignableFrom(result.getClass()))
            return result;

        String key = type.isInterface() ? "AbstractBinding.wrong-interface" : "AbstractBinding.wrong-type";

        String message = Tapestry.getString(key, parameterName, result, type.getName());

        throw new BindingException(message, this);
    }
}