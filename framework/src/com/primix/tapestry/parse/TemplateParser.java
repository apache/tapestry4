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

package com.primix.tapestry.parse;

import java.util.*;

/**
 *  A new implementation of the template parser; this does <em>not</em>
 *  rely on GNU Regexp, and performs a much more complex parsing job.
 *  This parser supports the &lt;jwc id="<i>id</id>"&gt; syntax (standard
 *  through release 1.0.1).    In addition, any HTML tag can become
 *  the equivalent of a &lt;jwc&gt; tag by including a <code>jwcid</code>
 *  attribute.
 *
 *  <p>The parser edits out HTML comments, removes
 *  the body of some tags (when the corresponding component doesn't
 *  allow a body), identifies more template errors, and allows
 *  portions of the template to be completely removed.
 *
 *  <p>The parser does a more thorough lexical analysis of the template,
 *  and reports a great number of errors, including improper nesting
 *  of tags.
 *
 *  <p>The parser identifies attributes in dynamic tags (ordinary 
 *  tags with the <code>jwcid</code> attribute) and records them,
 *  where they later become static bindings on the component.
 *
 *  <p>Although the &lt;jwc&gt; tag is still supported (and will always
 *  be), it can now be avoided by using the jwcid attribute on
 *  existing tags, such as &lt;span&gt;.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class TemplateParser
{
	/**
	 *  A "magic" component id that causes the tag with the id and its entire
	 *  body to be ignored during parsing.
	 *
	 */

	private static final String REMOVE_ID = "$remove$";

	/**
	 * A "magic" component id that causes the tag to represent the true
	 * content of the template.  Any content prior to the tag is discarded,
	 * and any content after the tag is ignored.  The tag itself is not
	 * included.
	 *
	 */

	private static final String CONTENT_ID = "$content$";

	private ITemplateParserDelegate delegate;

	/**
	 *  Identifies the template being parsed; used with error messages.
	 *
	 */

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
		// The element, i.e., <jwc> or virtually any other element (via jwcid attribute)
		String tagName;
		// If true, the tag is a placeholder for a dynamic element
		boolean component;
		// If true, the body of the tag is being ignored, and the
		// ignore flag is cleared when the close tag is reached
		boolean ignoringBody;
		// If true, then the entire tag (and its body) is being ignored
		boolean removeTag;
		// If true, then the tag must have a balanced closing tag.
		// This is always true for components.
		boolean mustBalance;
		// The line on which the start tag exists
		int line;
		// If true, then the parse ends when the closing tag is found.
		boolean content;

		Tag(String tagName, int line)
		{
			this.tagName = tagName;
			this.line = line;
		}

		boolean match(String matchTagName)
		{
			return tagName.equalsIgnoreCase(matchTagName);
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
	 *  Set to true when the body of a tag is being ignored.  This is typically
	 *  used to skip over the body of a tag when its corresponding
	 *  component doesn't allow a body, or whe the special
	 *  jwcid of $remove$ is used.
	 *
	 */

	private boolean ignoring;

	/**
	 *  A {@link Map} of {@link Strings}, used to store attributes collected
	 *  while parsing a tag.
	 *
	 */

	private Map attributes = new HashMap();

	/**
	 *  Parses the template data into an array of {@link TemplateToken}s.
	 *
	 *  <p>The parser is very much not threadsafe, so care should be taken
	 *  that only a single thread accesses it.
	 *
	 *  @param templateData the HTML template to parse.  Some tokens will hold
	 *  a reference to this array.
	 *  @param delegate delegate object that "knows" about components
	 *  @param resourcePath a description of where the template originated from,
	 *  used with error messages.
	 *
	 */

	public TemplateToken[] parse(
		char[] templateData,
		ITemplateParserDelegate delegate,
		String resourcePath)
		throws TemplateParseException
	{
		TemplateToken[] result = null;

		try
		{
			this.templateData = templateData;
			this.resourcePath = resourcePath;
			this.delegate = delegate;
			ignoring = false;
			line = 1;

			parse();

			result = (TemplateToken[]) tokens.toArray(new TemplateToken[tokens.size()]);
		}
		finally
		{
			this.delegate = null;
			this.templateData = null;
			this.resourcePath = null;
			stack.clear();
			tokens.clear();
			attributes.clear();
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

	private void parse() throws TemplateParseException
	{
		cursor = 0;
		blockStart = -1;
		int length = templateData.length;

		while (cursor < length)
		{
			if (templateData[cursor] != '<')
			{
				if (blockStart < 0 && !ignoring)
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

	/**
	 *  Advance forward in the document until the end of the comment is reached.
	 *  In addition, skip any whitespace following the comment.
	 *
	 */

	private void skipComment() throws TemplateParseException
	{
		int length = templateData.length;
		int startLine = line;

		if (blockStart < 0 && !ignoring)
			blockStart = cursor;

		while (true)
		{
			if (cursor >= length)
				throw new TemplateParseException(
					"Comment on line " + startLine + " did not end.",
					startLine,
					resourcePath);

			if (lookahead(COMMENT_END))
				break;

			// Not the end of the comment, advance over it.

			advance();
		}

		cursor += COMMENT_END.length;
		advanceOverWhitespace();
	}

	private void addTextToken(int end)
	{
		// No active block to add to.

		if (blockStart < 0)
			return;

		if (blockStart <= end)
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

	private void startTag() throws TemplateParseException
	{
		int cursorStart = cursor;
		int length = templateData.length;
		String tagName = null;
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
				tagName = new String(templateData, cursorStart + 1, cursor - cursorStart - 1);

				break;
			}

			advance();
		}

		boolean isJwcTag = (tagName != null && tagName.equalsIgnoreCase("jwc"));
		String jwcId = null;
		String jwcIdAttributeName = isJwcTag ? "id" : "jwcid";
		String attributeName = null;
		int attributeNameStart = -1;
		int attributeValueStart = -1;
		int state = WAIT_FOR_ATTRIBUTE_NAME;
		char quoteChar = 0;

		attributes.clear();

		// Collect each attribute

		while (!endOfTag)
		{
			if (cursor >= length)
			{
				String message = tagName == null ? "Tag" : "Tag <" + tagName + ">";

				throw new TemplateParseException(
					message + " on line " + startLine + " is never closed.",
					startLine,
					resourcePath);
			}

			char ch = templateData[cursor];

			switch (state)
			{
				case WAIT_FOR_ATTRIBUTE_NAME :

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

				case COLLECT_ATTRIBUTE_NAME :

					// Looking for end of attribute name.

					if (ch == '=' || ch == '/' || ch == '>' || Character.isWhitespace(ch))
					{
						attributeName =
							new String(templateData, attributeNameStart, cursor - attributeNameStart);

						if (isJwcTag && !attributeName.equalsIgnoreCase(jwcIdAttributeName))
							throw new TemplateParseException(
								"Tag <"
									+ tagName
									+ "> on line "
									+ startLine
									+ " may only contain attribute '"
									+ jwcIdAttributeName
									+ "'.",
								startLine,
								resourcePath);

						state = ADVANCE_PAST_EQUALS;
						break;
					}

					// Part of the attribute name

					advance();
					break;

				case ADVANCE_PAST_EQUALS :

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

				case WAIT_FOR_ATTRIBUTE_VALUE :

					if (ch == '/' || ch == '>')
						throw new TemplateParseException(
							"Tag <"
								+ tagName
								+ "> is missing a value for attribute "
								+ attributeName
								+ " on line "
								+ line
								+ ".",
							line,
							resourcePath);

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

				case COLLECT_QUOTED_VALUE :

					// Start collecting the quoted attribute value.  Stop at the matching quote character,
					// unless bare, in which case, stop at the next whitespace.

					if (ch == quoteChar)
					{
						String attributeValue =
							new String(templateData, attributeValueStart, cursor - attributeValueStart);

						if (attributeName.equalsIgnoreCase(jwcIdAttributeName))
							jwcId = attributeValue;
						else
							attributes.put(attributeName, attributeValue);

						// Advance over the quote.
						advance();
						state = WAIT_FOR_ATTRIBUTE_NAME;
						break;
					}

					advance();
					break;

				case COLLECT_UNQUOTED_VALUE :

					// An unquoted attribute value ends with whitespace 
					// or the end of the enclosing tag.

					if (ch == '/' || ch == '>' || Character.isWhitespace(ch))
					{
						String attributeValue =
							new String(templateData, attributeValueStart, cursor - attributeValueStart);

						if (attributeName.equalsIgnoreCase(jwcIdAttributeName))
							jwcId = attributeValue;
						else
							attributes.put(attributeName, attributeValue);

						state = WAIT_FOR_ATTRIBUTE_NAME;
						break;
					}

					advance();
					break;
			}
		}

		if (isJwcTag && jwcId == null)
			throw new TemplateParseException(
				"Tag <" + tagName + "> on line " + startLine + " does not specify an id.",
				startLine,
				resourcePath);

		if (jwcId != null)
		{
			if (jwcId.equalsIgnoreCase(CONTENT_ID))
			{
				if (ignoring)
					throw new TemplateParseException(
						"Tag <"
							+ tagName
							+ "> on line "
							+ startLine
							+ " is the template content, and may not be in an ignored block.",
						startLine,
						resourcePath);

				if (emptyTag)
					throw new TemplateParseException(
						"Tag <"
							+ tagName
							+ "> on line "
							+ startLine
							+ " is the template content, and may not be empty.",
						startLine,
						resourcePath);

				tokens.clear();
				blockStart = -1;

				Tag tag = new Tag(tagName, startLine);

				tag.mustBalance = true;
				tag.content = true;

				stack.clear();
				stack.add(tag);

				advance();

				return;
			}

			boolean isRemoveId = jwcId.equalsIgnoreCase(REMOVE_ID);

			if (!(isRemoveId || delegate.getKnownComponent(jwcId)))
				throw new TemplateParseException(
					"Tag <"
						+ tagName
						+ "> on line "
						+ startLine
						+ " references unknown component id '"
						+ jwcId
						+ "'.",
					startLine,
					resourcePath);

			if (ignoring && !isRemoveId)
				throw new TemplateParseException(
					"Tag <"
						+ tagName
						+ "> on line "
						+ startLine
						+ " is a dynamic component, and may not appear inside an ignored block.",
					startLine,
					resourcePath);

			// Ignore the body if we're removing the entire tag,
			// of if the corresponding component doesn't allow
			// a body.

			boolean ignoreBody = !emptyTag && (isRemoveId || !delegate.getAllowBody(jwcId));

			if (ignoring && ignoreBody)
				throw new TemplateParseException(
					"Tag <"
						+ tagName
						+ "> on line "
						+ startLine
						+ " should be ignored, but is already inside an ignored block (ignored blocks may not be nested).",
					startLine,
					resourcePath);

			if (!emptyTag)
			{
				Tag tag = new Tag(tagName, startLine);

				tag.component = !isRemoveId;
				tag.removeTag = isRemoveId;

				tag.ignoringBody = ignoreBody;

				ignoring = tag.ignoringBody;

				tag.mustBalance = true;

				stack.add(tag);
			}

			// End any open block.

			addTextToken(cursorStart - 1);

			if (!isRemoveId)
			{
				if (attributes.isEmpty())
					tokens.add(new TemplateToken(jwcId, tagName));
				else
					tokens.add(new TemplateToken(jwcId, tagName, new HashMap(attributes)));

				if (emptyTag)
					tokens.add(new TemplateToken(TokenType.CLOSE, tagName));
			}

			advance();

			return;
		}

		// A static tag (not a <jwc> tag, or an ordinary tag without a jwcid attribute).
		// We need to record this so that we can match close tags later.

		if (!emptyTag)
		{
			Tag tag = new Tag(tagName, startLine);
			stack.add(tag);
		}

		// If there wasn't an active block, then start one.

		if (blockStart < 0 && !ignoring)
			blockStart = cursorStart;

		advance();
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

	private void closeTag() throws TemplateParseException
	{
		int cursorStart = cursor;
		int length = templateData.length;
		int startLine = line;

		cursor += CLOSE_TAG.length;

		int tagStart = cursor;

		while (true)
		{
			if (cursor >= length)
				throw new TemplateParseException(
					"Incomplete close tag on line " + startLine + ".",
					startLine,
					resourcePath);

			char ch = templateData[cursor];

			if (ch == '>')
				break;

			advance();
		}

		String tagName = new String(templateData, tagStart, cursor - tagStart);

		int stackPos = stack.size() - 1;
		Tag tag = null;

		while (stackPos >= 0)
		{
			tag = (Tag) stack.get(stackPos);

			if (tag.match(tagName))
				break;

			if (tag.mustBalance)
				throw new TemplateParseException(
					"Closing tag </"
						+ tagName
						+ "> on line "
						+ startLine
						+ " is improperly nested with tag <"
						+ tag.tagName
						+ "> on line "
						+ tag.line
						+ ".",
					startLine,
					resourcePath);

			stackPos--;
		}

		if (stackPos < 0)
			throw new TemplateParseException(
				"Closing tag </"
					+ tagName
					+ "> on line "
					+ startLine
					+ " does not have a matching opening tag.",
				startLine,
				resourcePath);

		// Special case for the content tag

		if (tag.content)
		{
			addTextToken(cursorStart - 1);

			// Advance the cursor right to the end.

			cursor = length;
			stack.clear();
			return;
		}

		// When a component closes, add a CLOSE tag.
		if (tag.component)
		{
			addTextToken(cursorStart - 1);

			tokens.add(new TemplateToken(TokenType.CLOSE, tagName));
		}
		else
		{
			// The close of a static tag.  Unless removing the tag
			// entirely, make sure the block tag is part of a text block.

			if (blockStart < 0 && !tag.removeTag && !ignoring)
				blockStart = cursorStart;
		}

		// Remove all elements at stackPos or above.

		for (int i = stack.size() - 1; i >= stackPos; i--)
			stack.remove(i);

		// Advance cursor past '>'

		advance();

		// If editting out the tag (i.e., $remove$) then kill any whitespace.
		// For components that simply don't contain a body, removeTag will
		// be false.

		if (tag.removeTag)
			advanceOverWhitespace();

		// If we were ignoring the body of the tag, then clear the ignoring
		// flag, since we're out of the body.

		if (tag.ignoringBody)
			ignoring = false;
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

			if (cursor < length && templateData[cursor] == '\n')
				cursor++;

			return;
		}

		// Not an end-of-line character.

	}

	private void advanceOverWhitespace()
	{
		int length = templateData.length;

		while (cursor < length)
		{
			char ch = templateData[cursor];
			if (!Character.isWhitespace(ch))
				return;

			advance();
		}
	}
}