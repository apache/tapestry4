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

package org.apache.tapestry.services;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ITemplateComponent;

/**
 * Service interface for <code>tapestry.page.ComponentTemplateLoader</code>; responsible for
 * finding and integrating a component (or page) template with the component (or page) instance.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface ComponentTemplateLoader
{
    public void loadTemplate(IRequestCycle requestCycle, ITemplateComponent loadComponent);
}