package net.sf.tapestry.parse;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Token representing the open tag for a component.  Components may be either
 *  specified or implicit.  Specified components (the traditional type, dating
 *  back to the origin of Tapestry) are matched by an entry in the
 *  containing component's specification.  Implicit components specify their
 *  type in the component template and must not have an entry in
 *  the containing component's specification.
 *
 *  @see TokenType#OPEN
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class OpenToken extends TemplateToken
{
    private String _tag;
    private String _id;
    private String _componentType;
    private Map _values;
    private Map _expressions;

    /**
     *  Creates a new token with the given tag, id and type 
     * 
     *  @param tag the template tag which represents the component, typically "span"
     *  @param id the id for the component, which may be assigned by the template
     *  parser for implicit components
     *  @param  componentType the type of component, if an implicit component, or null for
     *  a specified component
     * 
     **/

    public OpenToken(String tag, String id, String componentType)
    {
        super(TokenType.OPEN);

        _tag = tag;
        _id = id;
        _componentType = componentType;

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

    /**
     *  Returns the id for the component.
     * 
     **/
    
    public String getId()
    {
        return _id;
    }

    /**
     *  Returns the tag used to represent the component within the template.
     * 
     **/
    
    public String getTag()
    {
        return _tag;
    }
    
    /**
     *  Returns the specified component type, or null for a component where the type
     *  is not defined in the template.  The type may include a library id prefix.
     * 
     **/
    
    public String getComponentType()
    {
        return _componentType;
    }

    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("id", _id);
        builder.append("componentType", _componentType);
        builder.append("tag", _tag);
        builder.append("values", _values);
        builder.append("expressions", _expressions);
    }

}
