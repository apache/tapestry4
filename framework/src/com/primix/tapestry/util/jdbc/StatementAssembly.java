package com.primix.foundation.jdbc;

import java.util.*;
import java.sql.*;

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
 *  Class for creating and executing JDBC statements.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class StatementAssembly
{
	private StringBuffer buffer = new StringBuffer();
	private List parameters;
	
	private int lineLength;
	private int maxLineLength = 80;
	private int indent = 5;
	
	/**
	 *  Default constructor; uses a maximum line length of 80 and an indent of 5.
	 *
	 */
	 
	public StatementAssembly()
	{
	}
	
	public StatementAssembly(int maxLineLength, int indent)
	{
		this.maxLineLength = maxLineLength;
		this.indent = indent;
	}
	
	/**
	 *  Maximum length of a line.
	 *
	 */
	 
	public int getMaxLineLength()
	{
		return maxLineLength;
	}
	
	/**
	 *  Number of spaces to indent continuation lines by.
	 *
	 */
	 
	public int getIndent()
	{
		return indent;
	}
	
	/**
	 *  Adds text to the current line, unless that would make the line too long, in
	 *  which case a new line is started (and indented) before adding the text.
	 *
	 */
	 
	public void add(String text)
	{
		int textLength;
		
		textLength = text.length();
		
		if (lineLength + textLength > maxLineLength)
		{
			buffer.append('\n');
			
			for (int i = 0; i < indent; i++)
				buffer.append(' ');
				
			lineLength = indent;
		}
		
		buffer.append(text);
		lineLength += textLength;
	}
	
	/** 
	 *  Adds a seperator (usually a comma and a space) to the current line, regardless
	 *  of line length.
	 *
	 */
	 
	public void addSep(String text)
	{
		buffer.append(text);
		lineLength += text.length();
	}
	
	/**
	 *  Starts a new line, without indenting.
	 *
	 */
	 
	public void newLine()
	{
		if (buffer.length() != 0)
			buffer.append('\n');
			
		lineLength = 0;
	}
	
	/**
	 * Starts a new line, then adds the given text.
	 *
	 */
	 
	public void newLine(String text)
	{
		if (buffer.length() != 0)
			buffer.append('\n');
			
		buffer.append(text);
		
		lineLength = text.length();
	}
	
	public void addList(String[] items, String seperator)
	{
		int i;
		
		for (i = 0; i < items.length; i++)
		{
			if (i > 0)
				addSep(seperator);
			
			add(items[i]);
		}
	}	
	
	/**
	 *  Adds a parameter to the statement.  Adds a question mark to the SQL and stores
	 *  the value for later.
	 *
	 */
	 
	public void addParameter(Object value)
	{
		if (parameters == null)
			parameters = new ArrayList();
		
		parameters.add(value);
		
		add("?");	
	}
	
	/**
	 *  Adds a parameter with some associated text, which should include the question
	 *  mark used to represent the parameter in the SQL.
	 *
	 */
	 	
	public void addParameter(String text, Object value)
	{
		if (parameters == null)
			parameters = new ArrayList();
		
		parameters.add(value);
		
		add(text);
	}
		
	/**
	 *  Creates and returns a {@link IStatement} based on the SQL and parameters
	 *  acquired.
	 *
	 */
	 
	public IStatement createStatement(Connection connection)
	throws SQLException
	{
		String sql;
		IStatement result;
		
		sql = buffer.toString();
		
		buffer = null;
		
		if (parameters == null)
			result = new SimpleStatement(sql, connection);
		else
			result = new ParameterizedStatement(sql, connection, parameters);
		
		// These are no longer needed.
		
		buffer = null;
		parameters = null;
		
		return result;		
	}
}