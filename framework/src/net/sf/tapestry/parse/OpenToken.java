package net.sf.tapestry.parse;

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
    private Map _attributes;

    /**
     *  Creates a new token with the given tag and id.  Retains (does not copy)
     *  the attributes (which may be null).
     * 
     **/

    public OpenToken(String tag, String id, Map attributes)
    {
        super(TokenType.OPEN);

        _tag = tag;
        _id = id;
        _attributes = attributes;
    }

    /**
     *  Returns the attributes (if any) for this tag.  Do not modify the return value.
     * 
     **/

    public Map getAttributes()
    {
        return _attributes;
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
        builder.append("attributes", _attributes);
    }

}
