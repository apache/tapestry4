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

package org.apache.tapestry.wml;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.AbstractFormComponent;

/**
 *  A base class for building components that correspond to WML postfield elements.
 *  All such components must be wrapped (directly or indirectly) by
 *  a {@link Go} component.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class AbstractPostfield extends AbstractFormComponent
{

    /**
     *  Returns the {@link org.apache.tapestry.wml.Go} wrapping this component.
     *
     *  @throws  ApplicationRuntimeException if the component is not wrapped by a
     * {@link org.apache.tapestry.wml.Go}.
     *
     **/

    public IForm getForm(IRequestCycle cycle)
    {
        IForm result = Go.get(cycle);

        if (result == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Postfield.must-be-contained-by-go"),
                this,
                null,
                null);

        setForm(result);

        return result;
    }

    /**
     *  @see org.apache.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        if (!rewinding && cycle.isRewinding())
            return;

        String name = form.getElementId(this);

        if (rewinding)
        {
            rewind(cycle);
            return;
        }

        writer.beginEmpty("postfield");

        writer.attribute("name", name);
        String varName = getVarName();
        writer.attributeRaw("value", varName != null ? getEncodedVarName(varName) : "");

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    protected abstract void rewind(IRequestCycle cycle);

    private String getEncodedVarName(String varName)
    {
        return "$(" + varName + ")";
    }

    public boolean isDisabled()
    {
        return false;
    }

    public abstract String getVarName();

    public abstract IBinding getValueBinding();

    public void updateValue(Object value)
    {
        getValueBinding().setObject(value);
    }

    public abstract IForm getForm();
    public abstract void setForm(IForm form);

    public abstract String getName();
    public abstract void setName(String name);
}
