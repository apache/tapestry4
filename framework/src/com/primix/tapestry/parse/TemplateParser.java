package com.primix.tapestry.parse;

import gnu.regexp.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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

import java.util.*;

/**
 *  Parses an HTML template file that has been read in as a String.
 *  This produces a stream of {@link TemplateToken}s representing
 *  the different tokens visible in the stream.
 *
 *  <p>The following {@link TokenType token types} are produced:
 *
 *  <table border=1>
 *  <tr><th>Type</th> <th>Description</th></tr>
 *
 * <tr> 
 *   <td>OPEN</td>
 *   <td>Corresponds to a &lt;jwc&gt; tag.  The length corresponds to the number
 *  of characters between the '&lt;' and '&gt;' characters. </td></tr>
 *
 * <tr>
 *  <td>CLOSE</td>
 *  <td>Corresponds to the &lt;/jwc&gt; tag.  The length is the number of characters
 * in the tag, or zero if the OPEN tag implicitly closed the tag (with a trailing
 * slash before the '&gt;').  </td></tr>
 *
 *  <tr>
 *  <td>TEXT</td>
 *  <td><em>Any</em> other text is passed through as raw HTML text.
 *  </td></tr></table>
 *
 *  <p>This is a new implementation, built around using the GNU Regular Expression library
 *  (version 1.0.8).
 *
 * @author Howard Ship
 * @version $Id$
 *
 */

public class TemplateParser
{
	/**
	*  Local reference to the template data used while parsing.
	*
	*/

	private char[] templateData;

	private List tokens = new ArrayList();

	/**
	*  The regular expression used to identify <jwc> and </jwc> tags.
	*
	*/

	private RE re;

	/**
	*  The magic pattern used to build the <code>RE</code> instance.
	*
	*/

	private static final String
		pattern = "(<\\s*jwc\\s+id\\s*=('|\")\\s*(.*?)\\s*(\\2)\\s*(/)?\\s*>)" + 
		"|" +
		"(<\\s*/\\s*jwc\\s*>)";

	// Substring	Meaning
	// 1			Matched <jwc> tag
	// 2			Quote character (single or double)
	// 3			Contents of id attribute
	// 4			Matching quote
	// 5		    Optional tag-closing slash
	// 6			Matched </jwc> tag

	//	Note on 3: .*? means stingy ... fewest characters that can suffice.
	//	Without it, two <jwc> tags on the same line don't get parsed
	//	correctly.

	public TemplateParser()
	{
		try
		{
			re = new RE(pattern, RE.REG_ICASE);
		}
		catch (Exception e)
		{
		}
	}

	private void add(TemplateToken token)
	{
		tokens.add(token);
	}

	protected void assemble(char[] templateData)
	{
		REMatch[] matches;
		REMatch match;
		int thisStart;
		int thisEnd;
		int idStart;
		int idEnd;
		int i;
		int lastEnd = -1;

		matches = re.getAllMatches(templateData);

		for (i = 0; i < matches.length; i++)
		{
			match = matches[i];

			thisStart = match.getStartIndex();

			// getEndIndex() actually gives the offset of the next character not
			// in the match string (this works well with String.substring() but
			// isn't what we need).  We convert it to the offset to the last
			// character in the match.

			thisEnd = match.getEndIndex() - 1;

			// If there are any text characters between the end of the last match
			// and the start of the next match, then we need to capture those as a TEXT
			// token.

			if (thisStart - lastEnd > 1)
				add(new TemplateToken(templateData, lastEnd + 1, thisStart - 1));

			// The 3rd subexpression is the id string, with leading and trailing
			// whitespace trimmed.  This indicates a <jwc> tag.

			idStart = match.getStartIndex(3);
			if (idStart >= 0)
			{
				idEnd = match.getEndIndex(3);

				// Extract the value of the id attribute from the <jwc> tag and
				// use it to form a new OPEN token.

				add(new TemplateToken(match.toString(3), thisStart, thisEnd));

				// The 4th subexpression is the closing (XML-style) slash.  It's optional.

				if (match.getStartIndex(5) >= 0)
					add(new TemplateToken(thisStart, thisEnd));

			}
			else
			{
				add(new TemplateToken(thisStart, thisEnd));
			}

			lastEnd = thisEnd;
		}

		// Lastly, check to see if anything follows the final close, and add that as a new
		// TEXT as well.

		if (templateData.length - lastEnd > 1)
		{
			add(new TemplateToken(templateData, lastEnd + 1, templateData.length - 1));
		}
	}

	public TemplateToken[] parse(char[] templateData)
	{
		TemplateToken[] result = null;

		// Setup to deal.

		tokens.clear();

		try
		{
			assemble(templateData);

			result = new TemplateToken[tokens.size()];

			result = (TemplateToken[])tokens.toArray(result);
		}
		finally
		{
			tokens.clear();
		}

		return result;
	}
}


