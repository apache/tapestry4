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

package org.apache.tapestry.html;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.tapestry.AbstractMarkupWriter;
import org.apache.tapestry.IMarkupWriter;

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

	/**
	 *  Creates a new markup writer around the {@link PrintWriter}.
	 *  The writer will not be closed when the markup writer closes.
	 *  The content type is currently hard-wired to
	 *  <code>text/html</code>.
	 * 
	 *  @since 3.0
	 * 
	 **/
	
	public HTMLWriter(PrintWriter writer)
	{
		super(safe, entities, "text/html", writer);
	}

    public HTMLWriter(String contentType, OutputStream outputStream)
    {
        super(safe, entities, contentType, outputStream);
    }

    public HTMLWriter(String contentType, String encoding, OutputStream outputStream)
    {
        super(safe, entities, contentType, encoding, outputStream);
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
        this(outputStream, "UTF-8");
    }

    public HTMLWriter(OutputStream outputStream, String encoding)
    {
        this("text/html", encoding, outputStream);
    }

    public IMarkupWriter getNestedWriter()
    {
        return new NestedHTMLWriter(this);
    }
}