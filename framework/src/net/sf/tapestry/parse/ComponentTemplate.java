package net.sf.tapestry.parse;

/**
 *  Enapsulates a parsed component template, allowing access to the
 *  tokens parsed.
 *
 *  <p>TBD:  Record the name of the resource (or other location) from which
 *  the template was parsed (useful during debugging).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ComponentTemplate
{
    /**
     *  The HTML template from which the tokens were generated.  This is a string
     *  read from a resource.  The tokens represents offsets and lengths into
     *  this string.
     *
     **/

    private char[] templateData;

    private TemplateToken[] tokens;

    /**
     *  Creates a new ComponentTemplate.
     *
     *  @param templateData The template data.  This is <em>not</em> copied, so
     *  the array passed in should not be modified further.
     *
     *  @param tokens  The tokens making up the template.
     *
     **/

    public ComponentTemplate(char[] templateData, TemplateToken[] tokens)
    {
        this.templateData = templateData;
        this.tokens = tokens;
    }

    public char[] getTemplateData()
    {
        return templateData;
    }

    public TemplateToken getToken(int index)
    {
        return tokens[index];
    }

    public int getTokenCount()
    {
        return tokens.length;
    }
}