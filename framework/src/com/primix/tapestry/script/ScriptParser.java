package com.primix.tapestry.script;

import com.primix.tapestry.*;
import com.primix.tapestry.components.html.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.*;
import com.primix.foundation.xml.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.apache.log4j.*;

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

/**
 *  Parses a Tapestry Script, an XML file defined by the public identifier
 *  <code>-//Primix Solutions//Tapestry Script 1.0//EN</code>.
 *
 *  <p>A Tapestry Script is used, in association with the 
 *  {@link Body} and/or {@link Script} components,
 *  to generate JavaScript for use with a Tapestry component.  Two seperate pieces
 *  of JavaScript can be generated.  The body section (associated with the <code>body</code>
 *  element of the XML document) is typically used to define JavaScript functions
 *  (most often, event handlers).  The initialization section
 *  (associated with the <code>initialization</code> element of the XML document)
 *  is used to add JavaScript that will be evaluated when the page finishes loading
 *  (i.e., from the HTML &lt;body&gt; element's onLoad event handler).
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class ScriptParser
extends AbstractDocumentParser
{

	private static final Category CAT = Category.getInstance(ScriptParser.class.getName());

	private static final int MAP_SIZE = 11;

	private Map symbolCache;

	public ParsedScript parse(InputStream stream, String resourcePath)
	throws DocumentParseException
	{
		InputSource source = new InputSource(stream);

		try
		{
			Document document = parse(source, resourcePath, "script"); 

			return build(document);

		}
		finally
		{
			setResourcePath(null);
		}
	}


	public ScriptParser()
	{		
		register("-//Primix Solutions//Tapestry Script 1.0//EN", 
			"Script_1_0.dtd");
	}


	private ParsedScript build(Document document)
	throws DocumentParseException
	{
		Element root;
		Node child;
		ParsedScript result = new ParsedScript(getResourcePath());

		root = document.getDocumentElement();

		for (child = root.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "let"))
			{
				result.addToken(buildLet(child));
				continue;
			}

			if (isElement(child, "body"))
			{
				result.addToken(buildBody(child));
				continue;
			}

			if (isElement(child, "initialization"))
			{
				result.addToken(buildInitialization(child));
				continue;
			}

			// Else, ignorable whitespace, I guess.
		}

		return result;
	}

	private IScriptToken buildLet(Node node)
	throws DocumentParseException
	{
		Element element = (Element)node;
		String key = element.getAttribute("key");

		IScriptToken token = new LetToken(key);
		build(token, node);

		return token;
	}

	private IScriptToken buildBody(Node node)
	throws DocumentParseException
	{
		IScriptToken token = new BodyToken();

		build(token, node);

		return token;
	}

	private IScriptToken buildInitialization(Node node)
	throws DocumentParseException
	{
		IScriptToken token = new InitToken();

		build(token, node);

		return token;
	}


	private void build(IScriptToken token, Node node)
	throws DocumentParseException
	{
		Node child;
		CharacterData textNode;
		String staticText;

		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			// Completely ignore any comment nodes.

			if (child.getNodeType() == Node.COMMENT_NODE)
				continue;

			// Currently, we support a single special markup, the
			// <insert key="..."/>.

			if (isElement(child, "insert"))
			{
				addSymbol(token, child);
				continue;
			}

			// Have to assume it is a Text or CDATASection, both
			// of which inherit from interface CharacterData

			textNode = (CharacterData)child;
			staticText = textNode.getData();

			token.addToken(new StaticToken(staticText));
		}

	}

	/**
	*  Creates a {@link SymbolToken} for the current node
	*  (which is an insert element node) and adds it as a child of the token.
	 *  Uses a caching mechanism so
	*  that it creates only one SymbolToken for each key referenced in the
	*  script.
	*
	*/

	private void addSymbol(IScriptToken token, Node node)
	{
		IScriptToken symbolToken = null;
		Element element = (Element)node;
		String key = element.getAttribute("key");

		if (symbolCache == null)
			symbolCache = new HashMap(MAP_SIZE);	
		else
			symbolToken = (IScriptToken)symbolCache.get(key);

		if (symbolToken == null)
		{
			symbolToken = new SymbolToken(key);
			symbolCache.put(key, symbolToken);
		}

		token.addToken(symbolToken);
	}

}

