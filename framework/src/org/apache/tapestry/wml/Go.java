/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
        writer.beginEmpty(getInputTag());
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
            Tapestry.getString("unsupported-property", this, "delegate"));
    }

    protected String getTag()
    {
        return "go";
    }

    protected String getInputTag()
    {
        return "postfield";
    }

    protected String getDisplayName()
    {
        return "Go";
    }
}
