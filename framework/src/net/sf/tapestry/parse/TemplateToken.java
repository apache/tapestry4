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
    private TokenType _type;

    private String _tag;
    private String _id;

    char[] _templateData;

    private int _startIndex = -1;
    private int _endIndex = -1;

    private IRender _render;

    private Map _attributes;

    /**
     *  Used with LOCALIZATION tokens.
     * 
     *  @since 2.3
     * 
     **/
    
    private boolean _raw;

    /**
     *  Constructs a {@link TokenType#TEXT} token with the given template data.
     *
     **/

    public TemplateToken(char[] templateData, int startIndex, int endIndex)
    {
        _type = TokenType.TEXT;

        _templateData = templateData;
        _startIndex = startIndex;
        _endIndex = endIndex;

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

    /**
     *  Constructs a {@link TokenType#LOCALIZATION} token.  The localization key
     *  can be retrieved via {@link #getId()}.
     * 
     **/
    
    public TemplateToken(String key, boolean raw, String tag, Map attributes)
    {        
        this(TokenType.LOCALIZATION, key, tag, attributes);
        _raw = raw;
    }

    protected TemplateToken(TokenType type, String id, String tag, Map attributes)
    {
        _type = type;
        _id = id;
        _tag = tag;
        _attributes = attributes;
    }

    public int getEndIndex()
    {
        return _endIndex;
    }

    /**
     *  For {@link TokenType#OPEN}, returns the component id.
     *  For {@link TokenType#LOCALIZATION}, returns the localization key.
     *  Returns null for other token types.
     *
     **/

    public String getId()
    {
        return _id;
    }

    /**
     *  Returns the tag if applicable (not {@link TokenType#TEXT}), or null
     * 
     *  @since 1.0.2
     * 
     **/

    public String getTag()
    {
        return _tag;
    }

    public synchronized IRender getRender()
    {
        if (_render == null)
        {
            if (_type != TokenType.TEXT)
                throw new ApplicationRuntimeException(Tapestry.getString("TemplateToken.may-not-render", _type));

            _render = new RenderTemplateHTML(_templateData, _startIndex, _endIndex - _startIndex + 1);
        }

        return _render;
    }

    /**
     *  Returns the starting index of the token.  Will return -1 for any non-TEXT
     *  token.
     *
     **/

    public int getStartIndex()
    {
        return _startIndex;
    }

    public TokenType getType()
    {
        return _type;
    }


    /**
     *  Returns true for {@link TokenType#LOCALIZATION} tokens, if the
     *  String is to be inserted into the response raw.
     * 
     **/
    
    public boolean isRaw()
    {
        return _raw;
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
        return _attributes;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("TemplateToken[");

        buffer.append(_type.getName());

        if (_id != null)
        {
            buffer.append(' ');
            buffer.append(_id);
        }

        if (_startIndex >= 0)
        {
            buffer.append(" start:");
            buffer.append(_startIndex);

            buffer.append(" end:");
            buffer.append(_endIndex);
        }

        boolean first = true;

        if (_attributes != null)
        {
            Iterator i = _attributes.keySet().iterator();

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