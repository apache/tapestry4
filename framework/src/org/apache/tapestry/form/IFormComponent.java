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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;

/**
 *  A common interface implemented by all form components (components that
 *  create interactive elements in the rendered page).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IFormComponent extends IComponent
{
    /**
     *  Returns the {@link org.apache.tapestry.IForm} which contains the component,
     *  or null if the component is not contained by a form,
     *  of if the containing Form is not currently renderring.
     * 
     **/

    public IForm getForm();

    /**
     *  Returns the name of the component, which is automatically generated
     *  during renderring.
     *
     *  <p>This value is set inside the component's render method and is
     *  <em>not</em> cleared.  If the component is inside a {@link org.apache.tapestry.components.Foreach}, the
     *  value returned is the most recent name generated for the component.
     *
     *  <p>This property is made available to facilitate writing JavaScript that
     *  allows components (in the client web browser) to interact.
     *
     *  <p>In practice, a {@link org.apache.tapestry.html.Script} component
     *  works with the {@link org.apache.tapestry.html.Body} component to get the
     *  JavaScript code inserted and referenced.
     *
     **/

    public String getName();
    
    /**
     *  Invoked by {@link IForm#getElementId(IFormComponent)} when a name is created
     *  for a form component.
     * 
     *  @since 3.0
     * 
     **/
    
    public void setName(String name);

    /**
     *  May be implemented to return a user-presentable, localized name for the component,
     *  which is used in labels or error messages.  Most components simply return null.
     * 
     *  @since 1.0.9
     * 
     **/

    public String getDisplayName();
    
    /**
     *  Returns true if the component is disabled.  This is important when the containing
     *  form is submitted, since disabled parameters do not update their bindings.
     * 
     *  @since 2.2
     * 
     **/
    
    public boolean isDisabled();
}