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

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.components.BaseComponentTestCase;
import org.apache.tapestry.services.Infrastructure;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.FormComponentContributorContextImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestFormComponentContributorContext extends BaseComponentTestCase
{
    private IForm newForm(String name)
    {
        MockControl control = newControl(IForm.class);
        IForm form = (IForm) control.getMock();

        form.getName();
        control.setReturnValue(name);

        return form;
    }

    private IFormComponent newField(IForm form, String name)
    {
        MockControl control = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) control.getMock();

        field.getForm();
        control.setReturnValue(form);

        field.getName();
        control.setReturnValue(name);

        return field;
    }

    private ClassResolver newResolver()
    {
        return (ClassResolver) newMock(ClassResolver.class);
    }

    private Infrastructure newInfrastructure(ClassResolver resolver)
    {
        MockControl control = newControl(Infrastructure.class);
        Infrastructure inf = (Infrastructure) control.getMock();

        inf.getClassResolver();
        control.setReturnValue(resolver);

        return inf;
    }

    public void testGetFieldDOM()
    {
        IForm form = newForm("myform");
        IFormComponent field = newField(form, "myfield");
        ClassResolver resolver = newResolver();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        Infrastructure inf = newInfrastructure(resolver);

        cycle.getInfrastructure();
        cyclec.setReturnValue(inf);

        PageRenderSupport prs = newSupport();

        trainGetAttribute(cyclec, cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        replayControls();

        FormComponentContributorContext context = new FormComponentContributorContextImpl(
                Locale.ENGLISH, cycle, field);

        assertEquals("document.myform.myfield", context.getFieldDOM());

        verifyControls();
    }

    public void testIncludeClasspathScript()
    {
        IForm form = newForm("myform");
        IFormComponent field = newField(form, "myfield");
        ClassResolver resolver = newResolver();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        Infrastructure inf = newInfrastructure(resolver);

        cycle.getInfrastructure();
        cyclec.setReturnValue(inf);

        PageRenderSupport prs = newSupport();

        Resource expected = new ClasspathResource(resolver, "/foo.js");

        prs.addExternalScript(expected);

        trainGetAttribute(cyclec, cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        replayControls();

        FormComponentContributorContext context = new FormComponentContributorContextImpl(
                Locale.ENGLISH, cycle, field);

        context.includeClasspathScript("/foo.js");

        verifyControls();
    }

    public void testAddSubmitListener()
    {

        IForm form = newForm("myform");
        IFormComponent field = newField(form, "myfield");
        ClassResolver resolver = newResolver();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        Infrastructure inf = newInfrastructure(resolver);

        cycle.getInfrastructure();
        cyclec.setReturnValue(inf);

        PageRenderSupport prs = newSupport();

        prs.addInitializationScript("document.myform.events.addSubmitListener(foo);");

        trainGetAttribute(cyclec, cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);

        replayControls();

        FormComponentContributorContext context = new FormComponentContributorContextImpl(
                Locale.ENGLISH, cycle, field);

        context.addSubmitListener("foo");

        verifyControls();
    }

    private PageRenderSupport newSupport()
    {
        return (PageRenderSupport) newMock(PageRenderSupport.class);
    }
}
