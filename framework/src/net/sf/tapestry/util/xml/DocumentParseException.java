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

package net.sf.tapestry.util.xml;

import org.xml.sax.SAXParseException;

/**
 *  Exception thrown if there is any kind of error parsing the
 *  an XML document. 
 *
 *  @see AbstractDocumentParser
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.10
 *
 **/

public class DocumentParseException extends Exception
{
    private Throwable rootCause;
    private int lineNumber;
    private int column;
    private String resourcePath;

    public DocumentParseException(String message, Throwable rootCause)
    {
        this(message, null, rootCause);
    }

    public DocumentParseException(String message, String resourcePath)
    {
        this(message, resourcePath, null);
    }

    public DocumentParseException(String message, SAXParseException rootCause)
    {
        this(message, null, rootCause);
    }

    public DocumentParseException(
        String message,
        String resourcePath,
        Throwable rootCause)
    {
        super(message);

        this.resourcePath = resourcePath;

        this.rootCause = rootCause;
    }

    public DocumentParseException(
        String message,
        String resourcePath,
        SAXParseException rootCause)
    {
        this(message, resourcePath, (Throwable) rootCause);

        if (rootCause != null)
        {
            lineNumber = rootCause.getLineNumber();
            column = rootCause.getColumnNumber();
        }
    }

    public DocumentParseException(String message)
    {
        super(message);
    }

    public DocumentParseException(Throwable rootCause)
    {
        this(rootCause.getMessage(), rootCause);
    }

    public DocumentParseException(SAXParseException rootCause)
    {
        this(rootCause.getMessage(), (Throwable) rootCause);
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public int getColumn()
    {
        return column;
    }

    public String getResourcePath()
    {
        return resourcePath;
    }

}