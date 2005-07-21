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
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.ImageSubmit}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestImageSubmit extends BaseFormComponentTest
{
    protected IAsset newAsset(IRequestCycle cycle, String imageURL)
    {
        MockControl control = newControl(IAsset.class);
        IAsset asset = (IAsset) control.getMock();

        asset.buildURL(cycle);
        control.setReturnValue(imageURL);

        return asset;
    }

    public void testPrerendered()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class);

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, true);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRender()
    {
        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();
        IAsset image = newAsset(cycle, "image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "image", image });

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(submit);
        delegatec.setVoidCallable();

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, false);

        trainIsRewinding(cyclec, cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "fred");
        writer.attribute("border", 0);
        writer.attribute("src", "image-url");
        writer.closeTag();

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        trainIsInError(delegatec, delegate, false);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRenderDisabled()
    {
        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();
        IAsset image = newAsset(cycle, "disabled-image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "disabledImage", image, "disabled", Boolean.TRUE });

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, false);

        trainIsRewinding(cyclec, cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");
        writer.attribute("border", 0);
        writer.attribute("src", "disabled-image-url");
        writer.closeTag();

        trainIsInError(delegatec, delegate, false);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRenderDisabledNoDisabledImage()
    {
        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();
        IAsset image = newAsset(cycle, "image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "image", image, "disabled", Boolean.TRUE });

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, false);

        trainIsRewinding(cyclec, cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");
        writer.attribute("border", 0);
        writer.attribute("src", "image-url");
        writer.closeTag();

        trainIsInError(delegatec, delegate, false);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRenderWithNameOverride()
    {
        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();
        IAsset image = newAsset(cycle, "image-url");

        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "image", image, "nameOverride", "barney" });

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        form.getElementId(submit, "barney");
        formc.setReturnValue("barney$0");

        trainIsRewinding(formc, form, false);

        trainIsRewinding(cyclec, cycle, false);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", "barney$0");
        writer.attribute("border", 0);
        writer.attribute("src", "image-url");
        writer.closeTag();

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        trainIsInError(delegatec, delegate, false);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRewindingDisabled()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "disabled", Boolean.TRUE });

        IValidationDelegate delegate = newDelegate();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, true);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRewindNotTrigger()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class);

        IValidationDelegate delegate = newDelegate();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, true);

        trainGetParameter(cyclec, cycle, "fred.x", null);

        replayControls();

        submit.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRewindTrigger()
    {
        Creator creator = new Creator();
        ImageSubmit submit = (ImageSubmit) creator.newInstance(ImageSubmit.class, new Object[]
        { "tag", "clicked" });

        IBinding binding = newBinding();
        submit.setBinding("selected", binding);

        IValidationDelegate delegate = newDelegate();
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, true);

        trainGetParameter(cyclec, cycle, "fred.x", "33");

        replayControls();

        submit.renderComponent(writer, cycle);

        assertEquals("clicked", PropertyUtils.read(submit, "selected"));

        // Note: we rely on the fact that ImageSubmit subclasses
        // from Submit to test some of the extra logic about
        // notifying listeners (deferred or not).
        // This test "proves" that Submit.handleClick() is invoked.

        verifyControls();
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
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);

        trainWasPrerendered(formc, form, writer, submit, false);

        form.getDelegate();
        formc.setReturnValue(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(formc, form, submit, "fred");

        trainIsRewinding(formc, form, true);

        trainGetParameter(cyclec, cycle, "fred.x", "33");
        trainGetParameter(cyclec, cycle, "fred.x", "33");
        trainGetParameter(cyclec, cycle, "fred.y", "19");

        replayControls();

        submit.renderComponent(writer, cycle);

        assertEquals("clicked", PropertyUtils.read(submit, "selected"));
        assertEquals(new Point(33, 19), PropertyUtils.read(submit, "point"));

        verifyControls();
    }
}
