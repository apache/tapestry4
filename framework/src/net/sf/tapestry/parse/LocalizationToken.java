package net.sf.tapestry.parse;

import java.util.Map;

/**
 *  Represents localized text from the template.
 *
 *  @see TokenType#LOCALIZATION
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class LocalizationToken extends TemplateToken
{
    private String _tag;
    private String _key;
    private boolean _raw;
    private Map _attributes;
    
    /**
     *  Creates a new token.
     * 
     * 
     *  @param tag the tag of the element from the template
     *  @param key the localization key specified
     *  @param raw if true, then the localized value contains markup that should not be escaped
     *  @param attribute any additional attributes (beyond those used to define key and raw)
     *  that were specified.  This value is retained, not copied.
     * 
     **/
    
    public LocalizationToken(String tag, String key, boolean raw, Map attributes)
    {
        super(TokenType.LOCALIZATION);
        
        _tag = tag;
        _key = key;
        _raw = raw;
        _attributes = attributes;
    }
    
    /**
     *  Returns any attributes for the token, which may be null.  Do not modify
     *  the return value.
     * 
     **/
    
    public Map getAttributes()
    {
        return _attributes;
    }

    public boolean isRaw()
    {
        return _raw;
    }

    public String getTag()
    {
        return _tag;
    }

    public String getKey()
    {
        return _key;
    }
}
