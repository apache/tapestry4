//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.parse;

import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRender;
import net.sf.tapestry.Tapestry;

/**
 *  A token parser for a Tapestry HTML template.
 *
 *  <p>TBD:  Use a single token to represent an bodyless component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TemplateToken
{
    private TokenType type;

    private String tag;
    private String id;

    char[] templateData;

    private int startIndex = -1;
    private int endIndex = -1;

    private IRender render;

    private Map attributes;

    /**
     *  Constructs a {@link TokenType#TEXT} token with the given template data.
     *
     **/

    public TemplateToken(char[] templateData, int startIndex, int endIndex)
    {
        type = TokenType.TEXT;

        this.templateData = templateData;
        this.startIndex = startIndex;
        this.endIndex = endIndex;

        if (startIndex < 0
            || endIndex < 0
            || startIndex > templateData.length
            || endIndex > templateData.length)
            throw new IllegalArgumentException(
                Tapestry.getString("TemplateToken.range-error", this, Integer.toString(templateData.length)));
    }

    /**
     *  Constructs token, typically used with {@link TokenType#CLOSE}.
     *
     **/

    public TemplateToken(TokenType type, String tag)
    {
        this(type, null, tag, null);

    }

    /**
     *  Constructs an {@link TokenType#OPEN} with the given id.
     *
     **/

    public TemplateToken(String id, String tag)
    {
        this(TokenType.OPEN, id, tag, null);
    }

    /**
     *  Contructs an {@link TokenType#OPEN} token with the given id and attributes.
     *
     *  @since 1.0.2
     * 
     **/

    public TemplateToken(String id, String tag, Map attributes)
    {
        this(TokenType.OPEN, id, tag, attributes);
    }

    public TemplateToken(TokenType type, String id, String tag, Map attributes)
    {
        this.type = type;
        this.id = id;
        this.tag = tag;
        this.attributes = attributes;
    }

    public int getEndIndex()
    {
        return endIndex;
    }

    /**
     *  For {@link TokenType#OPEN}, returns the component id.
     *  For {@link TokenType#LOCALIZATION}, returns the localization key.
     *  Returns null for other token types.
     *
     **/

    public String getId()
    {
        return id;
    }

    /**
     *  Returns the tag if applicable (not {@link TokenType#TEXT}), or null
     * 
     *  @since 1.0.2
     * 
     **/

    public String getTag()
    {
        return tag;
    }

    public synchronized IRender getRender()
    {
        if (render == null)
        {
            if (type != TokenType.TEXT)
                throw new ApplicationRuntimeException(Tapestry.getString("TemplateToken.may-not-render", type));

            render = new RenderTemplateHTML(templateData, startIndex, endIndex - startIndex + 1);
        }

        return render;
    }

    /**
     *  Returns the starting index of the token.  Will return -1 for any non-TEXT
     *  token.
     *
     **/

    public int getStartIndex()
    {
        return startIndex;
    }

    public TokenType getType()
    {
        return type;
    }

    /**
     *  Returns the attributes associated with an {@link TokenType#OPEN}
     *  or {@link TokenType#LOCALIZATION} tag, which may
     *  be null.
     *
     *  @since 1.0.2
     * 
     **/

    public Map getAttributes()
    {
        return attributes;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("TemplateToken[");

        buffer.append(type.getName());

        if (id != null)
        {
            buffer.append(' ');
            buffer.append(id);
        }

        if (startIndex >= 0)
        {
            buffer.append(" start:");
            buffer.append(startIndex);

            buffer.append(" end:");
            buffer.append(endIndex);
        }

        boolean first = true;

        if (attributes != null)
        {
            Iterator i = attributes.keySet().iterator();

            while (i.hasNext())
            {
                if (first)
                {
                    buffer.append(" attributes: ");
                    first = false;
                }
                else
                    buffer.append(", ");

                Map.Entry e = (Map.Entry) i.next();

                buffer.append(e.getKey());
                buffer.append('=');
                buffer.append(e.getValue());
            }
        }

        buffer.append(']');

        return buffer.toString();
    }
}