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

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.Form}. Most of the testing is, still alas, done with
 * mock objects.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestForm extends BaseComponentTestCase
{
    private IActionListener newListener()
    {
        return (IActionListener) newMock(IActionListener.class);
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
