package net.sf.tapestry.script;

import java.util.Map;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  A token that writes the value of a property using a property path
 *  routed in the symbols..
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class InsertToken implements IScriptToken
{
    private String _expression;
    private IResourceResolver _resolver;

    InsertToken(String expression, IResourceResolver resolver)
    {
        _expression = expression;
        _resolver = resolver;
    }

    /**
     *  Gets the named symbol from the symbols {@link Map}, verifies that
     *  it is a String, and writes it to the {@link Writer}.
     *
     **/

    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        Map symbols = session.getSymbols();

        Object value = OgnlUtils.get(_expression, _resolver, symbols);

        if (value != null)
            buffer.append(value);
    }

    public void addToken(IScriptToken token)
    {
        // Should never be invoked.
    }

}