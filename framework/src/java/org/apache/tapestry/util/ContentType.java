// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hivemind.util.Defense;

/**
 * Represents an HTTP content type. Allows to set various elements like the mime type, the character
 * set, and other parameters. This is similar to a number of other implementations of the same
 * concept in JAF, etc. We have created this simple implementation to avoid including the whole
 * libraries.
 * 
 * @author mindbridge
 * @since 3.0
 */
public class ContentType
{
    private String _baseType = "";

    private String _subType = "";

    private final Map _parameters = new HashMap();

    /**
     * Creates a new empty content type.
     */
    public ContentType()
    {
    }

    /**
     * Creates a new content type from the argument. The format of the argument has to be
     * basetype/subtype(;key=value)*
     * 
     * @param contentType
     *            the content type that needs to be represented
     */
    public ContentType(String contentType)
    {
        this();
        parse(contentType);
    }

    /**
     * Returns true only if the other object is another instance of ContentType, and has the ssame
     * baseType, subType and set of parameters.
     */
    public boolean equals(Object o)
    {
        if (o == null)
            return false;

        if (o.getClass() != this.getClass())
            return false;

        ContentType ct = (ContentType) o;

        return _baseType.equals(ct._baseType) && _subType.equals(ct._subType)
                && _parameters.equals(ct._parameters);
    }

    /**
     * @return the base type of the content type
     */
    public String getBaseType()
    {
        return _baseType;
    }

    /**
     * @param baseType
     */
    public void setBaseType(String baseType)
    {
        Defense.notNull(baseType, "baseType");

        _baseType = baseType;
    }

    /**
     * @return the sub-type of the content type
     */
    public String getSubType()
    {
        return _subType;
    }

    /**
     * @param subType
     */
    public void setSubType(String subType)
    {
        Defense.notNull(subType, "subType");

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
     * @param key
     *            the name of the content type parameter
     * @return the value of the content type parameter
     */
    public String getParameter(String key)
    {
        Defense.notNull(key, "key");

        return (String) _parameters.get(key);
    }

    /**
     * @param key
     *            the name of the content type parameter
     * @param value
     *            the value of the content type parameter
     */
    public void setParameter(String key, String value)
    {
        Defense.notNull(key, "key");
        Defense.notNull(value, "value");

        _parameters.put(key.toLowerCase(), value);
    }

    /**
     * Parses the argument and configures the content type accordingly. The format of the argument
     * has to be type/subtype(;key=value)*
     * 
     * @param contentType
     *            the content type that needs to be represented
     */
    public void parse(String contentType)
    {
        _baseType = "";
        _subType = "";
        _parameters.clear();

        StringTokenizer tokens = new StringTokenizer(contentType, ";");
        if (!tokens.hasMoreTokens())
            return;

        String mimeType = tokens.nextToken();
        StringTokenizer mimeTokens = new StringTokenizer(mimeType, "/");
        setBaseType(mimeTokens.hasMoreTokens() ? mimeTokens.nextToken() : "");
        setSubType(mimeTokens.hasMoreTokens() ? mimeTokens.nextToken() : "");

        while (tokens.hasMoreTokens())
        {
            String parameter = tokens.nextToken();

            StringTokenizer parameterTokens = new StringTokenizer(parameter, "=");
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
