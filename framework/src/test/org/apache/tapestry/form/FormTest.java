// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IgnoreMatcher;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.web.WebResponse;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.Form}. Most of the testing is, still alas, done with
 * mock objects.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class FormTest extends BaseComponentTestCase
{
    private IActionListener newListener()
    {
        return (IActionListener) newMock(IActionListener.class);
    }

    private FormSupport newFormSupport()
    {
        return (FormSupport) newMock(FormSupport.class);
    }

    protected void trainGetNextActionId(IRequestCycle cycle, String actionId)
    {
        cycle.getNextActionId();
        setReturnValue(cycle, actionId);
    }

    protected void trainGetUniqueId(IRequestCycle cycle, String baseId, String uniqueId)
    {
        cycle.getUniqueId(baseId);

        setReturnValue(cycle, uniqueId);
    }

    protected WebResponse newResponse()
    {
        return (WebResponse) newMock(WebResponse.class);
    }

    protected void trainGetNamespace(WebResponse response, String namespace)
    {
        response.getNamespace();
        setReturnValue(response, namespace);
    }

    protected IValidationDelegate newDelegate()
    {
        return (IValidationDelegate) newMock(IValidationDelegate.class);
    }

    public void testRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormSupport support = newFormSupport();
        IValidationDelegate delegate = newDelegate();
        IActionListener listener = newListener();
        ListenerInvoker invoker = newListenerInvoker();

        Form form = (Form) newInstance(FormFixture.class, new Object[]
        { "id", "myform", "direct", true, "expectedWriter", writer, "expectedRequestCycle", cycle,
                "formSupport", support, "listener", listener, "listenerInvoker", invoker,
                "delegate", delegate });

        trainStoreForm(cycle, form);

        trainIsRewinding(support, true);

        trainGetNextActionId(cycle, "7");

        support.rewind();
        setReturnValue(support, FormConstants.SUBMIT_NORMAL);

        delegate.getHasErrors();
        setReturnValue(delegate, false);

        invoker.invokeListener(listener, form, cycle);

        delegate.setFormComponent(null);

        TapestryUtils.removeForm(cycle);

        replayControls();

        try
        {
            form.render(writer, cycle);
        }
        catch (RenderRewoundException ex)
        {
            assertSame(form, ex.getComponent());
        }

        verifyControls();
    }

    private ListenerInvoker newListenerInvoker()
    {
        return (ListenerInvoker) newMock(ListenerInvoker.class);
    }

    public void testBasicRender()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormSupport support = newFormSupport();
        WebResponse response = newResponse();
        IEngineService direct = newEngineService();
        ILink link = newLink();
        IRender render = newRender();
        IValidationDelegate delegate = newDelegate();

        Form form = (Form) newInstance(FormFixture.class, new Object[]
        { "id", "myform", "direct", true, "expectedWriter", writer, "expectedRequestCycle", cycle,
                "formSupport", support, "response", response, "directService", direct, "method",
                "post", "delegate", delegate });

        trainStoreForm(cycle, form);

        trainIsRewinding(support, false);

        trainGetNextActionId(cycle, "7");

        trainGetUniqueId(cycle, "myform", "myform_1");

        trainGetNamespace(response, "$ns");

        trainGetLinkCheckIgnoreParameter(
                direct,
                cycle,
                true,
                new DirectServiceParameter(form),
                link);

        trainRender(support, link, render, null);

        delegate.setFormComponent(null);

        TapestryUtils.removeForm(cycle);

        replayControls();

        form.render(writer, cycle);

        verifyControls();
    }

    public void testRenderWithScheme()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormSupport support = newFormSupport();
        WebResponse response = newResponse();
        IEngineService direct = newEngineService();
        ILink link = newLink();
        IRender render = newRender();
        IValidationDelegate delegate = newDelegate();

        Form form = (Form) newInstance(FormFixture.class, new Object[]
        { "id", "myform", "direct", true, "expectedWriter", writer, "expectedRequestCycle", cycle,
                "formSupport", support, "response", response, "directService", direct, "method",
                "post", "delegate", delegate, "scheme", "https" });

        trainStoreForm(cycle, form);

        trainIsRewinding(support, false);

        trainGetNextActionId(cycle, "7");

        trainGetUniqueId(cycle, "myform", "myform_1");

        trainGetNamespace(response, "$ns");

        trainGetLinkCheckIgnoreParameter(
                direct,
                cycle,
                true,
                new DirectServiceParameter(form),
                link);

        trainRender(support, link, render, "https");

        delegate.setFormComponent(null);

        TapestryUtils.removeForm(cycle);

        replayControls();

        form.render(writer, cycle);

        verifyControls();
    }

    protected void trainStoreForm(IRequestCycle cycle, IForm form)
    {
        trainGetAttribute(cycle, TapestryUtils.FORM_ATTRIBUTE, null);

        cycle.setAttribute(TapestryUtils.FORM_ATTRIBUTE, form);
    }

    private void trainRender(FormSupport support, ILink link, IRender render, String scheme)
    {
        support.render("post", render, link, scheme);
        getControl(support).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, new IgnoreMatcher(), null, null }));
    }

    protected void trainIsRewinding(FormSupport support, boolean isRewinding)
    {

        support.isRewinding();
        setReturnValue(support, isRewinding);
    }

    public void testFindCancelListener()
    {
        IActionListener cancel = newListener();
        IActionListener listener = newListener();

        replayControls();

        Form form = (Form) newInstance(Form.class, new Object[]
        { "listener", listener, "cancel", cancel });

        assertSame(cancel, form.findListener(FormConstants.SUBMIT_CANCEL));

        verifyControls();
    }

    public void testFindCancelDefaultListener()
    {
        IActionListener listener = newListener();

        replayControls();

        Form form = (Form) newInstance(Form.class, "listener", listener);

        assertSame(listener, form.findListener(FormConstants.SUBMIT_CANCEL));

        verifyControls();
    }

    public void testFindRefreshListener()
    {
        IActionListener refresh = newListener();
        IActionListener listener = newListener();

        replayControls();

        Form form = (Form) newInstance(Form.class, new Object[]
        { "listener", listener, "refresh", refresh });

        assertSame(refresh, form.findListener(FormConstants.SUBMIT_REFRESH));

        verifyControls();
    }

    public void testFindRefreshListenerDefault()
    {
        IActionListener listener = newListener();

        replayControls();

        Form form = (Form) newInstance(Form.class, new Object[]
        { "listener", listener });

        assertSame(listener, form.findListener(FormConstants.SUBMIT_REFRESH));

        verifyControls();
    }

    public void testFindListenerSuccess()
    {
        IActionListener cancel = newListener();
        IActionListener refresh = newListener();
        IActionListener success = newListener();
        IActionListener listener = newListener();

        IValidationDelegate delegate = newDelegate(false);

        replayControls();

        Form form = (Form) newInstance(Form.class, new Object[]
        { "delegate", delegate, "success", success, "cancel", cancel, "refresh", refresh,
                "listener", listener });

        assertSame(success, form.findListener(FormConstants.SUBMIT_NORMAL));

        verifyControls();
    }

    public void testFindListenerValidationErrors()
    {
        IActionListener cancel = newListener();
        IActionListener refresh = newListener();
        IActionListener success = newListener();
        IActionListener listener = newListener();

        IValidationDelegate delegate = newDelegate(true);

        replayControls();

        Form form = (Form) newInstance(Form.class, new Object[]
        { "delegate", delegate, "success", success, "cancel", cancel, "refresh", refresh,
                "listener", listener });

        assertSame(listener, form.findListener(FormConstants.SUBMIT_NORMAL));

        verifyControls();
    }

    private IValidationDelegate newDelegate(boolean hasErrors)
    {
        MockControl control = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) control.getMock();

        delegate.getHasErrors();
        control.setReturnValue(hasErrors);

        return delegate;
    }
}
