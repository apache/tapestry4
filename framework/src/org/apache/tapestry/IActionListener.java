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

package org.apache.tapestry;

/**
 *  Defines a listener to an {@link IAction} component, which is way to
 *  get behavior when the component's URL is triggered (or the form
 *  containing the component is submitted).  Certain form elements 
 *  ({@link org.apache.tapestry.form.Hidden})
 *  also make use of this interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IActionListener
{

    /**
     *  Method invoked by the component (an {@link org.apache.tapestry.link.ActionLink} or 
     *  {@link org.apache.tapestry.form.Form}, when its URL is triggered.
     *
     *  @param component The component which was "triggered".
     *  @param cycle The request cycle in which the component was triggered.
     *
     **/

    public void actionTriggered(IComponent component, IRequestCycle cycle);
}