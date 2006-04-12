package net.sf.tapestry.parse;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Renders static HTML text from a template.  To neaten up the response HTML, leading
 *  and trailing whitespace is reduced to a single character.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class RenderTemplateHTML implements IRender
{
    private char[] templateData;
    private int offset;
    private int length;
    private boolean needsTrim = true;

    public RenderTemplateHTML(char[] templateData, int offset, int length)
    {
        this.templateData = templateData;
        this.offset = offset;
        this.length = length;

        if (offset < 0
            || length < 0
            || offset >= templateData.length
            || offset + length > templateData.length)
            throw new IllegalArgumentException(
                Tapestry.getString("RenderTemplateHTML.bad-range", this));
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (needsTrim)
        {
            synchronized (this)
            {
                if (needsTrim)
                    trim();

                needsTrim = false;
            }
        }

        if (length == 0)
            return;

        // At one time, we would check to see if the cycle was rewinding and
        // only invoke printRaw() if it was.  However, that slows down
        // normal rendering (microscopically) and, with the new
        // NullResponseWriter class, the "cost" of invoking cycle.isRewinding()
        // is approximately the same as the "cost" of invoking writer.printRaw().

        writer.printRaw(templateData, offset, length);
    }

    /**
     *  Strip off all leading and trailing whitespace by adjusting offset and length.
     *
     **/

    private void trim()
    {
        char ch;
        boolean didTrim = false;

        if (length == 0)
            return;

        try
        {

            // Shave characters off the end until we hit a non-whitespace
            // character.

            while (length > 0)
            {
                ch = templateData[offset + length - 1];

                if (!Character.isWhitespace(ch))
                    break;

                length--;
                didTrim = true;
            }

            // Restore one character of whitespace to the end

            if (didTrim)
                length++;

            didTrim = false;

            // Strip characters off the front until we hit a non-whitespace
            // character.

            while (length > 0)
            {
                ch = templateData[offset];

                if (!Character.isWhitespace(ch))
                    break;

                offset++;
                length--;
                didTrim = true;
            }

            // Again, restore one character of whitespace.

            if (didTrim)
            {
                offset--;
                length++;
            }

        }
        catch (IndexOutOfBoundsException ex)
        {
            throw new RuntimeException(
                Tapestry.getString("RenderTemplateHTML.error-trimming", this));
        }

        // Ok, this isn't perfect.  I don't want to write into templateData[] even
        // though I'd prefer that my single character of whitespace was always a space.
        // It would also be kind of neat to shave whitespace within the static HTML, rather
        // than just on the edges.
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("RenderTemplateHTML[");

        buffer.append("offset: ");
        buffer.append(offset);

        buffer.append(" length: ");
        buffer.append(length);

        buffer.append('/');
        buffer.append(templateData.length);

        buffer.append(" <");

        try
        {

            for (int i = 0; i < length; i++)
            {
                char ch = templateData[offset + i];

                // If outside of normal ASCII range ... this is sloppy!

                if (ch < 32 || ch > 126)
                    buffer.append('.');
                else
                    buffer.append(ch);
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
        }

        buffer.append(">]");

        return buffer.toString();
    }
}