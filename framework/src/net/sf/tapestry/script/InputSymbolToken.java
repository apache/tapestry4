package net.sf.tapestry.script;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;

/**
 *  A token that validates that an input symbol exists or is of a
 *  declared type.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

class InputSymbolToken extends AbstractToken
{
    private String _key;
    private Class _class;
    private boolean _required;

    InputSymbolToken(String key, Class clazz, boolean required)
    {
        _key = key;
        _class = clazz;
        _required = required;
    }

    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        Object value = session.getSymbols().get(_key);

        if (_required && value == null)
            throw new ScriptException(Tapestry.getString("InputSymbolToken.required", _key), session);

        if (value != null && _class != null && !_class.isAssignableFrom(value.getClass()))
            throw new ScriptException(
                Tapestry.getString("InputSymbolToken.wrong-type", _key, value.getClass().getName(), _class.getName()),
                session);
    }

}
