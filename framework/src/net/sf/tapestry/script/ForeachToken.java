package net.sf.tapestry.script;

import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  A looping operator, modeled after the Foreach component.  It takes
 *  as its source as property and iterates through the values, updating
 *  a symbol on each pass.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.1
 * 
 **/

class ForeachToken extends AbstractToken
{
    private IResourceResolver _resolver;
    private String _key;
    private String _expression;

    ForeachToken(String key, String expression, IResourceResolver resolver)
    {
        _key = key;
        _expression = expression;
        _resolver = resolver;
    }

    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        Map symbols = session.getSymbols();

        Object rawSource = OgnlUtils.get(_expression, _resolver, symbols);

        Iterator i = Tapestry.coerceToIterator(rawSource);

        if (i == null)
            return;

        while (i.hasNext())
        {
            Object newValue = i.next();

            symbols.put(_key, newValue);

            writeChildren(buffer, session);
        }

        // We leave the last value as a symbol; don't know if that's
        // good or bad.
    }
}