//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

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

    private char[] _templateData;

    private TemplateToken[] _tokens;

    /**
     *  Creates a new ComponentTemplate.
     *
     *  @param templateData The template data.  This is <em>not</em> copied, so
     *  the array passed in should not be modified further.
     *
     *  @param tokens  The tokens making up the template.  This is also
     *  retained (<em>not</em> copied), and so should not
     *  be modified once passed to the constructor.
     *
     **/

    public ComponentTemplate(char[] templateData, TemplateToken[] tokens)
    {
        _templateData = templateData;
        _tokens = tokens;
    }

    public char[] getTemplateData()
    {
        return _templateData;
    }

    public TemplateToken getToken(int index)
    {
        return _tokens[index];
    }

    public int getTokenCount()
    {
        return _tokens.length;
    }
}