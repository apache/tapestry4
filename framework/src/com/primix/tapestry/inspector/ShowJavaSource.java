package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.components.*;
import java.util.*;
import java.io.*;

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
 *  Component of the {@link Inspector} page used to display
 *  the source code for a component.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowJavaSource extends BaseComponent
implements ILifecycle
{
	private List source;
	private int lineNumber;
	private String line;
	
	public ShowJavaSource(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public void cleanupAfterRender(IRequestCycle cycle)
	{
		source = null;
		line = null;
	}
	
	private void readSource()
	{
		Inspector inspector;
		IComponent inspectedComponent;
		String className;
		String sourceName;
		InputStream in;
		LineNumberReader reader;
		String line;
		
		inspector = (Inspector)page;
		inspectedComponent = inspector.getInspectedComponent();
		
		className = inspectedComponent.getClass().getName();
		
		sourceName = "/" + className.replace('.', '/') + ".java";
	
		in = inspectedComponent.getClass().getResourceAsStream(sourceName);
		
		if (in == null)
			return;
			
		reader = new LineNumberReader(new InputStreamReader(in));
		source = new ArrayList();
		
		try
		{
			while (true)
			{
				line = reader.readLine();
				if (line == null)
					break;
				
				source.add(line);	
			}
		}
		catch (IOException e)
		{
			source = null;
			System.err.println("Unable to read: " + sourceName);
			e.printStackTrace(System.err);
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				System.err.println("Unable to close reader: " + e);
			}
		}
		
		lineNumber = 0;
	}
	
	/**
	 *  Returns a {@link List} of {@link String}, each a line from
	 *  the source code.  Returns null if the source code is not available.
	 *
	 */
	 
	public List getSource()
	{
		if (source == null)
			readSource();
		
		return source;	
	}
	
	/**
	 *  Returns the current line number, which is incremented by
	 *  the method {@link #setLine(String)}.
	 *
	 */
	 
	public int getLineNumber()
	{
		return lineNumber;
	}
	
	public String getLine()
	{
		return line;
	}
	
	public void setLine(String value)
	{
		line = value;
		lineNumber++;
	}
	
}