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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  SelectionField specifies a postfield element and it is used to complement the {@link PropertySelection} component.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/
public abstract class SelectionField extends AbstractPostfield
{
    protected void rewind(IRequestCycle cycle)
    {
        String optionValue = cycle.getRequestContext().getParameter(getName());
        IPropertySelectionModel model = getModel();
        Object value = (optionValue == null) ? null : model.translateValue(optionValue);

        updateValue(value);
    }

    public abstract IPropertySelectionModel getModel();
}
