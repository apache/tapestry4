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

import org.apache.tapestry.util.xml.RuleDirectedParser;
import org.xml.sax.Attributes;

/**
 * Constructs a {@link org.apache.tapestry.script.ForeachToken}
 * from a &lt;foreach&gt; element, which contains full content.
 * 
 * <p>As of 3.0, then index attribute has been added to foreach to keep 
 * track of the current index of the iterating collection.</p>
 *
 * @author Howard Lewis Ship, Harish Krishnaswamy
 * @version $Id$
 * @since 3.0
 */
class ForeachRule extends AbstractTokenRule
{

    public void endElement(RuleDirectedParser parser)
    {
        parser.pop();
    }

    public void startElement(RuleDirectedParser parser, Attributes attributes)
    {
        String key = getAttribute(attributes, "key");
        String index = getAttribute(attributes, "index");
        String expression = getAttribute(attributes, "expression");

        if (expression == null)
            expression = getAttribute(attributes, "property-path"); // 1.0, 1.1 DTD

        IScriptToken token = new ForeachToken(key, index, expression, parser.getLocation());

        addToParent(parser, token);

        parser.push(token);
    }

}
