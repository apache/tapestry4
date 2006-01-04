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

import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.xml.BaseRule;
import org.apache.tapestry.util.xml.RuleDirectedParser;
import org.xml.sax.Attributes;

/**
 * Rule for &lt;script&gt; element. Creates a {@link org.apache.tapestry.script.ParsedScript}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
public class ScriptRule extends BaseRule
{
    /** @since 4.0 */
    private ExpressionEvaluator _evaluator;

    /** @since 4.0 */
    private ValueConverter _valueConverter;

    public ScriptRule(ExpressionEvaluator evaluator, ValueConverter valueConverter)
    {
        _evaluator = evaluator;
        _valueConverter = valueConverter;
    }

    public void endElement(RuleDirectedParser parser)
    {
        parser.pop();
    }

    public void startElement(RuleDirectedParser parser, Attributes attributes)
    {
        ParsedScript script = new ParsedScript(_evaluator, _valueConverter, parser.getLocation());

        parser.push(script);
    }

}