package net.sf.tapestry.script;

import java.util.Map;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  
 *  Like {@link net.sf.tapestry.script.LetToken}, but sets the value
 *  from an expression attribute, rather than a body of full content.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

class SetToken extends AbstractToken
{
    private String _key;
    private String _expression;
    private IResourceResolver _resolver;

    SetToken(String key, String expression, IResourceResolver resolver)
    {
        _key = key;
        _expression = expression;
        _resolver = resolver;
    }

    /**
     *   
     *  Doesn't <em>write</em>, it evaluates the expression and assigns
     *  the result back to the key. 
     * 
     **/
    
    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        Map symbols = session.getSymbols();

        Object value = OgnlUtils.get(_expression, _resolver, symbols);

        symbols.put(_key, value);
    }

}
