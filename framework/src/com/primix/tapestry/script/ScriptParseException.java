package com.primix.tapestry.script;

import org.xml.sax.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Exception thrown if there is any kind of error parsing the
 *  Script XML document.  This is basically a cut-and-paste of
 *  {@link com.primix.tapestry.parse.SpecificationParseException}.
 *
 *  @see ScriptParser
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class ScriptParseException
extends Exception
{
	private Throwable rootCause;
    private int lineNumber;
    private int column;
    private String resourcePath;

    public ScriptParseException(String message, Throwable rootCause)
    {
        this(message, null, null, rootCause);
    }

    public ScriptParseException(String message, String resourcePath,
                Locator locator, Throwable rootCause)
    {
        super(message);

        this.resourcePath = resourcePath;
        
        if (locator != null)
        {
            lineNumber = locator.getLineNumber();
            column  = locator.getColumnNumber();
        }

        this.rootCause = rootCause;
    }

    public ScriptParseException(String message)
    {
        super(message);
    }

    public ScriptParseException(Throwable rootCause)
    {
        this(rootCause.getMessage(), rootCause);
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