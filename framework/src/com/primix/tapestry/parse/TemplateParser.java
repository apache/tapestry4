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

package com.primix.tapestry.parse;

import java.util.*;

/**
 *  A new implementation of the template parser; this does <em>not</em>
 *  rely on GNU Regexp, and performs a much more complex parsing job.
 *  This parser supports the &lt;jwc id="<i>id</id>"&gt; syntax (standard
 *  through release 1.0.1).    In addition, any HTML tag can become
 *  the equivalent of a &lt;jwc&gt; tag by including a <code>jwcid</code>
 *  attribute.  In addition, this new parser edits out HTML comments.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class TemplateParser
{
	private String resourcePath;
	
	/**
	 *  Local reference to the template data that is to be parsed.
	 *
	 */
	
	private char[] templateData;
	
	/**
	 *  List of Tag
	 *
	 */
	
	private List stack = new ArrayList();
	
	private static class Tag
	{
		String tagId;
		boolean dynamic;
		
		Tag(String tagId, boolean dynamic)
		{
			this.tagId = tagId;
			this.dynamic = dynamic;
		}
		
		boolean match(String matchTagId)
		{
			return tagId.equalsIgnoreCase(matchTagId);
		}
	}
	
	/**
	 *  List of {@link TemplateToken}, this forms the ultimate response.
	 *
	 */
	
	private List tokens = new ArrayList();
	
	/**
	 *  The location of the 'cursor' within the template data.  The
	 *  advance() method moves this forward.
	 *
	 */
	
	private int cursor;
	
	/**
	 *  The start of the current block of static text, or -1 if no block
	 *  is active.
	 *
	 */
	
	private int blockStart;
	
	/**
	 *  The current line number; tracked by advance().  Starts at 1.
	 *
	 */
	
	private int line;
	
	/**
	 *  Parses the template data into an array of {@link TemplateToken}s.
	 *
	 *  @param templateData the HTML template to parse.  Some tokens will hold
	 *  a reference to this array.
	 *  @param resourcePath a description of where the template originated from,
	 *  used with error messages.
	 *
	 */
	
	public TemplateToken[] parse(char[] templateData, String resourcePath)
		throws TemplateParseException
	{
		TemplateToken[] result = null;
		
		try
		{
			this.templateData = templateData;
			this.resourcePath = resourcePath;
			line = 1;
			
			parse();
			
			result = (TemplateToken[])tokens.toArray(new TemplateToken[tokens.size()]);
		}
		finally
		{
			this.templateData = null;
			this.resourcePath = null;
			stack.clear();
			tokens.clear();
		}
		
		return result;
	}
	
	/**
	 *  Checks to see if the next few characters match a given pattern.
	 *
	 */
	
	private boolean lookahead(char[] match)
	{
		try
		{
			for (int i = 0; i < match.length; i++)
			{
				if (templateData[cursor + i] != match[i])
					return false;
			}
	
			// Every character matched.
			
			return true;
		}
		catch (IndexOutOfBoundsException ex)
		{
			return false;
		}
	}
	
	private static final char[] COMMENT_START = new char[] { '<', '!', '-', '-' };
	private static final char[] COMMENT_END = new char[] { '-', '-', '>' };
	private static final char[] CLOSE_TAG = new char[] { '<', '/' };
	private static final char[] CLOSE_EMPTY_TAG = new char[] { '/', '>'};

	private void parse()
		throws TemplateParseException
	{
		cursor = 0;
		blockStart = -1;
		int length = templateData.length;
		
		while (cursor < length)
		{
			if (templateData[cursor] != '<')
			{
				if (blockStart < 0)
					blockStart = cursor;
				
				advance();
				continue;				
			}
			
			// OK, start of something.
			
			if (lookahead(CLOSE_TAG))
			{
				closeTag();
				continue;
			}
			
			if (lookahead(COMMENT_START))
			{
				// The start of a comment ends the current text block.  That is, we edit out
				// comments as we parse.
				
				addTextToken(cursor - 1);
				
				skipComment();
				continue;
			}
			
			// The start of some tag.
			
			startTag();
		}
		
		// Usually there's some text at the end of the template (after the last closing tag) that should
		// be added.  Often the last few tags are static tags so we definately
		// need to end the text block.
		
		addTextToken(templateData.length - 1);
	}
	
	private void skipComment()
		throws TemplateParseException
	{
		int length = templateData.length;
		int startLine = line;
		
		while (cursor < length)
		{
			if (lookahead(COMMENT_END))
			{
				cursor += COMMENT_END.length;
				return;
			}
			
			// Not the end of the comment, advance over it.
			
			advance();
		}
		
		throw new TemplateParseException(
			"Comment on line " + startLine + " did not end.",
			startLine, resourcePath);
	}
	
	private void addTextToken(int end)
	{
		// No active block to add to.
		
		if (blockStart < 0)
			return;
		
		if (blockStart != end)
		{
			TemplateToken token = new TemplateToken(templateData, blockStart, end);
			
			tokens.add(token);
		}
		
		blockStart = -1;
	}
	
	private static final int WAIT_FOR_ATTRIBUTE_NAME = 0;
	private static final int COLLECT_ATTRIBUTE_NAME = 1;
	private static final int ADVANCE_PAST_EQUALS = 2;
	private static final int WAIT_FOR_ATTRIBUTE_VALUE = 3;
	private static final int COLLECT_QUOTED_VALUE = 4;
	private static final int COLLECT_UNQUOTED_VALUE = 5;
	
	private void startTag()
		throws TemplateParseException
	{
		int cursorStart = cursor;
		int length = templateData.length;
		String tagId = null;
		boolean endOfTag = false;
		boolean emptyTag = false;
		int startLine = line;
		
		advance();
		
		// Collect the element type
		
		while (cursor < length)
		{
			char ch = templateData[cursor];
			
			if (ch == '/' || ch == '>' || Character.isWhitespace(ch))
			{
				tagId = new String(templateData, cursorStart + 1, cursor - cursorStart - 1);
				
				break;
			}
			
			advance();
		}
		
		boolean isJwcTag = (tagId != null && tagId.equalsIgnoreCase("jwc"));
		String jwcId = null;
		String jwcIdAttributeName = 
			isJwcTag ? "id" : "jwcid";
		String attributeName = null;
		String attributeValue = null;
		int attributeNameStart = -1;
		int attributeValueStart = -1;
		int state = WAIT_FOR_ATTRIBUTE_NAME;
		char quoteChar = 0;
		
		// Collect each attribute
		
		while (!endOfTag)
		{
			if (cursor >= length)
			{
				String message = 
					tagId == null 
					? "Tag"
					: "Tag <" + tagId + ">";
				
				throw new TemplateParseException(
					message + " on line " + startLine + " is never closed.", 
					startLine, resourcePath);
			}
			
			char ch = templateData[cursor];
			
			switch (state)
			{
				case WAIT_FOR_ATTRIBUTE_NAME:
					
					// Ignore whitespace before the next attribute name, while
					// looking for the end of the current tag.
					
					if (ch == '/')
					{
						emptyTag = true;
						advance();
						break;
					}
					
					if (ch == '>')
					{
						endOfTag = true;
						break;
					}
					
					if (Character.isWhitespace(ch))
					{
						advance();
						break;
					}
					
					// Found non-whitespace, assume its the attribute name.
					// Note: could use a check here for non-alpha.
					
					attributeNameStart = cursor;
					state = COLLECT_ATTRIBUTE_NAME;
					advance();
					break;
					
				case COLLECT_ATTRIBUTE_NAME:
					
					// Looking for end of attribute name.
					
					if (ch == '=' || ch == '/' || ch == '>' || Character.isWhitespace(ch))
					{
						attributeName = new String(templateData, attributeNameStart, cursor - attributeNameStart);
						state = ADVANCE_PAST_EQUALS;
						break;
					}
					
					// Part of the attribute name
					
					advance();
					break;
					
				case ADVANCE_PAST_EQUALS:
					
					// Looking for the '=' sign.  May hit the end of the tag, or (for bare attributes),
					// the next attribute name.
					
					if (ch == '/' || ch == '>')
					{
						// A bare attribute, which is not interesting to
						// us.
						
						state = WAIT_FOR_ATTRIBUTE_NAME;
						break;
					}
					
					if (Character.isWhitespace(ch))
					{
						advance();
						break;
					}
					
					if (ch == '=')
					{
						state = WAIT_FOR_ATTRIBUTE_VALUE;
						quoteChar = 0;
						attributeValueStart = -1;
						advance();
						break;
					}
					
					// Otherwise, an HTML style "bare" attribute (such as <select multiple>).
					// We aren't interested in those (we're just looking for the id or jwcid attribute).
					
					state = WAIT_FOR_ATTRIBUTE_NAME;
					break;
					
				case WAIT_FOR_ATTRIBUTE_VALUE:
					
					if (ch == '/' || ch == '>')
						throw new TemplateParseException(
							"Tag <" + tagId + "> is missing a value for attribute " + 
								attributeName + " on line " + line + ".",
							line, resourcePath);
					
					// Ignore whitespace between '=' and the attribute value.  Also, look
					// for initial quote.
					
					if (Character.isWhitespace(ch))
					{
						advance();
						break;
					}
					
					if (ch == '\'' || ch == '"')
					{
						quoteChar = ch;
						
						state = COLLECT_QUOTED_VALUE;
						advance();
						attributeValueStart = cursor;
						break;
					}
					
					// Not whitespace or quote, must be start of unquoted attribute.
					
					state = COLLECT_UNQUOTED_VALUE;
					attributeValueStart = cursor;
					break;
					
				case COLLECT_QUOTED_VALUE:
					
					// Start collecting the quoted attribute value.  Stop at the matching quote character,
					// unless bare, in which case, stop at the next whitespace.
					
					if (ch == quoteChar)
					{
						// We only care about the attribute value if the
						// attribute is interesting.
						
						if (attributeName.equalsIgnoreCase(jwcIdAttributeName))
							jwcId = new String(templateData, attributeValueStart,
									cursor - attributeValueStart);
						
						// Advance over the quote.
						advance();
						state = WAIT_FOR_ATTRIBUTE_NAME;
						break;
					}
					
					advance();
					break;
					
				case COLLECT_UNQUOTED_VALUE:
					
					// An unquoted attribute value ends with whitespace 
					// or the end of the enclosing tag.
					
					if (ch == '/' || ch== '>' || Character.isWhitespace(ch))
					{
						if (attributeName.equalsIgnoreCase(jwcIdAttributeName))
							jwcId = new String(templateData, attributeValueStart,
									cursor - attributeValueStart);
						
						state = WAIT_FOR_ATTRIBUTE_NAME;
						break;
					}	
					
					advance();
					break;
			}			
		}
		
		if (isJwcTag && jwcId == null)
			throw new TemplateParseException(
				"Tag <" + tagId + "> on line " + startLine + " does not specify an id.",
				startLine, resourcePath);
		
		if (jwcId != null)
		{
			if (!emptyTag)
				push(tagId, true);
			
			// End any open block.
			
			addTextToken(cursorStart - 1);
			
			tokens.add(new TemplateToken(jwcId));
			
			if (emptyTag)
				tokens.add(new TemplateToken(TokenType.CLOSE));
			
			advance();
			
			return;
		}
		
		// A static tag (not a <jwc> tag, or an ordinary tag with a jwcid attribute).
		// We need to record this so that we can match close tags later.
		
		if (!emptyTag)
			push(tagId, false);
		
		// If there wasn't an active block, then start one.
		
		if (blockStart < 0)
			blockStart = cursorStart;
		
		advance();
	}
	
	/**
	 *  Pushes a tag onto the stack of open tags.
	 *
	 */
	
	private void push(String tagId, boolean dynamic)
	{
		Tag tag = new Tag(tagId, dynamic);
		stack.add(tag);
	}
	
	/**
	 *  Invoked to handle a closing tag, i.e., &lt;/foo&gt;.  When a tag closes, it will match against
	 *  a tag on the open tag start.  Preferably the top tag on the stack (if everything is well balanced), but this
	 *  is HTML, not XML, so many tags won't balance.
	 *
	 *  <p>Once the matching tag is located, the question is ... is the tag dynamic or static?  If static, then
	 * the current text block is extended to include this close tag.  If dynamic, then the current text block
	 * is ended (before the '&lt;' that starts the tag) and a close token is added.
	 *
	 * <p>In either case, the matching static element and anything above it is removed, and the cursor is left
	 * on the character following the '&gt;'.
	 *
	 */
	
	private void closeTag()
		throws TemplateParseException
	{
		int cursorStart = cursor;
		int length = templateData.length;
		int startLine = line;
		
		cursor += CLOSE_TAG.length;
		
		int tagIdStart = cursor;
		
		while (true)
		{
			if (cursor >= length)
				throw new TemplateParseException("Incomplete close tag on line " +
							startLine + ".", startLine, resourcePath);
			
			char ch = templateData[cursor];
			
			if (ch == '>')
				break;
			
			advance();
		}
		
		String tagId = new String(templateData, tagIdStart, cursor - tagIdStart);
		
		int stackPos = stack.size() - 1;
		Tag tag = null;
		
		while (stackPos >= 0)
		{
			tag = (Tag)stack.get(stackPos);
			
			if (tag.match(tagId))
				break;
			
			stackPos--;
		}
		
		if (stackPos < 0)
			throw new TemplateParseException(
				"Closing tag </" + tagId + "> on line " + startLine +
					" does not have a matching opening tag.", startLine, resourcePath);
		
		if (tag.dynamic)
		{
			addTextToken(cursorStart - 1);
			
			tokens.add(new TemplateToken(TokenType.CLOSE));
		}
		else
		{
			if (blockStart < 0)
				blockStart = cursorStart;
		}
		
		// Remove all elements at stackPos or above.
		
		for (int i = stack.size() - 1; i >= stackPos; i--)
			stack.remove(i);
		
		// Advance cursor past '>'
		
		advance();
	}
	
	/**
	 *  Advances the cursor to the next character.
	 *  If the end-of-line is reached, then increments
	 *  the line counter.
	 *
	 */
	
	private void advance()
	{
		int length = templateData.length;
		
		if (cursor >= length)
			return;
		
		char ch = templateData[cursor];
		
		cursor++;
		
		if (ch == '\n')
		{
			line++;
			return;
		}
		
		// A \r, or a \r\n also counts as a new line.
		
		if (ch == '\r')
		{
			line++;
			
			if (cursor < length &&
					templateData[cursor] == '\n')
				cursor++;
			
			return;
		}
		
		// Not an end-of-line character.
		
	}
	
}

