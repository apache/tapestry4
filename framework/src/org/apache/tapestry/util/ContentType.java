/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *  Represents an HTTP content type. Allows to set various elements like
 *  the mime type, the character set, and other parameters.
 *  This is similar to a number of other implementations of the same concept in JAF, etc.
 *  We have created this simple implementation to avoid including the whole libraries. 
 * 
 *  @version $Id$
 *  @author mindbridge
 *  @since 3.0
 **/
public class ContentType
{
    private String _baseType;
    private String _subType;
    private Map _parameters;

    /**
     * Creates a new empty content type
     */
    public ContentType()
    {
        initialize();
    }
    
    /**
     * Creates a new content type from the argument.
     * The format of the argument has to be basetype/subtype(;key=value)* 
     * 
     * @param contentType the content type that needs to be represented
     */
    public ContentType(String contentType)
    {
        this();
        parse(contentType);
    }
    
    private void initialize()
    {
        _baseType = "";
        _subType = "";
        _parameters = new HashMap();
    }
    
    /**
     * @return
     */
    public String getBaseType()
    {
        return _baseType;
    }

    /**
     * @param string
     */
    public void setBaseType(String baseType)
    {
        _baseType = baseType;
    }

    /**
     * @return
     */
    public String getSubType()
    {
        return _subType;
    }

    /**
     * @param string
     */
    public void setSubType(String subType)
    {
        _subType = subType;
    }

    /**
     * @return the MIME type of the content type
     */
    public String getMimeType()
    {
        return _baseType + "/" + _subType;
    }

    /**
     * @return the list of names of parameters in this content type 
     */
    public String[] getParameterNames()
    {
        Set parameterNames = _parameters.keySet(); 
        return (String[]) parameterNames.toArray(new String[parameterNames.size()]);
    }

    /**
     * @param key the name of the content type parameter
     * @return the value of the content type parameter
     */
    public String getParameter(String key)
    {
        return (String) _parameters.get(key);
    }

    /**
     * @param key the name of the content type parameter
     * @param value the value of the content type parameter
     */
    public void setParameter(String key, String value)
    {
        _parameters.put(key.toLowerCase(), value);
    }

    /**
     * Parses the argument and configures the content type accordingly.
     * The format of the argument has to be type/subtype(;key=value)* 
     * 
     * @param contentType the content type that needs to be represented
     */
    public void parse(String contentType)
    {
        initialize();

        StringTokenizer tokens = new StringTokenizer(contentType, ";");
        if (!tokens.hasMoreTokens()) 
            return;
        
        String mimeType = tokens.nextToken();
        StringTokenizer mimeTokens = new StringTokenizer(mimeType, "/");
        setBaseType(mimeTokens.hasMoreTokens() ? mimeTokens.nextToken() : "");
        setSubType(mimeTokens.hasMoreTokens() ? mimeTokens.nextToken() : "");
        
        while (tokens.hasMoreTokens()) {
            String parameter = tokens.nextToken();

            StringTokenizer parameterTokens = new StringTokenizer(mimeType, "=");
            String key = parameterTokens.hasMoreTokens() ? parameterTokens.nextToken() : "";
            String value = parameterTokens.hasMoreTokens() ? parameterTokens.nextToken() : "";
            setParameter(key, value);
        }
    }

    

    /**
     * @return the string representation of this content type
     */
    public String unparse()
    {
        StringBuffer buf = new StringBuffer(getMimeType());

        String[] parameterNames = getParameterNames();
        for (int i = 0; i < parameterNames.length; i++)
        {
            String key = parameterNames[i];
            String value = getParameter(key);
            buf.append(";" + key + "=" + value);
        } 
        
        return buf.toString();
    }
    
    /**
     * @return the string representation of this content type. Same as unparse().
     */
    public String toString()
    {
        return unparse();
    }

}
