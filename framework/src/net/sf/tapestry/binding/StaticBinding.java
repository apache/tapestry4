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