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

package org.apache.tapestry.form.validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.form.validator.ValidatorFactoryImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestValidatorFactory extends HiveMindTestCase
{
    private Map buildContributions(String name, boolean configurable)
    {
        ValidatorContribution vc = newContribution(configurable, ValidatorFixture.class);

        return Collections.singletonMap(name, vc);
    }

    private ValidatorContribution newContribution(boolean configurable, Class validatorClass)
    {
        ValidatorContribution vc = new ValidatorContribution();
        vc.setConfigurable(configurable);
        vc.setValidatorClass(validatorClass);

        return vc;
    }

    public void testEmpty()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();

        List result = vf.constructValidatorList("");

        assertTrue(result.isEmpty());
    }

    public void testSingle()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("value", true));

        List result = vf.constructValidatorList("value=foo");

        ValidatorFixture fixture = (ValidatorFixture) result.get(0);

        assertEquals("foo", fixture.getValue());
    }

    public void testMessage()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("fred", false));

        List result = vf.constructValidatorList("fred[fred's message]");

        ValidatorFixture fixture = (ValidatorFixture) result.get(0);

        assertEquals("fred's message", fixture.getMessage());
    }

    public void testConfigureAndMessage()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("value", true));

        List result = vf.constructValidatorList("value=biff[fred's message]");

        ValidatorFixture fixture = (ValidatorFixture) result.get(0);

        assertEquals("biff", fixture.getValue());
        assertEquals("fred's message", fixture.getMessage());
    }

    public void testValidatorListsCached()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("value", true));

        List result1 = vf.constructValidatorList("value=foo");
        List result2 = vf.constructValidatorList("value=foo");

        assertSame(result1, result2);
    }

    public void testMissingConfiguration()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("fred", true));

        try
        {
            vf.constructValidatorList("fred");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Validator 'name' must be configured in order to be used. "
                    + "The value is configured by changing 'name' to 'name=value'.", ex
                    .getMessage());
        }
    }

    public void testMultiple()
    {
        Map map = new HashMap();
        map.put("required", newContribution(false, Required.class));
        map.put("email", newContribution(false, Email.class));
        map.put("minLength", newContribution(true, MinLength.class));

        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(map);

        List result = vf
                .constructValidatorList("required[EMail is required],email,minLength=10[EMail must be at least ten characters long]");

        assertEquals(3, result.size());

        Required r = (Required) result.get(0);
        assertEquals("EMail is required", r.getMessage());

        assertTrue(result.get(1) instanceof Email);

        MinLength minLength = (MinLength) result.get(2);

        assertEquals(10, minLength.getMinLength());
        assertEquals("EMail must be at least ten characters long", minLength.getMessage());
    }

    public void testUnparseable()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("fred", false));

        try
        {
            vf.constructValidatorList("fred,=foo");
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Unable to parse 'fred,=foo' into a list of validators.", ex.getMessage());
        }
    }

    public void testUnwantedConfiguration()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(buildContributions("fred", false));

        try
        {
            vf.constructValidatorList("fred=biff");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Validator 'fred' is not configurable, "
                    + "'fred=biff' should be changed to just 'fred'.", ex.getMessage());
        }
    }

    public void testMissingValidator()
    {
        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(Collections.EMPTY_MAP);

        try
        {
            vf.constructValidatorList("missing");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("No validator named 'missing' has been defined.", ex.getMessage());
        }
    }

    public void testInstantiateFailure()
    {
        Map map = new HashMap();

        map.put("fred", newContribution(false, Object.class));

        ValidatorFactoryImpl vf = new ValidatorFactoryImpl();
        vf.setValidators(map);

        try
        {
            vf.constructValidatorList("fred");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Error initializing validator 'fred' (class java.lang.Object): java.lang.Object",
                    ex.getMessage());
        }

    }
}
