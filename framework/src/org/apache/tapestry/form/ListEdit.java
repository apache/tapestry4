//  Copyright 2004 The Apache Software Foundation
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
import java.util.Iterator;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.io.DataSqueezer;

/**
 *  A specialized component used to edit a list of items
 *  within a form; it is similar to a {@link org.apache.tapestry.components.Foreach} but leverages
 *  hidden inputs within the &lt;form&gt; to store the items in the list.
 *
 *  [<a href="../../../../../ComponentReference/ListEdit.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public abstract class ListEdit extends AbstractFormComponent
{

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Iterator i = null;

        IForm form = getForm(cycle);

        boolean cycleRewinding = cycle.isRewinding();

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).

        if (cycleRewinding && !form.isRewinding())
            return;

        String name = form.getElementId(this);

        if (!cycleRewinding)
        {
            i = Tapestry.coerceToIterator(getSourceBinding().getObject());
        }
        else
        {
            RequestContext context = cycle.getRequestContext();
            String[] submittedValues = context.getParameters(name);

            i = Tapestry.coerceToIterator(submittedValues);
        }

        // If the source (when rendering), or the submitted values (on submit)
        // are null, then skip the remainder (nothing to update, nothing to
        // render).

        if (i == null)
            return;

        int index = 0;

        IBinding indexBinding = getIndexBinding();
        IBinding valueBinding = getValueBinding();
        IActionListener listener = getListener();
        String element = getElement();

        while (i.hasNext())
        {
            Object value = null;

            if (indexBinding != null)
                indexBinding.setInt(index++);

            if (cycleRewinding)
                value = convertValue((String) i.next());
            else
            {
                value = i.next();
                writeValue(form, name, value);
            }

            valueBinding.setObject(value);

            if (listener != null)
                listener.actionTriggered(this, cycle);

            if (element != null)
            {
                writer.begin(element);
                renderInformalParameters(writer, cycle);
            }

            renderBody(writer, cycle);

            if (element != null)
                writer.end();

        }
    }

    private void writeValue(IForm form, String name, Object value)
    {
        String externalValue;

        try
        {
            externalValue = getDataSqueezer().squeeze(value);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ListEdit.unable-to-convert-value", value),
                this,
                null,
                ex);
        }

        form.addHiddenValue(name, externalValue);
    }

    private Object convertValue(String value)
    {
        try
        {
            return getDataSqueezer().unsqueeze(value);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ListEdit.unable-to-convert-string", value),
                this,
                null,
                ex);
        }
    }

    public abstract String getElement();

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    /** @since 2.2 **/

    public abstract IActionListener getListener();

    /** @since 3.0 **/

    public abstract IBinding getSourceBinding();

    public abstract IBinding getValueBinding();

    public abstract IBinding getIndexBinding();

    /** @since 3.0 **/

    public boolean isDisabled()
    {
        return false;
    }

}