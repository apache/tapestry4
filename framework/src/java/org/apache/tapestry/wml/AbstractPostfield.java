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

package org.apache.tapestry.wml;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponent;

/**
 * A base class for building components that correspond to WML postfield elements. All such
 * components must be wrapped (directly or indirectly) by a {@link Go}component.
 * 
 * @author David Solis
 * @since 3.0
 */

public abstract class AbstractPostfield extends AbstractFormComponent
{
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.beginEmpty("postfield");

        writer.attribute("name", getName());

        String varName = getVarName();
        writer.attributeRaw("value", varName != null ? getEncodedVarName(varName) : "");

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    private String getEncodedVarName(String varName)
    {
        return "$(" + varName + ")";
    }

    public boolean isDisabled()
    {
        return false;
    }

    public abstract String getVarName();

    public void updateValue(Object value)
    {
        getBinding("value").setObject(value);
    }
}