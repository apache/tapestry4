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

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.xml.BaseRule;
import org.apache.tapestry.util.xml.RuleDirectedParser;
import org.xml.sax.Attributes;

/**
 * Constructs at {@link org.apache.tapestry.script.SetToken} from at &lt;set&gt; element,
 * which contains full content.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */

class SetRule extends BaseRule
{
    public void startElement(RuleDirectedParser parser, Attributes attributes)
    {
        String key = getAttribute(attributes, "key");

        parser.validate(key, Tapestry.SIMPLE_PROPERTY_NAME_PATTERN, "ScriptParser.invalid-key");

        String expression = getAttribute(attributes, "expression");

        SetToken token = new SetToken(key, expression, parser.getLocation());

        IScriptToken parent = (IScriptToken) parser.peek();
        parent.addToken(token);

        parser.push(token);
    }

    public void endElement(RuleDirectedParser parser)
    {
        parser.pop();
    }
}
