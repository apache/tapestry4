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
package tutorial.portal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.asset.ExternalAsset;
import net.sf.tapestry.util.xml.AbstractDocumentParser;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 * Parser for the Slashdot format.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class SlashdotParser extends AbstractDocumentParser
{
    private static class URLResourceLocation implements IResourceLocation
    {
        private URL _URL;

        private URLResourceLocation(URL URL)
        {
            _URL = URL;
        }

        public IResourceLocation getLocalization(Locale locale)
        {
            return this;
        }

        public String getName()
        {
            return _URL.getFile();
        }

        public IResourceLocation getRelativeLocation(String name)
        {
            return this; // Not right, but good enough.
        }

        public URL getResourceURL()
        {
            return _URL;
        }

        public IAsset toAsset()
        {
            return new ExternalAsset(_URL.toString());
        }

    }

    public List parseStories(URL URL) throws DocumentParseException
    {
        IResourceLocation location = new URLResourceLocation(URL);

        try
        {
            Document document = parse(location, "backslash");

            return build(document);
        }
        finally
        {
            setResourceLocation(null);
        }

    }

    /**
     *  Returns false, since there's no DTD for the document
     *  we need to parse.
     *
     **/

    protected boolean getRequireValidatingParser()
    {
        return false;
    }

    private List build(Document document) throws DocumentParseException
    {
        List result = new ArrayList();
        Node node = document.getDocumentElement();

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "story"))
            {
                SlashdotStory s = buildStory(child);
                result.add(s);
                continue;
            }
        }

        return result;
    }

    private SlashdotStory buildStory(Node node) throws DocumentParseException
    {
        SlashdotStory result = new SlashdotStory();

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "title"))
            {
                result.title = getValue(child);
                continue;
            }

            if (isElement(child, "author"))
            {
                result.author = getValue(child);
                continue;
            }

            if (isElement(child, "url"))
            {
                result.URL = getValue(child);
                continue;
            }

            if (isElement(child, "time"))
            {
                result.date = getValue(child);
                continue;
            }
        }

        return result;
    }
}