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

package net.sf.tapestry.html;

import java.io.OutputStream;

import net.sf.tapestry.AbstractResponseWriter;
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

public class HTMLWriter extends AbstractResponseWriter
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
     *  Creates a default writer for content type "text/html".
     * 
     **/

    public HTMLWriter(OutputStream outputStream)
    {
        this("text/html", outputStream);
    }

    public IMarkupWriter getNestedWriter()
    {
        return new NestedHTMLWriter(this);
    }
}