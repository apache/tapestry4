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

package org.apache.tapestry.wml;

import java.io.OutputStream;

import org.apache.tapestry.AbstractMarkupWriter;
import org.apache.tapestry.IMarkupWriter;

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
        this(stream, "UTF-8");
    }

    /**
     * 
     * @param stream the output stream where to write the text
     * @param encoding the encoding to be used to generate the output
     * @since 3.0
     * 
     **/
    public WMLWriter(OutputStream stream, String encoding)
    {
        this("text/vnd.wap.wml", encoding, stream);
    }

    public WMLWriter(String contentType, OutputStream stream)
    {
        super(safe, entities, contentType, stream);
    }

    /**
     * 
     * @param mimeType the MIME type to be used to generate the content type
     * @param encoding the encoding to be used to generate the output
     * @param stream the output stream where to write the text
     * @since 3.0
     * 
     **/
    public WMLWriter(String mimeType, String encoding, OutputStream stream)
    {
        super(safe, entities, mimeType, encoding, stream);
    }

    protected WMLWriter(String contentType)
    {
        super(safe, entities, contentType);
    }

    public IMarkupWriter getNestedWriter()
    {
        return new NestedWMLWriter(this);
    }
}