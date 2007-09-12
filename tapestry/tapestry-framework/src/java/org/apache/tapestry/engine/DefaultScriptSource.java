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

package org.apache.tapestry.engine;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.script.ScriptParser;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.xml.DocumentParseException;

import java.util.Map;

/**
 * Provides basic access to scripts available on the classpath. Scripts are cached in memory once
 * parsed.
 *
 * @author Howard Lewis Ship
 * @since 1.0.2
 */

public class DefaultScriptSource implements IScriptSource, ResetEventListener, ReportStatusListener
{
    private String _serviceId;

    private ClassResolver _classResolver;

    /** @since 4.0 */
    private ExpressionEvaluator _expressionEvaluator;

    /** @since 4.0 */
    private ValueConverter _valueConverter;

    private Map _cache = new ConcurrentHashMap();

    public void resetEventDidOccur()
    {
        _cache.clear();
    }

    public void reportStatus(ReportStatusEvent event)
    {
        event.title(_serviceId);
        event.property("parsed script count", _cache.size());
        event.collection("parsed scripts", _cache.keySet());
    }

    public IScript getScript(Resource resource)
    {
        IScript result = (IScript) _cache.get(resource);

        if (result != null)
            return result;

        result = parse(resource);

        _cache.put(resource, result);

        return result;
    }

    private IScript parse(Resource resource)
    {
        ScriptParser parser = new ScriptParser(_classResolver, _expressionEvaluator, _valueConverter);
        try
        {
            return parser.parse(resource);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format("DefaultScriptSource.unable-to-parse-script",
                                                                  resource), ex);
        }
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    /** @since 4.0 */
    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator)
    {
        _expressionEvaluator = expressionEvaluator;
    }

    /** @since 4.0 */
    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }

    public void setServiceId(String serviceId)
    {
        _serviceId = serviceId;
    }
}
