//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.script;

import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
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

    public ScriptParser(IResourceResolver resolver)
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

    public IScript parse(IResourceLocation resourceLocation) throws DocumentParseException
    {
        return (IScript) _parser.parse(resourceLocation);
    }

}