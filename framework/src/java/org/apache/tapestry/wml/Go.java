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

import org.apache.tapestry.FormSupport;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.Form;

/**
 * The go element declares a go task, indicating navigation to a URI. If the URI names a WML card or
 * deck, it is displayed.
 * 
 * @author David Solis
 * @since 3.0
 */

public abstract class Go extends Form
{
    /**
     * This component doesn't support event handlers.
     */
    protected void emitEventHandlers(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    protected String constructFormNameForActionService(String actionId)
    {
        return "Go" + actionId;
    }

    protected FormSupport newFormSupport(IMarkupWriter writer, IRequestCycle cycle)
    {
        return new GoFormSupportImpl(writer, cycle, this);
    }
}