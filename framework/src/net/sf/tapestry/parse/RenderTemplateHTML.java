/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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