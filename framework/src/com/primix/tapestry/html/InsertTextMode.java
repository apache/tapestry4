/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
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

import com.primix.tapestry.IResponseWriter;
import com.primix.tapestry.util.Enum;

/**
 *  Defines a number of ways to format multi-line text for proper
 *  renderring.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class InsertTextMode extends Enum
{
	/**
	 *  Mode where each line (after the first) is preceded by a &lt;br&gt; tag.
	 *
	 **/

	public static final InsertTextMode BREAK = new BreakMode();

	/**
	 *  Mode where each line is wrapped with a &lt;p&gt; element.
	 *
	 **/

	public static final InsertTextMode PARAGRAPH = new ParagraphMode();

	protected InsertTextMode(String enumerationId)
	{
		super(enumerationId);
	}

	/**
	*  Invoked by the {@link InsertText} component to write the next line.
	*
	*  @param lineNumber the line number of the line, starting with 0 for the first line.
	*  @param line the String for the current line.
	*  @param writer the {@link IResponseWriter} to send output to.
	**/

	public abstract void writeLine(
		int lineNumber,
		String line,
		IResponseWriter writer);

	private static class BreakMode extends InsertTextMode
	{
		private BreakMode()
		{
			super("BREAK");
		}

		public void writeLine(int lineNumber, String line, IResponseWriter writer)
		{
			if (lineNumber > 0)
				writer.beginEmpty("br");

			writer.print(line);
		}
	}

	private static class ParagraphMode extends InsertTextMode
	{
		private ParagraphMode()
		{
			super("PARAGRAPH");
		}

		public void writeLine(int lineNumber, String line, IResponseWriter writer)
		{
			writer.begin("p");

			writer.print(line);

			writer.end();
		}
	}

}