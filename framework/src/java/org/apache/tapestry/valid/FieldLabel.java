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

package org.apache.tapestry.valid;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.IFormComponent;

/**
 * Used to label an {@link IFormComponent}. Because such fields know their displayName
 * (user-presentable name), there's no reason to hard code the label in a page's HTML template (this
 * also helps with localization). [ <a
 * href="../../../../../ComponentReference/FieldLabel.html">Component Reference </a>]
 * 
 * @author Howard Lewis Lewis Ship
 */

public abstract class FieldLabel extends AbstractComponent
{
    /**
     * Gets the {@link IForm}&nbsp;and {@link IValidationDelegate delegate}, then renders the
     * label obtained from the field. Does nothing when rewinding.
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        IForm form = TapestryUtils.getForm(cycle, this);

        IFormComponent field = getField();

        if (field != null)
            form.prerenderField(writer, field, getLocation());

        String displayName = getDisplayName();

        if (displayName == null)
        {
            if (field == null)
                throw Tapestry.createRequiredParameterException(this, "field");

            displayName = field.getDisplayName();

            if (displayName == null)
                throw new BindingException(ValidMessages.noDisplayName(this, field), this, null,
                        getBinding("field"), null);
        }

        IValidationDelegate delegate = form.getDelegate();

        delegate.writeLabelPrefix(field, writer, cycle);

        writer.print(displayName, getRaw());

        delegate.writeLabelSuffix(field, writer, cycle);
    }

    /** displayName parameter */
    public abstract String getDisplayName();

    /** field parameter */
    public abstract IFormComponent getField();

    /** raw parameter */
    public abstract boolean getRaw();
}