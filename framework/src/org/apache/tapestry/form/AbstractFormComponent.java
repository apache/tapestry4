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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 *  A base class for building components that correspond to HTML form elements.
 *  All such components must be wrapped (directly or indirectly) by
 *  a {@link Form} component.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 1.0.3
 * 
 **/

public abstract class AbstractFormComponent extends AbstractComponent implements IFormComponent
{
    /**
     *  Returns the {@link Form} wrapping this component.  Invokes
     *  {@link #setForm(IForm)} (so that the component may know, later, what the
     *  form is).  Also, if the form has a delegate, 
     *  then {@link IValidationDelegate#setFormComponent(IFormComponent)} is invoked.
     *
     *  @throws ApplicationRuntimeException if the component is not wrapped by a {@link Form}.
     *
     **/

    public IForm getForm(IRequestCycle cycle)
    {
        IForm result = Form.get(cycle);

        if (result == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("AbstractFormComponent.must-be-contained-by-form"),
                this,
                null,
                null);

        setForm(result);

        IValidationDelegate delegate = result.getDelegate();

        if (delegate != null)
            delegate.setFormComponent(this);

        return result;
    }

    public abstract IForm getForm();
    public abstract void setForm(IForm form);

    public abstract String getName();
    public abstract void setName(String name);

    /**
     *  Implemented in some subclasses to provide a display name (suitable
     *  for presentation to the user as a label or error message).  This implementation
     *  return null.
     * 
     **/

    public String getDisplayName()
    {
        return null;
    }
}