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

import java.util.Map;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.IScriptProcessor;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * A top level container for a number of {@link IScriptToken script tokens}.
 * 
 * @author Howard Lewis Ship
 * @since 0.2.9
 */

public class ParsedScript extends AbstractToken implements IScript
{
    private Resource _scriptResource;

    private ExpressionEvaluator _evaluator;

    /** @since 3.1 */

    private ValueConverter _valueConverter;

    public ParsedScript(ExpressionEvaluator evaluator, ValueConverter valueConverter,
            Location location)
    {
        super(location);

        _evaluator = evaluator;
        _valueConverter = valueConverter;
        _scriptResource = location.getResource();
    }

    public Resource getScriptResource()
    {
        return _scriptResource;
    }

    /**
     * Creates the {@link ScriptSessionImpl}and invokes
     * {@link org.apache.tapestry.script.AbstractToken#writeChildren(java.lang.StringBuffer, org.apache.tapestry.script.ScriptSession)}.
     */
    public void execute(IRequestCycle cycle, IScriptProcessor processor, Map symbols)
    {
        ScriptSession session = new ScriptSessionImpl(_scriptResource, cycle, processor,
                _evaluator, _valueConverter, symbols);

        writeChildren(null, session);
    }

    /**
     * Does nothing; never invoked.
     */
    public void write(StringBuffer buffer, ScriptSession session)
    {

    }

}