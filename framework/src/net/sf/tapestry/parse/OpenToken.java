package net.sf.tapestry.parse;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Token representing the open tag for a component.
 *
 *  @see TokenType#OPEN
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since NEXT_RELEASE
 *
 **/

public class OpenToken extends TemplateToken
{
    private String _tag;
    private String _id;
    private Map _values;
    private Map _expressions;

    /**
     *  Creates a new token with the given tag and id.  
     * 
     **/

    public OpenToken(String tag, String id)
    {
        super(TokenType.OPEN);

        _tag = tag;
        _id = id;

    }

    /**
     *  Adds a static value with the given name.
     * 
     **/

    public void addStaticValue(String name, String value)
    {
        if (_values == null)
            _values = new HashMap();

        _values.put(name, value);
    }

    /**
     *  Adds an expression with the given name.
     * 
     **/

    public void addExpressionValue(String name, String value)
    {
        if (_expressions == null)
            _expressions = new HashMap();

        _expressions.put(name, value);
    }

    /**
     *  Returns a Map of static values, keyed on attribute name.  Do not modify the returned Map.
     *  May return null.
     **/

    public Map getStaticValuesMap()
    {
        return _values;
    }

    /**
     *  Returns a Map of OGNL expressions, keyed on attribute name.
     *  Done not modify the returned Map.
     *  May return null.
     * 
     **/

    public Map getExpressionValuesMap()
    {
        return _expressions;
    }

    public String getId()
    {
        return _id;
    }

    public String getTag()
    {
        return _tag;
    }

    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("id", _id);
        builder.append("tag", _tag);
        builder.append("values", _values);
        builder.append("expressions", _expressions);
    }

}
