/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.html;

import com.primix.tapestry.*;
import java.io.*;

// Appease Javadoc
import com.primix.tapestry.form.*;

/**
 *  Inserts formatted text (possibly collected using a {@link Text} component.
 *  To maintain the line breaks provided originally, this component will
 *  break the input into individual lines and insert additional
 *  HTML to make each line seperate.
 *
 * <p>This can be down more simply, using the &lt;pre&gt; HTML element, but
 * that usually renders the text in a non-proportional font.
 *
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th>
 * <th>Read / Write</th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>text</td> <td>{@link String}</td> <td>R</td>
 *  <td>no</td> <td>&nbsp;</td>
 *  <td>The text to be inserted.  If not provided, no output is written</td> </tr>
 *
 *  <tr>
 *      <td>mode</td>
 *      <td>{@link InsertTextMode}</td>
 *		<td>R</td>
 *      <td>no</td>
 *      <td>{@link InsertTextMode#BREAK}</td>
 *      <td>Defines how each line will be emitted.
 *      </td>
 *  </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed.  The component must not have a body.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class InsertText extends AbstractComponent
{
	private IBinding textBinding;
	private IBinding modeBinding;
	private InsertTextMode modeValue;

	public IBinding getTextBinding()
	{
		return textBinding;
	}

	public void setTextBinding(IBinding value)
	{
		textBinding = value;
	}

	public IBinding getModeBinding()
	{
		return modeBinding;
	}

	public void setModeBinding(IBinding value)
	{
		modeBinding = value;

		if (value.isStatic())
			modeValue = (InsertTextMode) value.getObject("mode", InsertTextMode.class);
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	
	{
		InsertTextMode mode = modeValue;
		String text;
		StringReader reader = null;
		LineNumberReader lineReader = null;
		int lineNumber = 0;
		String line;

		if (textBinding == null)
			return;

		text = textBinding.getString();
		if (text == null)
			return;

		if (mode == null && modeBinding != null)
			mode = (InsertTextMode) modeBinding.getObject("mode", InsertTextMode.class);

		if (mode == null)
			mode = InsertTextMode.BREAK;

		try
		
			{
			reader = new StringReader(text);

			lineReader = new LineNumberReader(reader);

			while (true)
			{
				line = lineReader.readLine();

				// Exit loop at end of file.

				if (line == null)
					break;

				mode.writeLine(lineNumber, line, writer);

				lineNumber++;
			}

		}
		catch (IOException ex)
		{
			throw new RequestCycleException(
				"Error converting text to lines (for InsertText).",
				this,
				ex);
		}
		finally
		{
			close(lineReader);
			close(reader);
		}

	}

	private void close(Reader reader)
	{
		if (reader == null)
			return;

		try
		{
			reader.close();
		}
		catch (IOException e)
		{
		}
	}
}