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
 * Constructs a {@link org.apache.tapestry.script.UniqueToken}
 * from an &lt;unique&gt; element, which contains full content.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */

public class UniqueRule extends AbstractTokenRule
{

    public void endElement(RuleDirectedParser parser)
    {
        parser.pop();
    }

    public void startElement(RuleDirectedParser parser, Attributes attributes)
    {
        IScriptToken token = new UniqueToken(parser.getLocation());

        addToParent(parser, token);
        parser.push(token);
    }

}
