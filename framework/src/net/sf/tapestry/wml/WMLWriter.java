package net.sf.tapestry.wml;

import java.io.OutputStream;

import net.sf.tapestry.AbstractMarkupWriter;
import net.sf.tapestry.IMarkupWriter;

/**
 *  This class is used to create WML output.
 *
 *  <p>The <code>WMLResponseWriter</code> handles the necessary escaping 
 *  of invalid characters.
 *  Specifically, the '$', '&lt;', '&gt;' and '&amp;' characters are properly
 *  converted to their WML entities by the <code>print()</code> methods.
 *  Similar measures are taken by the {@link #attribute(String, String)} method.
 *  Other invalid characters are converted to their numeric entity equivalent.
 *
 *  <p>This class makes it easy to generate trivial and non-trivial WML pages.
 *  It is also useful to generate WML snippets. It's ability to do simple
 *  formatting is very useful. A JSP may create an instance of the class
 *  and use it as an alternative to the simple-minded <b>&lt;%= ... %&gt;</b>
 *  construct, espcially because it can handle null more cleanly.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 0.2.9
 * 
 **/

public class WMLWriter extends AbstractMarkupWriter
{

    private static final String[] entities = new String[64];
    private static final boolean[] safe = new boolean[128];
    private static final String SAFE_CHARACTERS =
        "01234567890"
            + "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "\t\n\r !\"#%'()*+,-./:;=?@[\\]^_`{|}~";

    static {
        entities['"'] = "&quot;";
        entities['<'] = "&lt;";
        entities['>'] = "&gt;";
        entities['&'] = "&amp;";
        entities['$'] = "$$";

        int length = SAFE_CHARACTERS.length();
        for (int i = 0; i < length; i++)
            safe[SAFE_CHARACTERS.charAt(i)] = true;
    }

    /**
     *  Creates a response writer for content type "text/vnd.wap.wml".
     * 
     **/

    public WMLWriter(OutputStream stream)
    {
        this("text/vnd.wap.wml", stream);
    }

    public WMLWriter(String contentType, OutputStream stream)
    {
        super(safe, entities, contentType, stream);
    }

    protected WMLWriter(String contentType)
    {
        super(safe, entities, contentType);
    }

    public IMarkupWriter getNestedWriter()
    {
        return new NestedWMLWriter(this);
    }

    public String getContentType()
    {
        return "";
    }
}