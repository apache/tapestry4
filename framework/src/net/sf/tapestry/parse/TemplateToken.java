package net.sf.tapestry.parse;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRender;
import net.sf.tapestry.Tapestry;

/**
 *  Base class for a number of different types of tokens that can be extracted
 *  from a page/component template.  This class defines the
 *  type of the token,
 *  subclasses provide interpretations on the token.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class TemplateToken
{
    private TokenType _type;

    protected TemplateToken(TokenType type)
    {
        _type = type;
    }

    public TokenType getType()
    {
        return _type;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
        
        builder.append("type", _type.getName());       
 
        extendDescription(builder);       

        return builder.toString();
    }
    
    /**
     *  Overridden in subclasses to append additional fields (defined in the subclass)
     *  to the description.  Subclasses may override this method without invoking
     *  this implementation, which is empty.
     * 
     *  @since 2.4
     * 
     **/
    
    protected void extendDescription(ToStringBuilder builder)
    {
    }
}