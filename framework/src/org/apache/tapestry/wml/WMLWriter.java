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

package org.apache.tapestry.wml;

import org.apache.tapestry.AbstractMarkupWriter;
import org.apache.tapestry.IMarkupWriter;

import java.io.OutputStream;

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
        this("text/vnd.wap.wml", stream);
    }

    public WMLWriter(String contentType, OutputStream stream)
    {
        super(safe, entities, contentType, stream);
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