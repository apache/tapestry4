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

package net.sf.tapestry.junit.parse;

import junit.framework.*;
import com.primix.tapestry.parse.*;
import java.io.*;
import java.util.*;

/**
 *  Tests for the Tapestry HTML template parser.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class TemplateParserTest
	extends TestCase
{
	private TemplateParser parser;
	
	private static class ParserDelegate
		implements ITemplateParserDelegate
	{
		public boolean getKnownComponent(String componentId)
		{
			return true;
		}
		
		public boolean getAllowBody(String componentId)
		{
			return true;
		}
	}
	
	public TemplateParserTest(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		return new TestSuite(TemplateParserTest.class);
	}
	
	protected void setUp()
	{
		parser = new TemplateParser();
	}
	
	protected void tearDown()
	{
		parser = null;
	}	
	
	protected TemplateToken[] run(char[] templateData, ITemplateParserDelegate delegate, String resourcePath)
		throws TemplateParseException
	{
		return parser.parse(templateData, delegate, resourcePath);
	}
	
	protected TemplateToken[] run(InputStream stream, 
			ITemplateParserDelegate delegate, String resourcePath)
		throws TemplateParseException
	{
		StringBuffer buffer = new StringBuffer();
		char[] block = new char[1000];
		InputStreamReader reader = new InputStreamReader(stream);
		
		try
		{
			while (true)
			{
				int count = reader.read(block, 0, block.length);
				
				if (count < 0)
					break;
				
				buffer.append(block, 0, count);
			}
			
			reader.close();
		}
		catch (IOException ex)
		{
			throw new AssertionFailedError("Unable to read from stream.");
		}
		
		return run(buffer.toString().toCharArray(), delegate, resourcePath);
	}
	
	protected TemplateToken[] run(String file)
		throws TemplateParseException
	{
		return run(file, new ParserDelegate());
	}
	
	
	protected TemplateToken[] run(String file, ITemplateParserDelegate delegate)
		throws TemplateParseException
	{
		InputStream stream = getClass().getResourceAsStream(file);
		
		if (stream == null)
			throw new TemplateParseException("File " + file + " not found.");
		
		return run(stream, delegate, file);
	}
	
	protected void assertTextToken(TemplateToken token, int startIndex, int endIndex)
	{
		assertEquals("Text token type.", TokenType.TEXT, token.getType());
		assertEquals("Text token start index.", startIndex, token.getStartIndex());
		assertEquals("Text token end index.", endIndex, token.getEndIndex());
	}
	
	protected void assertOpenToken(TemplateToken token, String id)
	{
		assertEquals("Open token type.", TokenType.OPEN, token.getType());
		assertEquals("Open token id.", id, token.getId());
	}
	
	protected void assertCloseToken(TemplateToken token)
	{
		assertEquals("Close token type.", TokenType.CLOSE, token.getType());
	}
	
	protected void assertTokenCount(TemplateToken[] tokens, int count)
	{
		assertNotNull("Parsed tokens.", tokens);
		assertEquals("Parsed token count.", count, tokens.length);
	}
	
	private void runFailure(String file, String message)
	{
		runFailure(file, new ParserDelegate(), message);
	}
	
	private void runFailure(String file, ITemplateParserDelegate delegate, String message)
	{
		try
		{
			run(file, delegate);
			
			throw new AssertionFailedError("Invalid document " + file + " parsed without exception.");
		}
		catch (TemplateParseException ex)
		{
			assertEquals(message, ex.getMessage());
			assertEquals(file, ex.getResourcePath());
		}
	}
	
	public void testAllStatic()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("AllStatic.html");
		
		assertTokenCount(tokens, 1);
		assertTextToken(tokens[0], 0, 184);
	}
	
	public void testSingleEmptyTag()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("SingleEmptyTag.html");
		
		assertTokenCount(tokens, 4);
		
		assertTextToken(tokens[0], 0, 39);
		assertOpenToken(tokens[1], "emptyTag");
		assertCloseToken(tokens[2]);
		assertTextToken(tokens[3], 60, 101);
	}
	
	public void testSimpleNested()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("SimpleNested.html");
		
		assertTokenCount(tokens, 8);
		assertOpenToken(tokens[1], "outer");
		assertOpenToken(tokens[3], "inner");
		assertCloseToken(tokens[4]);
		assertCloseToken(tokens[6]);
	}
	
	public void testMixedNesting()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("MixedNesting.html");
		
		assertTokenCount(tokens, 5);
		assertOpenToken(tokens[1], "row");
		assertCloseToken(tokens[3]);
	}
	
	public void testSingleQuotes()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("SingleQuotes.html");
		
		assertTokenCount(tokens, 7);
		assertOpenToken(tokens[1], "first");
		assertOpenToken(tokens[4], "second");
	}
	
	public void testComplex()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("Complex.html");
		
		assertTokenCount(tokens, 19);
		
		// Just pick a few highlights out of it.
		
		assertOpenToken(tokens[1], "ifData");
		assertOpenToken(tokens[3], "e");
		assertOpenToken(tokens[5], "row");
	}
	
	public void testStartWithStaticTag()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("StartWithStaticTag.html");
		
		assertTokenCount(tokens, 4);
		assertTextToken(tokens[0], 0, 240);
		assertOpenToken(tokens[1], "justBecause");
	}
	
	public void testUnterminatedCommentFailure()
	{
		runFailure("UnterminatedComment.html", "Comment on line 3 did not end.");
	}
	
	public void testUnclosedOpenTagFailure()
	{
		runFailure("UnclosedOpenTag.html", "Tag <body> on line 4 is never closed.");
	}
	
	public void testMissignAttributeValueFailure()
	{
		runFailure("MissingAttributeValue.html",
				"Tag <img> is missing a value for attribute src on line 9.");
	}
	
	public void testMissingJwcIdFailure()
	{
		runFailure("MissingJwcId.html",
				"Tag <jWc> on line 7 does not specify an id.");
	}
	
	public void testIncompleteCloseFailure()
	{
		runFailure("IncompleteClose.html",
				"Incomplete close tag on line 6.");
	}
	
	public void testMismatchedCloseTagsFailure()
	{
		runFailure("MismatchedCloseTags.html",
				"Closing tag </th> on line 9 does not have a matching opening tag.");
	}
	
	public void testInvalidDynamicNestingFailure()
	{
		runFailure("InvalidDynamicNesting.html", 
				"Closing tag </body> on line 12 is improperly nested with tag <jwc> on line 8.");
	}
	
	public void testUnknownComponentIdFailure()
	{
		ITemplateParserDelegate delegate = 
			new ITemplateParserDelegate()
		{
			public boolean getKnownComponent(String componentId)
			{
				return !componentId.equals("row");
			}
			
			public boolean getAllowBody(String componentId)
			{
				return true;
			}
		};
		
		runFailure("Complex.html", delegate, 
				"Tag <tr> on line 11 references unknown component id 'row'.");
	}
	
	public void testBasicRemove()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("BasicRemove.html");
		
		assertTokenCount(tokens, 11);
		assertTextToken(tokens[0], 0, 126);
		assertTextToken(tokens[1], 197, 287);
		assertTextToken(tokens[2], 345, 346);
		assertOpenToken(tokens[3], "e");
		assertTextToken(tokens[4], 359, 361);
		assertOpenToken(tokens[5], "row");
		assertTextToken(tokens[6], 378, 398);
		assertCloseToken(tokens[7]);
		assertTextToken(tokens[8], 404, 405);
		assertCloseToken(tokens[9]);
		assertTextToken(tokens[10], 412, 425);
	}
	
	public void testBodyRemove()
		throws TemplateParseException
	{
		ITemplateParserDelegate delegate = new ITemplateParserDelegate()
		{
			public boolean getKnownComponent(String id)
			{
				return true;
			}
			
			public boolean getAllowBody(String id)
			{
				return id.equals("form");
			}
		};
		
		TemplateToken[] tokens = run("BodyRemove.html", delegate);
		
		assertTokenCount(tokens, 8);
		assertOpenToken(tokens[1], "form");
		assertOpenToken(tokens[3], "inputType");
		assertCloseToken(tokens[4]);
		assertTextToken(tokens[5], 253, 254);
		assertCloseToken(tokens[6]);		
	}
	
	public void testRemovedComponentFailure()
	{
		runFailure("RemovedComponent.html",
				"Tag <jwc> on line 5 is a dynamic component, " +
					"and may not appear inside an ignored block.");
	}
	
	public void testNestedRemoveFailure()
	{
		runFailure("NestedRemove.html",
				"Tag <jwc> on line 4 should be ignored, but is already inside " +
					"an ignored block (ignored blocks may not be nested).");
	}
	
	public void testInvalidJwcAttributeFailure()
	{
		runFailure("InvalidJwcAttribute.html",
				"Tag <jwc> on line 5 may only contain attribute 'id'.");
	}
	
	public void testBasicContent()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("BasicContent.html");
		
		assertTokenCount(tokens, 4);
		assertTextToken(tokens[0], 112, 173);
		assertOpenToken(tokens[1], "nested");
		assertCloseToken(tokens[2]);
		assertTextToken(tokens[3], 192, 198);
	}
	
	public void testIgnoredContentFailure()
	{
		runFailure("IgnoredContent.html",
				"Tag <td> on line 7 is the template content, and may not be in an ignored block.");
	}
	
	public void testTagAttributes()
		throws TemplateParseException
	{
		TemplateToken[] tokens = run("TagAttributes.html");
		
		assertTokenCount(tokens, 5);
		assertOpenToken(tokens[1], "tag");
		
		Map a = tokens[1].getAttributes();
		
		assertEquals("Attribute count", 3, a.size());
		assertEquals("zip", a.get("class"));
		assertEquals("right", a.get("align"));
		assertEquals("#ff00ff", a.get("color"));
	}
}
