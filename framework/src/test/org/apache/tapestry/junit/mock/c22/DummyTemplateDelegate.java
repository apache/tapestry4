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

package org.apache.tapestry.junit.mock.c22;

import java.util.Locale;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ITemplateSourceDelegate;
import org.apache.tapestry.parse.ComponentTemplate;

/**
 * Used to test the {@link org.apache.tapestry.engine.ITemplateSourceDelegate}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class DummyTemplateDelegate implements ITemplateSourceDelegate
{

    public ComponentTemplate findTemplate(IRequestCycle cycle, IComponent component, Locale locale)
    {
        cycle.getRequestContext().getSession().setAttribute(
                "template",
                component.getSpecification().getSpecificationLocation().toString());

        return null;
    }

}