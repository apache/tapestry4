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

package com.primix.tapestry.script;

import com.primix.tapestry.*;
import com.primix.tapestry.html.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.util.*;
import com.primix.tapestry.util.xml.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.apache.log4j.*;

/**
 *  Parses a Tapestry Script, an XML file defined by the public identifier
 *  <code>-//Primix Solutions//Tapestry Script 1.0//EN</code> 
 * or
 * <code>-//Howard Ship//Tapestry Script 1.1//EN</code>.
 *
 * <p>The new DTD, version 1.1, is largely backwards compatible to the
 * old script, but adds a number of new features (if, if-not, foreach
 * and the use of property paths with insert).
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
	
	private static final Category CAT = 
		Category.getInstance(ScriptParser.class);
	
	private static final int MAP_SIZE = 11;
	
	/**
	 *  A cache of {@link InsertToken}s, keyed on property path.
	 *
	 */
	
	private Map insertCache;
	
	public static final String SCRIPT_DTD_1_0_PUBLIC_ID =
		"-//Primix Solutions//Tapestry Script 1.0//EN";
	
	public static final String SCRIPT_DTD_1_1_PUBLIC_ID = 
		"-//Howard Ship//Tapestry Script 1.1//EN";
	
	public ScriptParser()
	{		
		register(SCRIPT_DTD_1_0_PUBLIC_ID, "Script_1_0.dtd");
		register(SCRIPT_DTD_1_1_PUBLIC_ID, "Script_1_1.dtd");
	}
	
	/**
	 *  Parses the given input stream to produce a parsed script,
	 *  ready to execute.
	 *
	 */
	
	public IScript parse(InputStream stream, String resourcePath)
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
	
	private IScript build(Document document)
		throws DocumentParseException
	{
		ParsedScript result = new ParsedScript(getResourcePath()); 
		Element root = document.getDocumentElement();

		String publicId = document.getDoctype().getPublicId();
		
		if (! (publicId.equals(SCRIPT_DTD_1_0_PUBLIC_ID) ||
				publicId.equals(SCRIPT_DTD_1_1_PUBLIC_ID)))
			throw new DocumentParseException("Script uses unknown public identifier " +
						publicId + ".", getResourcePath());
			
		for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling())
		{
			
			// Ordered first since it is most prevalent.
			
			if (isElement(child, "let"))
			{
				result.addToken(buildLet(child));
				continue;
			}
			
			if (isElement(child, "include-script"))
			{
				result.addToken(buildIncludeScript(child));
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
	

	/**
	 *  Builds the inner content of some other token; this method understands
	 *  all the content that can appear in the %full-content; entity of the DTD.
	 *
	 */
	
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
						
			if (isElement(child, "insert"))
			{
				token.addToken(buildInsert(child));
				continue;
			}
			
			if (isElement(child, "if"))
			{
				token.addToken(buildIf(true, child));
				continue;
			}
			
			if (isElement(child, "if-not"))
			{
				token.addToken(buildIf(false, child));
				continue;
			}
			
			if (isElement(child, "foreach"))
			{
				token.addToken(buildForeach(child));
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
	 *  Creates an {@link InsertToken} for the current node
	 *  (which is an insert element node) and adds it as a child of the token.
	 *  Uses a caching mechanism so
	 *  that it creates only one SymbolToken for each key referenced in the
	 *  script.
	 *
	 */
	
	private IScriptToken buildInsert(Node node)
	{
		IScriptToken result = null;
		String propertyPath = getAttribute(node, "property-path");
		
		// Version 1.0 of the DTD called the attribute "key".
		
		if (propertyPath == null)
			propertyPath = getAttribute(node, "key");
		
		if (insertCache == null)
			insertCache = new HashMap(MAP_SIZE);	
		else
			result = (IScriptToken)insertCache.get(propertyPath);
		
		if (result == null)
		{
			result = new InsertToken(propertyPath);
			insertCache.put(propertyPath, result);
		}
		
		return result;
	}
	
	private IScriptToken buildIf(boolean condition, Node node)
		throws DocumentParseException
	{
		String propertyPath = getAttribute(node, "property-path");
		IScriptToken result = new IfToken(condition, propertyPath);
		
		build(result, node);
		
		return result;
	}
	
	private IScriptToken buildForeach(Node node)
		throws DocumentParseException
	{
		String key = getAttribute(node, "key");
		String propertyPath = getAttribute(node, "property-path");
		IScriptToken result = new ForeachToken(key, propertyPath);
		
		build(result, node);
		
		return result;
	}
	
	/** @since 1.0.5 **/
	
	private IScriptToken buildIncludeScript(Node node)
	{
		String path = getAttribute(node, "resource-path");
		
		return new IncludeScriptToken(path);
	}
}

