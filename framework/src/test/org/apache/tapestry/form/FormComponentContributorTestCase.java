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

import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.junit.TapestryTestCase;
import org.easymock.MockControl;
import org.easymock.internal.ArrayMatcher;

/**
 * Abstract test case for {@link FormComponentContributor}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public abstract class FormComponentContributorTestCase extends TapestryTestCase
{
    // Paul,
    //
    // Think you missed the newControl() and newMock() methods inherited from HiveMindTestCase.
    // Those exist to eliminate the need for all this stuff. Instead, you create newFoo() methods
    // that
    // create and initialize a Foo instance.
    // -- Howard

    protected MockControl _componentControl = MockControl.createControl(IFormComponent.class);

    protected IFormComponent _component = (IFormComponent) _componentControl.getMock();

    protected MockControl _pageControl = MockControl.createControl(IPage.class);

    protected IPage _page = (IPage) _pageControl.getMock();

    protected MockControl _cycleControl = MockControl.createControl(IRequestCycle.class);

    protected IRequestCycle _cycle = (IRequestCycle) _cycleControl.getMock();

    protected MockControl _formControl = MockControl.createControl(IForm.class);

    protected IForm _form = (IForm) _formControl.getMock();

    protected MockControl _engineControl = MockControl.createControl(IEngine.class);

    protected IEngine _engine = (IEngine) _engineControl.getMock();

    protected MockControl _pageRenderSupportControl = MockControl
            .createControl(PageRenderSupport.class);

    protected PageRenderSupport _pageRenderSupport = (PageRenderSupport) _pageRenderSupportControl
            .getMock();

    /**
     * @see org.apache.hivemind.test.HiveMindTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        _componentControl.reset();
        _pageControl.reset();
        _cycleControl.reset();
        _formControl.reset();
        _engineControl.reset();
        _pageRenderSupportControl.reset();

        super.tearDown();
    }

    protected void replay()
    {
        _componentControl.replay();
        _pageControl.replay();
        _cycleControl.replay();
        _formControl.replay();
        _engineControl.replay();
        _pageRenderSupportControl.replay();
    }

    protected void verify()
    {
        _componentControl.verify();
        _pageControl.verify();
        _cycleControl.verify();
        _formControl.verify();
        _engineControl.verify();
        _pageRenderSupportControl.verify();
    }

    protected void addScript(String script)
    {
        _cycle.getEngine();
        _cycleControl.setReturnValue(_engine);

        _engine.getClassResolver();
        _engineControl.setReturnValue(null);

        _cycle.getAttribute("org.apache.tapestry.PageRenderSupport");
        _cycleControl.setReturnValue(_pageRenderSupport);

        _pageRenderSupport.addExternalScript(new ClasspathResource(null, script));
        _pageRenderSupportControl.setVoidCallable();
    }

    protected IFormComponent newField(String displayName)
    {
        MockControl control = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) control.getMock();

        field.getDisplayName();
        control.setReturnValue(displayName);

        return field;
    }

    protected void trainGetFieldDOM(MockControl control, FormComponentContributorContext context,
            String result)
    {
        context.getFieldDOM();
        control.setReturnValue(result);
    }

    protected void trainBuildMessage(MockControl control, ValidationMessages messages,
            String overrideMessage, String key, Object[] parameters, String result)
    {
        messages.formatValidationMessage(overrideMessage, key, parameters);
        control.setMatcher(new ArrayMatcher());
        control.setReturnValue(result);
    }

    protected void trainGetLocale(MockControl control, ValidationMessages messages, Locale locale)
    {
        messages.getLocale();
        control.setReturnValue(locale);
    }

    protected IFormComponent newField()
    {
        return (IFormComponent) newMock(IFormComponent.class);
    }

    protected IMarkupWriter newWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected ValidationMessages newValidationMessages(Locale locale)
    {
        MockControl control = newControl(ValidationMessages.class);
        ValidationMessages messages = (ValidationMessages) control.getMock();

        messages.getLocale();
        control.setReturnValue(locale);

        return messages;
    }
}
