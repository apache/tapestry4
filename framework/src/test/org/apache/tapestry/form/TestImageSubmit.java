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
import static org.testng.AssertJUnit.assertEquals;

import java.awt.Point;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstants;

/**
 * Tests for {@link org.apache.tapestry.form.ImageSubmit}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestImageSubmit extends BaseFormComponentTestCase
{
    protected IAsset newAsset(IRequestCycle cycle, String imageURL)
    {
        IAsset asset = (IAsset)newMock(IAsset.class);

        expect(asset.buildURL()).andReturn(imageURL);

        return asset;
    }

    public void testPrerendered()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class);

        IForm form = (IForm)newMock(IForm.class);
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, true);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRender()
    {
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        IAsset image = newAsset(cycle, "image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "image", image });

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(submit);
        
        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "fred");
        writer.attribute("border", 0);
        writer.attribute("src", "image-url");
        writer.closeTag();
        
        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRenderDisabled()
    {
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        IAsset image = newAsset(cycle, "disabled-image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "disabledImage", image, "disabled", Boolean.TRUE });

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");
        writer.attribute("border", 0);
        writer.attribute("src", "disabled-image-url");
        writer.closeTag();

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRenderDisabledNoDisabledImage()
    {
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        IAsset image = newAsset(cycle, "image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "image", image, "disabled", Boolean.TRUE });

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");
        writer.attribute("border", 0);
        writer.attribute("src", "image-url");
        writer.closeTag();

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRenderWithNameOverride()
    {
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        IAsset image = newAsset(cycle, "image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "image", image, "nameOverride", "barney" });

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        expect(form.getElementId(submit, "barney")).andReturn("barney$0");
        
        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "barney$0");
        writer.attribute("border", 0);
        writer.attribute("src", "image-url");
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRewindingDisabled()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "disabled", Boolean.TRUE });

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);
        
        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRewindNotTrigger()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class);

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        trainGetParameter(cycle, "fred.x", null);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRewindTrigger()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "tag", "clicked" });

        IBinding binding = newBinding();
        submit.setBinding("selected", binding);

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        trainGetParameter(cycle, "fred.x", "33");

        replay();

        submit.renderComponent(writer, cycle);

        assertEquals("clicked", PropertyUtils.read(submit, "selected"));

        // Note: we rely on the fact that ImageSubmit subclasses
        // from Submit to test some of the extra logic about
        // notifying listeners (deferred or not).
        // This test "proves" that Submit.handleClick() is invoked.

        verify();
    }

    public void testRewindTriggeredWithPointBound()
    {
        // Note: because there isn't a real Form to set the name, we'll do it here.

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "tag", "clicked", "name", "fred" });

        IBinding binding = newBinding();
        submit.setBinding("selected", binding);
        submit.setBinding("point", binding);

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        trainGetParameter(cycle, "fred.x", "33");
        trainGetParameter(cycle, "fred.x", "33");
        trainGetParameter(cycle, "fred.y", "19");

        replay();

        submit.renderComponent(writer, cycle);

        assertEquals("clicked", PropertyUtils.read(submit, "selected"));
        assertEquals(new Point(33, 19), PropertyUtils.read(submit, "point"));

        verify();
    }
}
