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

import org.apache.hivemind.Resource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScriptProcessor;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * The result of executing a script, the session is used during the parsing
 * process as well. Following
 * {@link org.apache.tapestry.IScript#execute(org.apache.tapestry.IRequestCycle, org.apache.tapestry.IScriptProcessor, java.util.Map)},
 * the session provides access to output symbols as well as the body and
 * initialization blocks created by the script tokens.
 * 
 * @author Howard Lewis Ship
 * @since 0.2.9
 */

public class ScriptSessionImpl implements ScriptSession
{

    private IRequestCycle _cycle;

    private IScriptProcessor _processor;

    private Resource _scriptTemplateResource;

    private Map _symbols;

    /** @since 4.0 */
    private ExpressionEvaluator _evaluator;

    /** @since 4.0 */
    private ValueConverter _valueConverter;

    private IComponent _component;
    
    public ScriptSessionImpl(Resource scriptTemplateResource,
            IRequestCycle cycle, IScriptProcessor processor,
            ExpressionEvaluator evaluator, ValueConverter valueConverter,
            Map symbols)
    {
        _scriptTemplateResource = scriptTemplateResource;
        _cycle = cycle;
        _processor = processor;
        _symbols = symbols;
        _evaluator = evaluator;
        _valueConverter = valueConverter;
    }
    
    public ScriptSessionImpl(Resource scriptTemplateResource,
            IComponent component, 
            IRequestCycle cycle, IScriptProcessor processor,
            ExpressionEvaluator evaluator, ValueConverter valueConverter,
            Map symbols)
    {
        _scriptTemplateResource = scriptTemplateResource;
        _component = component;
        _cycle = cycle;
        _processor = processor;
        _symbols = symbols;
        _evaluator = evaluator;
        _valueConverter = valueConverter;
    }

    public Object evaluate(String expression)
    {
        return _evaluator.read(_symbols, expression); //_evaluator.read(_symbols, expression);
    }

    public Object evaluate(String expression, Class desiredType)
    {
        Object raw = evaluate(expression);

        return _valueConverter.coerceValue(raw, desiredType);
    }

    public Resource getScriptTemplateResource()
    {
        return _scriptTemplateResource;
    }

    public Map getSymbols()
    {
        return _symbols;
    }

    public IRequestCycle getRequestCycle()
    {
        return _cycle;
    }

    public void addBodyScript(String script)
    {
        addBodyScript(_component, script);
    }
    
    public void addBodyScript(IComponent target, String script)
    {
        _processor.addBodyScript(target, script);
    }
    
    public void addExternalScript(Resource resource)
    {
        addExternalScript(_component, resource);
    }
    
    public void addExternalScript(IComponent target, Resource resource)
    {
        _processor.addExternalScript(target, resource);
    }

    public void addInitializationScript(String script)
    {
        addInitializationScript(_component, script);
    }

    public void addInitializationScript(IComponent target, String script)
    {
        _processor.addInitializationScript(target, script);
    }

    public String getUniqueString(String baseValue)
    {
        return _processor.getUniqueString(baseValue);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ScriptSession[");
        buffer.append(_scriptTemplateResource);
        buffer.append(']');

        return buffer.toString();
    }
}
