package com.primix.tapestry.script;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;

 
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
 *  Parses a Tapestry Script, an XML file defined by the public identifier
 *  <code>-//Primix Solutions//Tapestry Script 1.0//EN</code>.
 *
 *  <p>A Tapestry Script is used, in association with the 
 *  {@link com.primix.tapestry.components.Body} and/or
 *  {@link Script} components,
 *  to generate JavaScript for use with a Tapestry component.  Two seperate pieces
 *  of JavaScript can be generated.  The body section (associated with the <code>body</code>
 *  element of the XML document) is typically used to define JavaScript functions
 *  (most often, event handlers).  The initialization section
 *  (associated with the <code>initialization</code> element of the XML document)
 *  is used to add JavaScript that will be evaluated when the page finishes loading
 *  (i.e., from the HTML &lt;body&gt; element's onLoad event handler).
 *
 *  <p>This code has way too much in common (read: cut and paste) with
 *  {@link com.primix.tapestry.parse.SpecificationParser}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class ScriptParser
implements ErrorHandler, EntityResolver
{
    // Description of the InputSource, used in some errors.

    private String resourcePath;

    private InputSource inputSource;

    private static final int MAP_SIZE = 11;
    private Map symbolCache;

    /**
     *  {@link List} of {@link ITemplateToken}, extracted from
     *  the XML document's body element.
     *
     */

    private List body = new ArrayList();

    /**
     *  {@link List} of {@link ITemplateToken}, extracted from
     *  the XML document's initialization element.
     *
     */

    private List initialization = new ArrayList();


    public ScriptParser(InputSource inputSource, String resourcePath)
    {
        this.inputSource = inputSource;
        this.resourcePath = resourcePath;
    }

    public void parse()
    throws ScriptParseException
    {
        Document document = parseDocument(); 

        build(document);
    }

    /**
     *  Returns the token from the body element, or null if there
     *  are no body element tokens.
     *
     */

    public ITemplateToken[] getBodyTokens()
    {
        return extract(body);
    }

    /**
     *  Returns the token from the initialization element, or null if there
     *  are no body element tokens.
     *
     */

    public ITemplateToken[] getInitializationTokens()
    {
        return extract(initialization);
    }

    private ITemplateToken[] extract(List list)
    {
        int count;
        ITemplateToken[] array;
                
        if (list == null)
            return null;

        count = list.size();

        if (count == 0)
            return null;

        array = new ITemplateToken[count];

        return (ITemplateToken[])list.toArray(array);
    }

    public void warning(SAXParseException exception)
    throws SAXException
    {
    	throw exception;
    }
    
    public void error(SAXParseException exception)
    throws SAXException
    {
    	throw exception;
    }
    
    public void fatalError(SAXParseException exception)
    throws SAXException
    {
    	throw exception;
    }
    	
    /**
     *  Handles the public reference <code>-//Primix Solutions//Tapestry Script 1.0//EN</code>
     *  by resolving it to the input stream of the <code>Script_1_0.dtd</code>
     *  resource.
     *
     */
     
    public InputSource resolveEntity(String publicId,
                                 String systemId)
                          throws SAXException,
                                 IOException
    {
    	if (publicId.equals("-//Primix Solutions//Tapestry Script 1.0//EN"))
    	{
    		InputStream stream;
    		
    		stream = getClass().getResourceAsStream("Script_1_0.dtd");

    		return new InputSource(stream);
    	}
    	
    	// Use default behavior.
    	
    	return null;
    }

    private Document parseDocument()
    throws ScriptParseException
    {
        DOMParser parser = null;
    	
    	try
    	{
    		parser = new DOMParser();
    		parser.setErrorHandler(this);
    		parser.setEntityResolver(this);
    		
    		// Turn on validation.  We use the setFeature() method since
    		// it doesn't throw java.lang.Exception (!).
    		
    		parser.setFeature("http://xml.org/sax/features/validation", true);
    		
    		// We always traverse the entire tree.
    		
    		parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);		
    		    		
    		parser.parse(inputSource);
    		
    		return parser.getDocument();
    	}
    	catch (SAXException se)
    	{
    		throw new ScriptParseException(
    		    "Unable to parse " + resourcePath + ".", 
    			resourcePath, parser.getLocator(), se);
    	}
    	catch (IOException ioe)
    	{
    		throw new ScriptParseException(
    		    "Error reading " + resourcePath + ".",
    		    resourcePath, parser.getLocator(), ioe);
    	}
    }

    private void build(Document document)
    throws ScriptParseException
    {
        Element root;
        Node child;
   
        root = document.getDocumentElement();
        
        if (!root.getTagName().equals("script"))
        {
    	    throw new ScriptParseException(
    		    "Incorrect document type; expected script but received " + 
    		    root.getTagName() + ".", resourcePath, null, null);
        }

        for (child = root.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "body"))
            {
                build(child, body);
                continue;
            }

            if (isElement(child, "initialization"))
            {
                build(child, initialization);
                continue;
            }

            // Else, ignorable whitespace, I guess.
        }

    }

    private void build(Node node, List list)
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
                addSymbol(list, child);
    		    continue;
    	    }

            // Have to assume it is a Text or CDATASection, both
            // of which inherit from interface CharacterData

            textNode = (CharacterData)child;
            staticText = textNode.getData();

            list.add(new StaticToken(staticText));
        }
    
    }

    /**
     *  Creates a {@link SymbolToken} for the current node
     *  (which is an insert element node).  Uses a caching mechanism so
     *  that it creates only one SymbolToken for each key referenced in the
     *  script.
     *
     */

    private void addSymbol(List list, Node node)
    {
        Element element;
        String key;
        ITemplateToken token = null;

        element = (Element)node;
        key = element.getAttribute("key");

        if (symbolCache != null)
            token = (ITemplateToken)symbolCache.get(key);

        if (token == null)
        {
            token = new SymbolToken(key);

            if (symbolCache == null)
                symbolCache = new HashMap(MAP_SIZE);

            symbolCache.put(key, token);
        }

        list.add(token);
    }

    private boolean isElement(Node node, String elementName)
    {
    	if (node.getNodeType() != Node.ELEMENT_NODE)
    		return false;
    	
    	// Cast it to Element
    	
    	Element element = (Element)node;
    	
    	// Note:  Using Xerces 1.0.3 and deferred DOM loading
    	// (which is explicitly turned off), this sometimes
    	// throws a NullPointerException.
    	
    	return element.getTagName().equals(elementName);
    
    }	

}