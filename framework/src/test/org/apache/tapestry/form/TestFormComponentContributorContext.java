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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.Infrastructure;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.FormComponentContributorContextImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestFormComponentContributorContext extends BaseComponentTestCase
{
    private IForm newForm(String name)
    {
        IForm form = newMock(IForm.class);

        expect(form.getName()).andReturn(name);

        return form;
    }

    private IFormComponent newField(IForm form)
    {
        IFormComponent field = newMock(IFormComponent.class);

        expect(field.getForm()).andReturn(form);

        return field;
    }

    private ClassResolver newResolver()
    {
        return newMock(ClassResolver.class);
    }

    private Infrastructure newInfrastructure(ClassResolver resolver)
    {
        Infrastructure inf = newMock(Infrastructure.class);

        expect(inf.getClassResolver()).andReturn(resolver);

        return inf;
    }

    public void testIncludeClasspathScript()
    {
        IForm form = newForm("myform");
        IFormComponent field = newField(form);
        ClassResolver resolver = newResolver();

        IRequestCycle cycle = newCycle();

        Infrastructure inf = newInfrastructure(resolver);

        expect(cycle.getInfrastructure()).andReturn(inf);

        PageRenderSupport prs = newSupport();

        Resource expected = new ClasspathResource(resolver, "/foo.js");

        prs.addExternalScript(expected);

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        replay();

        FormComponentContributorContext context = new FormComponentContributorContextImpl(
                Locale.ENGLISH, cycle, field);

        context.includeClasspathScript("/foo.js");

        verify();
    }

    public void testAddSubmitHandler()
    {

        IForm form = newForm("myform");
        IFormComponent field = newField(form);
        ClassResolver resolver = newResolver();
        
        IRequestCycle cycle = newCycle();
        Infrastructure inf = newInfrastructure(resolver);

        expect(cycle.getInfrastructure()).andReturn(inf);

        PageRenderSupport prs = newSupport();

        prs.addInitializationScript("Tapestry.onsubmit('myform', foo);");

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        replay();

        FormComponentContributorContext context = new FormComponentContributorContextImpl(
                Locale.ENGLISH, cycle, field);

        context.addSubmitHandler("foo");

        verify();
    }

    private PageRenderSupport newSupport()
    {
        return newMock(PageRenderSupport.class);
    }
}
