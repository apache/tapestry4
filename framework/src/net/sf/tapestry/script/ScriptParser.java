/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.script;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.IScript;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.xml.AbstractDocumentParser;
import net.sf.tapestry.util.xml.DocumentParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *  Parses a Tapestry Script, an XML file defined by the public identifier
 *  <code>-//Primix Solutions//Tapestry Script 1.0//EN</code> 
 *  or
 *  <code>-//Howard Ship//Tapestry Script 1.1//EN</code>.
 *
 *  <p>The new DTD, version 1.1, is largely backwards compatible to the
 *  old script, but adds a number of new features (if, if-not, foreach
 *  and the use of property paths with insert).
 *
 *  <p>A Tapestry Script is used, in association with the 
 *  {@link net.sf.tapestry.html.Body} and/or 
 *  {@link net.sf.tapestry.html.Script} components,
 *  to generate JavaScript for use with a Tapestry component.  Two seperate pieces
 *  of JavaScript can be generated.  The body section (associated with the <code>body</code>
 *  element of the XML document) is typically used to define JavaScript functions
 *  (most often, event handlers).  The initialization section
 *  (associated with the <code>initialization</code> element of the XML document)
 *  is used to add JavaScript that will be evaluated when the page finishes loading
 *  (i.e., from the HTML &lt;body&gt; element's onLoad event handler).
 * 
 *  <p>
 *  Starting in release 2.2, the &lt;insert&gt; element is no longer used, instead
 *  the (Ant-like) syntax <code>${<i>property-path</i>}</code> accomplishes the
 *  same thing.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ScriptParser extends AbstractDocumentParser
{

    private static final Log LOG = LogFactory.getLog(ScriptParser.class);

    private static final int MAP_SIZE = 11;

    /**
     *  A cache of {@link InsertToken}s, keyed on expression.
     *
     **/

    private Map _insertCache;

    /** @since 2.2 **/

    private IResourceResolver _resolver;

    /**
     *  True when parsing a version 1.2 script, which renames attribute
     *  "property-path" to "expression".
     * 
     **/

    private boolean _version_1_2;

    private String _expressionAttribute;

    public static final String SCRIPT_DTD_1_0_PUBLIC_ID = "-//Primix Solutions//Tapestry Script 1.0//EN";

    public static final String SCRIPT_DTD_1_1_PUBLIC_ID = "-//Howard Ship//Tapestry Script 1.1//EN";

    public static final String SCRIPT_DTD_1_2_PUBLIC_ID = "-//Howard Lewis Ship//Tapestry Script 1.2//EN";

    public ScriptParser(IResourceResolver resolver)
    {
        _resolver = resolver;

        register(SCRIPT_DTD_1_0_PUBLIC_ID, "Script_1_0.dtd");
        register(SCRIPT_DTD_1_1_PUBLIC_ID, "Script_1_1.dtd");
        register(SCRIPT_DTD_1_2_PUBLIC_ID, "Script_1_2.dtd");
    }

    /**
     *  Parses the given input stream to produce a parsed script,
     *  ready to execute.
     *
     **/

    public IScript parse(InputStream stream, String resourcePath) throws DocumentParseException
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

    private IScript build(Document document) throws DocumentParseException
    {
        ParsedScript result = new ParsedScript(getResourcePath());
        Element root = document.getDocumentElement();

        String publicId = document.getDoctype().getPublicId();

        if (!(publicId.equals(SCRIPT_DTD_1_0_PUBLIC_ID)
            || publicId.equals(SCRIPT_DTD_1_1_PUBLIC_ID)
            || publicId.equals(SCRIPT_DTD_1_2_PUBLIC_ID)))
            throw new DocumentParseException(
                Tapestry.getString("ScriptParser.unknown-public-id", publicId),
                getResourcePath());

        _version_1_2 = publicId.equals(SCRIPT_DTD_1_2_PUBLIC_ID);

        _expressionAttribute = (_version_1_2 ? "expression" : "property-path");

        for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling())
        {

            // Ordered first since it is most prevalent.

            if (isElement(child, "let"))
            {
                result.addToken(buildLet(child));
                continue;
            }

            if (isElement(child, "set"))
            {
                result.addToken(buildSet(child));
                continue;
            }

            if (isElement(child, "include-script"))
            {
                result.addToken(buildIncludeScript(child));
                continue;
            }
            
            if (isElement(child, "input-symbol"))
            {
                result.addToken(buildInputSymbol(child));
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

    private IScriptToken buildLet(Node node) throws DocumentParseException
    {
        String key = getAttribute(node, "key");

        validate(key, SIMPLE_PROPERTY_NAME_PATTERN, "ScriptParser.invalid-key");

        IScriptToken token = new LetToken(key);
        build(token, node);

        return token;
    }

    public IScriptToken buildSet(Node node) throws DocumentParseException
    {
        String key = getAttribute(node, "key");

        validate(key, SIMPLE_PROPERTY_NAME_PATTERN, "ScriptParser.invalid-key");

        String expression = getAttribute(node, "expression");

        return new SetToken(key, expression, _resolver);
    }

    private IScriptToken buildInputSymbol(Node node) throws DocumentParseException
    {
        String key = getAttribute(node, "key");

        validate(key, SIMPLE_PROPERTY_NAME_PATTERN, "ScriptParser.invalid-key");

        String className = getAttribute(node, "class");

        Class expectedClass = lookupClass(className);

        String required = getAttribute(node, "required");

        return new InputSymbolToken(key, expectedClass, required.equals("yes"));
    }

    private Class lookupClass(String className) throws DocumentParseException
    {
        if (Tapestry.isNull(className))
            return null;

        try
        {
            return _resolver.findClass(className);
        }
        catch (ApplicationRuntimeException ex)
        {
            throw new DocumentParseException(
                Tapestry.getString("ScriptParser.unable-to-resolve-class", className),
                getResourcePath(),
                ex);
        }
    }

    private IScriptToken buildBody(Node node) throws DocumentParseException
    {
        IScriptToken token = new BodyToken();

        build(token, node);

        return token;
    }

    private IScriptToken buildInitialization(Node node) throws DocumentParseException
    {
        IScriptToken token = new InitToken();

        build(token, node);

        return token;
    }

    /**
     *  Builds the inner content of some other token; this method understands
     *  all the content that can appear in the %full-content; entity of the DTD.
     *
     **/

    private void build(IScriptToken token, Node node) throws DocumentParseException
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

            textNode = (CharacterData) child;
            staticText = textNode.getData();

            addTextTokens(token, staticText);
        }

    }

    /**
     *  Creates an {@link InsertToken} for the current node
     *  (which is an insert element node) and adds it as a child of the token.
     *  Uses a caching mechanism so
     *  that it creates only one SymbolToken for each key referenced in the
     *  script.
     * 
     *  <p>This appears in the 1.0 and 1.1 scripts, but it is
     *  removed from 1.2.
     *
     **/

    private IScriptToken buildInsert(Node node)
    {
        String expression = getAttribute(node, _expressionAttribute);

        // Version 1.0 of the DTD called the attribute "key".

        if (expression == null)
            expression = getAttribute(node, "key");

        return constructInsert(expression);
    }

    /** @since 2.2 **/

    private IScriptToken constructInsert(String expression)
    {
        IScriptToken result = null;

        if (_insertCache == null)
            _insertCache = new HashMap(MAP_SIZE);
        else
            result = (IScriptToken) _insertCache.get(expression);

        if (result == null)
        {
            result = new InsertToken(expression, _resolver);
            _insertCache.put(expression, result);
        }

        return result;
    }

    private IScriptToken buildIf(boolean condition, Node node) throws DocumentParseException
    {
        String expression = getAttribute(node, _expressionAttribute);
        IScriptToken result = new IfToken(condition, expression, _resolver);

        build(result, node);

        return result;
    }

    private IScriptToken buildForeach(Node node) throws DocumentParseException
    {
        String key = getAttribute(node, "key");
        String expression = getAttribute(node, _expressionAttribute);
        IScriptToken result = new ForeachToken(key, expression, _resolver);

        build(result, node);

        return result;
    }

    /** @since 1.0.5 **/

    private IScriptToken buildIncludeScript(Node node)
    {
        String path = getAttribute(node, "resource-path");

        return new IncludeScriptToken(path);
    }

    private static final int STATE_START = 0;
    private static final int STATE_DOLLAR = 1;
    private static final int STATE_COLLECT_EXPRESSION = 2;

    /** @since 2.2 **/

    private void addTextTokens(IScriptToken token, String text)
    {
        char[] buffer = text.toCharArray();
        int state = STATE_START;
        int blockStart = 0;
        int blockLength = 0;
        int expressionStart = -1;
        int expressionLength = 0;
        int i = 0;
        int braceDepth = 0;

        while (i < buffer.length)
        {
            char ch = buffer[i];

            switch (state)
            {
                case STATE_START :

                    if (ch == '$')
                    {
                        state = STATE_DOLLAR;
                        i++;
                        continue;
                    }

                    blockLength++;
                    i++;
                    continue;

                case STATE_DOLLAR :

                    if (ch == '{')
                    {
                        state = STATE_COLLECT_EXPRESSION;
                        i++;

                        expressionStart = i;
                        expressionLength = 0;
                        braceDepth = 1;

                        continue;
                    }

                    state = STATE_START;
                    continue;

                case STATE_COLLECT_EXPRESSION :

                    if (ch != '}')
                    {
                        if (ch == '{')
                            braceDepth++;

                        i++;
                        expressionLength++;
                        continue;
                    }

                    braceDepth--;

                    if (braceDepth > 0)
                    {
                        i++;
                        expressionLength++;
                        continue;
                    }

                    // Hit the closing brace of an expression.

                    // Degenerate case:  the string "${}".

                    if (expressionLength == 0)
                        blockLength += 3;

                    if (blockLength > 0)
                        token.addToken(constructStatic(text, blockStart, blockLength));

                    if (expressionLength > 0)
                    {
                        String expression = text.substring(expressionStart, expressionStart + expressionLength);

                        token.addToken(constructInsert(expression));
                    }

                    i++;
                    blockStart = i;
                    blockLength = 0;

                    // And drop into state start

                    state = STATE_START;

                    continue;

            }

        }

        // OK, to handle the end.  Couple of degenerate cases where
        // a ${...} was incomplete, so we adust the block length.

        if (state == STATE_DOLLAR)
            blockLength++;

        if (state == STATE_COLLECT_EXPRESSION)
            blockLength += expressionLength + 2;

        if (blockLength > 0)
            token.addToken(constructStatic(text, blockStart, blockLength));
    }

    /** @since 2.2. **/

    private IScriptToken constructStatic(String text, int blockStart, int blockLength)
    {
        return new StaticToken(text.substring(blockStart, blockStart + blockLength));
    }
}