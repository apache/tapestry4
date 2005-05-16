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
import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.test.TypeMatcher;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectScriptWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectScriptWorker extends HiveMindTestCase
{
    public void testSuccess()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        Location componentSpecLocation = fabricateLocation(22);
        final Resource scriptResource = componentSpecLocation.getResource().getRelativeResource(
                "bar.script");

        final Location injectSpecLocation = newLocation();

        final IScriptSource source = (IScriptSource) newMock(IScriptSource.class);

        op.claimProperty("foo");

        op.getPropertyType("foo");
        opc.setReturnValue(IScript.class);

        op.getAccessorMethodName("foo");
        opc.setReturnValue("getFoo");

        op.addInjectedField("_$script", new DeferredScriptImpl(scriptResource, source,
                injectSpecLocation));
        opc.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, new ArgumentMatcher()
        {

            public boolean compareArguments(Object expected, Object actual)
            {
                DeferredScriptImpl ds = (DeferredScriptImpl) actual;

                return ds._location == injectSpecLocation && ds._scriptSource == source
                        && ds._scriptResource.equals(scriptResource);
            }

        }

        }));
        opc.setReturnValue("_script");

        MethodSignature sig = new MethodSignature(IScript.class, "getFoo", null, null);

        op.addMethod(Modifier.PUBLIC, sig, "return _script.getScript();");

        replayControls();

        InjectSpecificationImpl is = new InjectSpecificationImpl();
        is.setProperty("foo");
        is.setObject("bar.script");
        is.setLocation(injectSpecLocation);

        InjectScriptWorker worker = new InjectScriptWorker();
        worker.setSource(source);

        worker.performEnhancement(op, is);

        verifyControls();
    }
}
