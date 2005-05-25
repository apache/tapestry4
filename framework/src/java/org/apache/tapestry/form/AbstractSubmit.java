// Copyright 2004, 2005 The Apache Software Foundation
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

import java.util.Collection;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerInvoker;

/**
 * Superclass for components submitting their form.
 * 
 * @author Richard Lewis-Shell
 * @since 4.0
 */

abstract class AbstractSubmit extends AbstractFormComponent
{

    /**
     * Determine if this submit component was clicked.
     * 
     * @param cycle
     * @param name
     * @return true if this submit was clicked
     */
    protected abstract boolean isClicked(IRequestCycle cycle, String name);

    /**
     * Write the tag (and any nested content) for this submit component.
     * 
     * @param writer
     * @param cycle
     * @param name
     */
    protected abstract void writeTag(IMarkupWriter writer, IRequestCycle cycle, String name);

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        if (form.wasPrerendered(writer, this))
            return;

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);

        if (rewinding)
        {
            // Don't bother doing anything if disabled.
            if (isDisabled())
                return;

            if (isClicked(cycle, name))
                handleClick(cycle, form);

            return;
        }

        writeTag(writer, cycle, name);
    }

    void handleClick(final IRequestCycle cycle, IForm form)
    {
        if (isParameterBound("selected"))
            setSelected(getTag());

        final IActionListener listener = getListener();

        if (listener == null)
            return;

        final ListenerInvoker listenerInvoker = getListenerInvoker();

        Object parameters = getParameters();
        if (parameters != null)
        {
            if (parameters instanceof Collection)
            {
                cycle.setListenerParameters(((Collection) parameters).toArray());
            }
            else
            {
                cycle.setListenerParameters(new Object[]
                { parameters });
            }
        }

        // Have a listener; notify it now, or defer for later?

        Runnable notify = new Runnable()
        {
            public void run()
            {
                listenerInvoker.invokeListener(listener, AbstractSubmit.this, cycle);
            }
        };

        if (getDefer())
            form.addDeferredRunnable(notify);
        else
            notify.run();
    }

    /** parameter */
    public abstract boolean isDisabled();

    /** parameter */
    public abstract IActionListener getListener();

    /** parameter */
    public abstract Object getTag();

    /** parameter */
    public abstract void setSelected(Object tag);

    /** parameter */
    public abstract boolean getDefer();

    /** parameter */
    public abstract Object getParameters();

    /** Injected */
    public abstract ListenerInvoker getListenerInvoker();
}
