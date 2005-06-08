// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Injects {@link org.apache.tapestry.IScript} instances directly into pages or components.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectScriptWorker implements InjectEnhancementWorker
{
    private IScriptSource _source;

    public void performEnhancement(EnhancementOperation op, InjectSpecification spec)
    {
        String propertyName = spec.getProperty();
        String scriptName = spec.getObject();
        Location location = spec.getLocation();

        injectScript(op, propertyName, scriptName, location);
    }

    /**
     * Injects a compiled script.
     * 
     * @param op
     *            the enhancement operation
     * @param propertyName
     *            the name of the property to inject
     * @param scriptName
     *            the name of the script (relative to the location)
     * @param location
     *            the location of the specification; primarily used as the base location for finding
     *            the script.
     */

    public void injectScript(EnhancementOperation op, String propertyName, String scriptName,
            Location location)
    {
        Defense.notNull(op, "op");
        Defense.notNull(propertyName, "propertyName");
        Defense.notNull(scriptName, "scriptName");
        Defense.notNull(location, "location");

        op.claimProperty(propertyName);

        Class propertyType = EnhanceUtils.verifyPropertyType(op, propertyName, IScript.class);

        // PropertyType will likely be either java.lang.Object or IScript

        String methodName = op.getAccessorMethodName(propertyName);

        Resource resource = location.getResource().getRelativeResource(scriptName);

        DeferredScript script = new DeferredScriptImpl(resource, _source, location);

        String fieldName = op.addInjectedField("_$script", IScript.class, script);

        MethodSignature sig = new MethodSignature(propertyType, methodName, null, null);

        op.addMethod(Modifier.PUBLIC, sig, "return " + fieldName + ".getScript();");
    }

    public void setSource(IScriptSource source)
    {
        _source = source;
    }
}
