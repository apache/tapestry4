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

import java.io.IOException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.services.DataSqueezer;

/**
 * Implements a hidden field within a {@link Form}. [ <a
 * href="../../../../../ComponentReference/Hidden.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class Hidden extends AbstractFormComponent
{
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm();
        String externalValue = null;

        if (getEncode())
        {
            Object value = getValue();

            try
            {
                externalValue = getDataSqueezer().squeeze(value);
            }
            catch (IOException e)
            {
                throw new ApplicationRuntimeException(e.getMessage(), this, null, e);
            }
        }
        else
            externalValue = (String) getBinding("value").getObject(String.class);

        String id = getElementId();

        form.addHiddenValue(getName(), id, externalValue);
    }
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String parameter = cycle.getParameter(getName());

        Object value = parameter;

        if (getEncode())
        {
            try
            {
                value = getDataSqueezer().unsqueeze(parameter);
            }
            catch (IOException ex)
            {
                throw new ApplicationRuntimeException(ex.getMessage(), this, null, ex);
            }
        }

        // A listener is not always necessary ... it's easy to code
        // the synchronization as a side-effect of the accessor method.

        setValue(value);

        getListenerInvoker().invokeListener(getListener(), this, cycle);
    }

    public abstract String getElementId();

    /** @since 2.2 * */
    public abstract DataSqueezer getDataSqueezer();

    public abstract Object getValue();

    public abstract void setValue(Object value);

    public abstract IActionListener getListener();

    /**
     * Injected.
     * 
     * @since 4.0
     */

    public abstract ListenerInvoker getListenerInvoker();

    /**
     * Returns false. Hidden components are never disabled.
     * 
     * @since 2.2
     */
    public boolean isDisabled()
    {
        return false;
    }

    /**
     * Returns true if the compent encodes object values using a
     * {@link org.apache.tapestry.util.io.DataSqueezerImpl}, false if values are always Strings.
     * 
     * @since 2.2
     */
    public abstract boolean getEncode();
}