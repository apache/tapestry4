// Copyright 2004 The Apache Software Foundation
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

import java.util.Iterator;
import java.util.Map;

import org.apache.hivemind.Resource;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScriptProcessor;

/**
 * Process object used when executing a {@link org.apache.tapestry.IScript script template}. This
 * ScriptSession provides support
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ScriptSession extends IScriptProcessor
{
    /**
     * Evaluates an OGNL expression, where the root object for the expression is the
     * {@link #getSymbols() symbols map}.
     */
    public Object evaluate(String expression);

    /**
     * Returns the resource for the script template.
     */

    public Resource getScriptTemplateResource();

    /**
     * Returns the symbols (which may be created or updated during the execution of the script
     * template).
     */

    public Map getSymbols();

    /**
     * Returns the current request cycle.
     */
    public IRequestCycle getRequestCycle();

    /**
     * Evaluates an expression and coerces the result to a particlar type.
     * 
     * @since 3.1
     * @see org.apache.tapestry.coerce.ValueConverter
     */

    public Object evaluate(String expression, Class desiredType);
}