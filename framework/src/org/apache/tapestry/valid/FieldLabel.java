/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.valid;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.form.IFormComponent;

/**
 *  Used to label an {@link IFormComponent}.  Because such fields
 *  know their displayName (user-presentable name), there's no reason
 *  to hard code the label in a page's HTML template (this also helps
 *  with localization).
 *
 *  [<a href="../../../../../ComponentReference/FieldLabel.html">Component Reference</a>]

 *
 *  @author Howard Lewis Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class FieldLabel extends AbstractComponent
{
    /**
    *  Gets the {@link IFormComponent}
    *  and {@link IValidationDelegate delegate},
    *  then renders the label obtained from the field.  Does nothing
    *  when rewinding.
    *
    **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        IFormComponent field = getField();
        String displayName = getDisplayName();

        if (displayName == null)
        {
            if (field == null)
                throw Tapestry.createRequiredParameterException(this, "field");

            displayName = field.getDisplayName();

            if (displayName == null)
            {
                String msg = Tapestry.format("FieldLabel.no-display-name", field.getExtendedId());

                throw new BindingException(msg, this, null, getBinding("field"), null);
            }
        }

        IForm form = Form.get(cycle);

        if (form == null)
        {
            String msg = Tapestry.getMessage("FieldLabel.must-be-contained-by-form");

            throw new ApplicationRuntimeException(msg, this, null, null);
        }

        IValidationDelegate delegate = form.getDelegate();

        if (delegate == null)
        {
            String msg =
                Tapestry.format("FieldLabel.no-delegate", getExtendedId(), form.getExtendedId());

            throw new ApplicationRuntimeException(msg, this, null, null);
        }

        delegate.writeLabelPrefix(field, writer, cycle);

        writer.print(displayName);

        delegate.writeLabelSuffix(field, writer, cycle);
    }

    public abstract String getDisplayName();

    public abstract IFormComponent getField();
}
