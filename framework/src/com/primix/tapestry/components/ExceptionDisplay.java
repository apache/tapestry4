package com.primix.tapestry.components;

import com.primix.foundation.*;
import com.primix.foundation.exception.*;
import java.lang.reflect.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import org.xml.sax.SAXException;
import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;
import java.util.*;
import java.beans.*;

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
 *  A complicated component that deals with displaying the current application
 *  exception.  An application will simply wrap this component on a page.
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *    <th>Read / Write</th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>exceptions</td>
 *  <td>{@link ExceptionDescription}[]</td>
 *  <td>R</th>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>An array of {@link ExceptionDescription} objects.</td>
 * </tr>
 *
 *
 * <p>Informal parameters are not allowed.  May not contain a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ExceptionDisplay extends BaseComponent
{
    private ExceptionDescription exceptionDescription;
    private ExceptionProperty exceptionProperty;
    private String stackTrace;
    private boolean last;
  
    public ExceptionDisplay(IPage page, IComponent container, String id, 
			  ComponentSpecification spec)
    {
	super(page, container, id, spec);
    }

    public ExceptionDescription[] getDescriptions()
    {
	return null;
    }

    public ExceptionDescription getExceptionDescription()
    {
	return exceptionDescription;
    }

    public ExceptionProperty getExceptionProperty()
    {
	return exceptionProperty;
    }

    public String getStackTrace()
    {
	return stackTrace;
    }

    public boolean isLast()
    {
	return last;
    }

    public void setExceptionDescription(ExceptionDescription value)
    {
	exceptionDescription = value;
    }

    public void setExceptionProperty(ExceptionProperty value)
    {
	exceptionProperty = value;
    }

    public void setLast(boolean value)
    {
	last = value;
    }

    public void setStackTrace(String value)
    {
	stackTrace = value;
    }
}
