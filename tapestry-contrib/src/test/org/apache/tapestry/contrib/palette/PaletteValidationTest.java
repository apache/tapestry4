// Copyright Aug 6, 2006 The Apache Software Foundation
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
package org.apache.tapestry.contrib.palette;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.ValidatableFieldExtension;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.form.validator.Required;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationStrings;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link Palette} and {@link ValidatableFieldExtension} 
 * contributions.
 * 
 * @author jkuhnert
 */
@Test
public class PaletteValidationTest extends BaseComponentTestCase
{

    public void test_Palette_Required()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        IRequestCycle cycle = newCycle();
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        IMarkupWriter writer = newWriter();
        
        JSONObject profile = new JSONObject();
        Required required = new Required("message=Field {0} is required.");
        
        Palette component = newInstance(Palette.class, 
                new Object[] { 
            "validatableFieldSupport", vfs, 
            "id", "pal", "displayName", "Pally"});
        
        expect(context.getProfile()).andReturn(profile);
        
        expect(context.formatValidationMessage(eq(required.getMessage()), eq(ValidationStrings.REQUIRED_FIELD), 
                aryEq(new Object[] { "Pally" }) )).andReturn("Pally is required.");
        
        replay();
        
        assert component.overrideValidator(required, cycle);
        
        component.overrideContributions(required, context, writer, cycle);
        
        verify();
        
        assert profile.has(ValidationConstants.CONSTRAINTS);
        
        Object literal = profile.getJSONObject(ValidationConstants.CONSTRAINTS).get(component.getClientId());
        
        assert literal != null;
        
        assertEquals(literal.toString(),"[[tapestry.form.validation.isPalleteSelected]]");
    }
}
