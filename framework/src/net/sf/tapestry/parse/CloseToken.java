package net.sf.tapestry.parse;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Represents the closing tag of a component element in the template.
 *
 *  @see TokenType#CLOSE
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class CloseToken extends TemplateToken
{
    private String _tag;
    
    public CloseToken(String tag)
    {
        super(TokenType.CLOSE);
        
        _tag = tag;
    }
    
    public String getTag()
    {
        return _tag;
    }
       
    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("tag", _tag);
    }

}
