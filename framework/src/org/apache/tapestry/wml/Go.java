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
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 *  The go element declares a go task, indicating navigation to a URI. If the URI
 *  names a WML card or deck, it is displayed. 
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class Go extends Form
{

    /** @since 3.0 **/

    protected void writeAttributes(IMarkupWriter writer, ILink link)
    {
        String method = getMethod();

        writer.begin(getTag());
        writer.attribute("method", (method == null) ? "post" : method);
        writer.attribute("href", link.getURL(null, false));
    }

    /** @since 3.0 **/

    protected void writeHiddenField(IMarkupWriter writer, String name, String value)
    {
        writer.beginEmpty("postfield");
        writer.attribute("name", name);
        writer.attribute("value", value);
        writer.println();
    }

    /**
     *  This component doesn't support event handlers.
     *
     **/
    protected void emitEventHandlers(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    /**
     *  This component doesn't support delegate.
     *
     **/
    public IValidationDelegate getDelegate()
    {
        return null;
    }

    public void setDelegate(IValidationDelegate delegate)
    {
        throw new ApplicationRuntimeException(
            Tapestry.format("unsupported-property", this, "delegate"));
    }

    protected String getTag()
    {
        return "go";
    }


    protected String getDisplayName()
    {
        return "Go";
    }
}
