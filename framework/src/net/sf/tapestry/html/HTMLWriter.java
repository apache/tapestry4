package net.sf.tapestry.html;

import java.io.OutputStream;

import net.sf.tapestry.AbstractMarkupWriter;
import net.sf.tapestry.IMarkupWriter;

/**
 *  This class is used to create HTML output.
 *
 *  <p>The <code>HTMLWriter</code> handles the necessary escaping 
 *  of invalid characters.
 *  Specifically, the '&lt;', '&gt;' and '&amp;' characters are properly
 *  converted to their HTML entities by the <code>print()</code> methods.
 *  Similar measures are taken by the {@link #attribute(String, String)} method.
 *  Other invalid characters are converted to their numeric entity equivalent.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 **/

public class HTMLWriter extends AbstractMarkupWriter
{

    private static final String[] entities = new String[64];
    private static final boolean[] safe = new boolean[128];

    private static final String SAFE_CHARACTERS =
        "01234567890"
            + "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "\t\n\r !\"#$%'()*+,-./:;=?@[\\]^_`{|}~";

    static {
        entities['"'] = "&quot;";
        entities['<'] = "&lt;";
        entities['>'] = "&gt;";
        entities['&'] = "&amp;";

        int length = SAFE_CHARACTERS.length();
        for (int i = 0; i < length; i++)
            safe[SAFE_CHARACTERS.charAt(i)] = true;
    }

    public HTMLWriter(String contentType, OutputStream outputStream)
    {
        super(safe, entities, contentType, outputStream);
    }

    protected HTMLWriter(String contentType)
    {
        super(safe, entities, contentType);
    }

    /**
     *  Creates a default writer for content type "text/html; charset=utf-8".
     * 
     **/

    public HTMLWriter(OutputStream outputStream)
    {
        this("text/html; charset=utf-8", outputStream);
    }

    public IMarkupWriter getNestedWriter()
    {
        return new NestedHTMLWriter(this);
    }
}