// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.util.xml.BaseRule;
import org.apache.tapestry.util.xml.RuleDirectedParser;
import org.xml.sax.Attributes;

/**
 * Constructs an {@link org.apache.tapestry.script.IncludeScriptToken}
 * from a &lt;include-script&gt; element, which contains no content.
 *
 * @author Howard Lewis Ship
 * @since 3.0
 */
class IncludeScriptRule extends BaseRule
{

    public void startElement(RuleDirectedParser parser, Attributes attributes)
    {
        String path = getAttribute(attributes, "resource-path");
        
        IncludeScriptToken token = new IncludeScriptToken(path, parser.getLocation());

		IScriptToken parent = (IScriptToken) parser.peek();
        parent.addToken(token);
    }

}
