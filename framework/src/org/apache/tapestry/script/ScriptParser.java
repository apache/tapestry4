/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.script;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IScript;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.apache.tapestry.util.xml.RuleDirectedParser;

/**
 *  Parses a Tapestry Script, an XML file defined by one of the following
 *  public identifiers:
 *  <ul>
 *  <li><code>-//Primix Solutions//Tapestry Script 1.0//EN</code></li> 
 *  <li><code>-//Howard Ship//Tapestry Script 1.1//EN</code></li>
 *  <li><code>-//Howard Lewis Ship//Tapestry Script 1.2//EN</code></li>
 * .
 *
 *  <p>The version 1.1, is largely backwards compatible to the
 *  old script, but adds a number of new features (if, if-not, foreach
 *  and the use of property paths with insert).
 * 
 *  <p>Version 1.2 removes the &lt;insert&gt; element, using an Ant-like
 *  syntax (<code>${<i>expression</i>}</code>).  It also replaces
 *  the attribute name <code>property-path</code> with <code>expression</code>
 *  (because OGNL is used).
 *
 *  <p>A Tapestry Script is used, in association with the 
 *  {@link org.apache.tapestry.html.Body} and/or 
 *  {@link org.apache.tapestry.html.Script} components,
 *  to generate JavaScript for use with a Tapestry component.  Two seperate pieces
 *  of JavaScript can be generated.  The body section (associated with the <code>body</code>
 *  element of the XML document) is typically used to define JavaScript functions
 *  (most often, event handlers).  The initialization section
 *  (associated with the <code>initialization</code> element of the XML document)
 *  is used to add JavaScript that will be evaluated when the page finishes loading
 *  (i.e., from the HTML &lt;body&gt; element's onLoad event handler).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ScriptParser
{
    private RuleDirectedParser _parser;

    public static final String SCRIPT_DTD_1_0_PUBLIC_ID =
        "-//Primix Solutions//Tapestry Script 1.0//EN";

    public static final String SCRIPT_DTD_1_1_PUBLIC_ID = "-//Howard Ship//Tapestry Script 1.1//EN";

    public static final String SCRIPT_DTD_1_2_PUBLIC_ID =
        "-//Howard Lewis Ship//Tapestry Script 1.2//EN";

    /** @since 3.0 */
    public static final String SCRIPT_DTD_3_0_PUBLIC_ID =
        "-//Apache Software Foundation//Tapestry Script Specification 3.0//EN";

    public ScriptParser(ClassResolver resolver)
    {
        _parser = new RuleDirectedParser();

        _parser.registerEntity(
            SCRIPT_DTD_1_0_PUBLIC_ID,
            "/org/apache/tapestry/script/Script_1_0.dtd");
        _parser.registerEntity(
            SCRIPT_DTD_1_1_PUBLIC_ID,
            "/org/apache/tapestry/script/Script_1_1.dtd");
        _parser.registerEntity(
            SCRIPT_DTD_1_2_PUBLIC_ID,
            "/org/apache/tapestry/script/Script_1_2.dtd");
        _parser.registerEntity(
            SCRIPT_DTD_3_0_PUBLIC_ID,
            "/org/apache/tapestry/script/Script_3_0.dtd");

        _parser.addRule("script", new ScriptRule());
        _parser.addRule("let", new LetRule());
        _parser.addRule("set", new SetRule());
        _parser.addRule("include-script", new IncludeScriptRule());
        _parser.addRule("input-symbol", new InputSymbolRule(resolver));
        _parser.addRule("body", new BodyRule());
        _parser.addRule("initialization", new InitRule());
        _parser.addRule("if", new IfRule(true));
        _parser.addRule("if-not", new IfRule(false));
        _parser.addRule("foreach", new ForeachRule());
        _parser.addRule("unique", new UniqueRule());

        // This will go away when the 1.1 and earler DTDs are retired.
        _parser.addRule("insert", new InsertRule());

    }

    /**
     *  Parses the given input stream to produce a parsed script,
     *  ready to execute.
     *
     **/

    public IScript parse(Resource resourceLocation) throws DocumentParseException
    {
        return (IScript) _parser.parse(resourceLocation);
    }

}