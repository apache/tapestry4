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

package org.apache.tapestry.form;

import static org.easymock.EasyMock.expect;

import java.util.Locale;

import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.junit.TapestryTestCase;
import org.testng.annotations.Test;

/**
 * Abstract test case for {@link FormComponentContributor}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
@Test
public abstract class FormComponentContributorTestCase extends TapestryTestCase
{
    // Paul,
    //
    // Think you missed the newControl() and newMock() methods inherited from HiveMindTestCase.
    // Those exist to eliminate the need for all this stuff. Instead, you create newFoo() methods
    // that
    // create and initialize a Foo instance.
    // -- Howard
    
    protected IFormComponent _component = newMock(IFormComponent.class);
    
    protected IPage _page = newPage();
    
    protected IRequestCycle _cycle = newCycle();
    
    protected IForm _form = newMock(IForm.class);

    protected IEngine _engine = newMock(IEngine.class);

    protected PageRenderSupport _pageRenderSupport = newPageRenderSupport();

    protected void addScript(String script)
    {
        expect(_cycle.getEngine()).andReturn(_engine);

        expect(_cycle.getAttribute("org.apache.tapestry.PageRenderSupport"))
        .andReturn(_pageRenderSupport);
        
        _pageRenderSupport.addExternalScript(new ClasspathResource(null, script));
    }

    protected IFormComponent newField(String displayName)
    {
        IFormComponent field = newMock(IFormComponent.class);

        expect(field.getDisplayName()).andReturn(displayName);

        return field;
    }

    protected IFormComponent newField(String displayName, String clientId, int count)
    {
        IFormComponent field = newMock(IFormComponent.class);

        expect(field.getDisplayName()).andReturn(displayName);

        expect(field.getClientId()).andReturn(clientId).times(count);

        return field;
    }
    
    protected IFormComponent newFieldWithClientId(String clientId)
    {
        IFormComponent field = newMock(IFormComponent.class);
        
        expect(field.getClientId()).andReturn(clientId);
        
        return field;        
    }


    protected void trainBuildMessage(ValidationMessages messages,
            String overrideMessage, String key, Object[] parameters, String result)
    {
        expect(messages.formatValidationMessage(overrideMessage, key, parameters))
        .andReturn(result);
    }

    protected void trainGetLocale(ValidationMessages messages, Locale locale)
    {
        expect(messages.getLocale()).andReturn(locale);
    }

    protected IFormComponent newField()
    {
        return newMock(IFormComponent.class);
    }

    protected IMarkupWriter newWriter()
    {
        return newMock(IMarkupWriter.class);
    }

    protected IRequestCycle newCycle()
    {
        return newMock(IRequestCycle.class);
    }

    protected ValidationMessages newValidationMessages(Locale locale)
    {
        ValidationMessages messages = newMock(ValidationMessages.class);

        expect(messages.getLocale()).andReturn(locale);

        return messages;
    }

    protected void trainTrim(FormComponentContributorContext context, String fieldId)
    {
        context.addSubmitHandler("function (event) { Tapestry.trim_field_value('" + fieldId
                + "'); }");
    }
}
