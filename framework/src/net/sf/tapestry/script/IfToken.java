package net.sf.tapestry.script;

import java.util.Map;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  A conditional portion of the generated script.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.1
 *
 **/

class IfToken extends AbstractToken
{
    private boolean _condition;
    private String _expression;
    private IResourceResolver _resolver;

    IfToken(boolean condition, String expression, IResourceResolver resolver)
    {
        _condition = condition;
        _expression = expression;
        _resolver = resolver;
    }

    private boolean evaluate(ScriptSession session)
    {
        Map symbols = session.getSymbols();

        Object value = OgnlUtils.get(_expression, _resolver, symbols);

        return Tapestry.evaluateBoolean(value);
    }

    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        if (evaluate(session) == _condition)
            writeChildren(buffer, session);
    }
}