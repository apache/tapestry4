package net.sf.tapestry.parse;

import org.apache.commons.lang.enum.Enum;

/**
 * An {@link Enum} of the different possible token types.
 *
 * @see TemplateToken
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class TokenType extends Enum
{
    /**
     *  Raw HTML text.
     * 
     *  @see TextToken
     * 
     *
     **/

    public static final TokenType TEXT = new TokenType("TEXT");

    /**
     *  The opening tag of an element.
     * 
     *  @see OpenToken
     *
     **/

    public static final TokenType OPEN = new TokenType("OPEN");

    /**
     *  The closing tag of an element.
     * 
     *  @see CloseToken
     *
     **/

    public static final TokenType CLOSE = new TokenType("CLOSE");

    /**
     * 
     *  A reference to a localized string.
     * 
     *  @since 2.0.4
     * 
     **/
    
    public static final TokenType LOCALIZATION = new TokenType("LOCALIZATION");
    
    private TokenType(String name)
    {
        super(name);
    }

}