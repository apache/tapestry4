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

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.testng.AssertJUnit.assertSame;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.web.WebResponse;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.Form}. Most of the testing is, still alas, done with
 * mock objects.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class FormTest extends BaseComponentTestCase
{
    private IActionListener newListener()
    {
        return newMock(IActionListener.class);
    }

    private FormSupport newFormSupport()
    {
        return newMock(FormSupport.class);
    }

    protected void trainGetNextActionId(IRequestCycle cycle, String actionId)
    {
        expect(cycle.getNextActionId()).andReturn(actionId);
    }

    protected void trainGetUniqueId(IRequestCycle cycle, String baseId, String uniqueId)
    {
        expect(cycle.getUniqueId(baseId)).andReturn(uniqueId);
    }

    protected WebResponse newResponse()
    {
        return newMock(WebResponse.class);
    }

    protected void trainGetNamespace(WebResponse response, String namespace)
    {
        expect(response.getNamespace()).andReturn(namespace);
    }

    protected IValidationDelegate newDelegate()
    {
        return newMock(IValidationDelegate.class);
    }

    public void testRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormSupport support = newFormSupport();
        IValidationDelegate delegate = newDelegate();
        IActionListener listener = newListener();
        ListenerInvoker invoker = newListenerInvoker();

        Form form = newInstance(FormFixture.class, new Object[]
        { "id", "myform", "direct", true, "expectedWriter", writer, "expectedRequestCycle", cycle,
                "formSupport", support, "listener", listener, "listenerInvoker", invoker,
                "delegate", delegate });

        trainStoreForm(cycle, form);

        trainIsRewinding(support, true);

        trainGetNextActionId(cycle, "7");

        expect(support.rewind()).andReturn(FormConstants.SUBMIT_NORMAL);
        
        expect(delegate.getHasErrors()).andReturn(false);

        invoker.invokeListener(listener, form, cycle);

        delegate.setFormComponent(null);

        TapestryUtils.removeForm(cycle);

        replay();

        try
        {
            form.render(writer, cycle);
        }
        catch (RenderRewoundException ex)
        {
            assertSame(form, ex.getComponent());
        }

        verify();
    }

    private ListenerInvoker newListenerInvoker()
    {
        return newMock(ListenerInvoker.class);
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

        Form form = newInstance(FormFixture.class, new Object[]
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

        trainRender(support, link, render, null, null);

        delegate.setFormComponent(null);

        TapestryUtils.removeForm(cycle);

        replay();

        form.render(writer, cycle);

        verify();
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

        Form form = newInstance(FormFixture.class, new Object[]
        { "id", "myform", "direct", true, "expectedWriter", writer, "expectedRequestCycle", cycle,
                "formSupport", support, "response", response, "directService", direct, "method",
                "post", "delegate", delegate, "scheme", "https", "port", new Integer(443) });

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

        trainRender(support, link, render, "https", new Integer(443));

        delegate.setFormComponent(null);

        TapestryUtils.removeForm(cycle);

        replay();

        form.render(writer, cycle);

        verify();
    }

    protected void trainStoreForm(IRequestCycle cycle, IForm form)
    {
        trainGetAttribute(cycle, TapestryUtils.FORM_ATTRIBUTE, null);

        cycle.setAttribute(TapestryUtils.FORM_ATTRIBUTE, form);
    }

    private void trainRender(FormSupport support, ILink link, IRender render, String scheme, Integer port)
    {
        support.render(eq("post"), isA(IRender.class), eq(link), eq(scheme), eq(port));
    }

    protected void trainIsRewinding(FormSupport support, boolean isRewinding)
    {
        expect(support.isRewinding()).andReturn(isRewinding);
    }

    public void testFindCancelListener()
    {
        IActionListener cancel = newListener();
        IActionListener listener = newListener();

        replay();

        Form form = newInstance(Form.class, new Object[]
        { "listener", listener, "cancel", cancel });

        assertSame(cancel, form.findListener(FormConstants.SUBMIT_CANCEL));

        verify();
    }

    public void testFindCancelDefaultListener()
    {
        IActionListener listener = newListener();

        replay();

        Form form = (Form) newInstance(Form.class, "listener", listener);

        assertSame(listener, form.findListener(FormConstants.SUBMIT_CANCEL));

        verify();
    }

    public void testFindRefreshListener()
    {
        IActionListener refresh = newListener();
        IActionListener listener = newListener();

        replay();

        Form form = newInstance(Form.class, new Object[]
        { "listener", listener, "refresh", refresh });

        assertSame(refresh, form.findListener(FormConstants.SUBMIT_REFRESH));

        verify();
    }

    public void testFindRefreshListenerDefault()
    {
        IActionListener listener = newListener();

        replay();

        Form form = newInstance(Form.class, new Object[]
        { "listener", listener });

        assertSame(listener, form.findListener(FormConstants.SUBMIT_REFRESH));

        verify();
    }

    public void testFindListenerSuccess()
    {
        IActionListener cancel = newListener();
        IActionListener refresh = newListener();
        IActionListener success = newListener();
        IActionListener listener = newListener();

        IValidationDelegate delegate = newDelegate(false);

        replay();

        Form form = newInstance(Form.class, new Object[]
        { "delegate", delegate, "success", success, "cancel", cancel, "refresh", refresh,
                "listener", listener });

        assertSame(success, form.findListener(FormConstants.SUBMIT_NORMAL));

        verify();
    }

    public void testFindListenerValidationErrors()
    {
        IActionListener cancel = newListener();
        IActionListener refresh = newListener();
        IActionListener success = newListener();
        IActionListener listener = newListener();

        IValidationDelegate delegate = newDelegate(true);

        replay();
        
        Form form = newInstance(Form.class, new Object[]
        { "delegate", delegate, "success", success, "cancel", cancel, "refresh", refresh,
                "listener", listener });

        assertSame(listener, form.findListener(FormConstants.SUBMIT_NORMAL));

        verify();
    }

    private IValidationDelegate newDelegate(boolean hasErrors)
    {
        IValidationDelegate delegate = newMock(IValidationDelegate.class);
        
        expect(delegate.getHasErrors()).andReturn(hasErrors);
        
        return delegate;
    }
}
