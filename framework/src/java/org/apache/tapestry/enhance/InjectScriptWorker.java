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

import org.apache.hivemind.Resource;
import org.apache.hivemind.service.MethodSignature;
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

        op.claimProperty(propertyName);

        Class propertyType = EnhanceUtils.verifyPropertyType(op, propertyName, IScript.class);

        // PropertyType will likely be either java.lang.Object or IScript

        String methodName = op.getAccessorMethodName(propertyName);

        Resource resource = spec.getLocation().getResource().getRelativeResource(spec.getObject());

        DeferredScript script = new DeferredScriptImpl(resource, _source, spec.getLocation());

        String fieldName = op.addInjectedField("_$script", IScript.class, script);

        MethodSignature sig = new MethodSignature(propertyType, methodName, null, null);

        op.addMethod(Modifier.PUBLIC, sig, "return " + fieldName + ".getScript();");
    }

    public void setSource(IScriptSource source)
    {
        _source = source;
    }
}
