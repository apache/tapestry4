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

package org.apache.tapestry.form;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.html.Body;

/**
 *  Implements a component that submits its enclosing form via a JavaScript link.
 * 
 *  [<a href="../../../../../ComponentReference/LinkSubmit.html">Component Reference</a>]
 *
 *  @author Richard Lewis-Shell
 *  @version $Id: Submit.java,v 1.6 2003/04/21 13:15:41 glongman Exp $
 * 
 **/

public abstract class LinkSubmit extends AbstractFormComponent
{
    /**
     *  The name of an {@link org.apache.tapestry.IRequestCycle} attribute in which the
     *  current submit link is stored.  LinkSubmits do not nest.
     *
     **/

    public static final String ATTRIBUTE_NAME = "org.apache.tapestry.form.LinkSubmit";

    /**
     * The name of an  {@link org.apache.tapestry.IRequestCycle} attribute in which the
     * link submit component that generates the javascript function is stored.  The
     * function is only required once per page (containing a form with a non-disabled
     * LinkSubmit)
     * 
     **/
    public static final String ATTRIBUTE_FUNCTION_NAME =
        "org.apache.tapestry.form.LinkSubmit_function";

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {

        IForm form = getForm(cycle);
        String formName = form.getName();

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);

        IMarkupWriter wrappedWriter;

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("LinkSubmit.may-not-nest"),
                this,
                null,
                null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        boolean disabled = isDisabled();
        if (!disabled)
        {
            if (!rewinding)
            {
                Body body = Body.get(cycle);

                // make sure the submit function is on the page (once)
                if (cycle.getAttribute(ATTRIBUTE_FUNCTION_NAME) == null)
                {
                    body.addBodyScript(
                        "function submitLink(form, elementId) { form._linkSubmit.value = elementId; if (form.onsubmit == null || form.onsubmit()) form.submit(); }");
                    cycle.setAttribute(ATTRIBUTE_FUNCTION_NAME, this);
                }

                // one hidden field per form:
                String formHiddenFieldAttributeName = ATTRIBUTE_FUNCTION_NAME + formName;
                if (cycle.getAttribute(formHiddenFieldAttributeName) == null)
                {
                    writer.beginEmpty("input");
                    writer.attribute("type", "hidden");
                    writer.attribute("name", "_linkSubmit");
                    cycle.setAttribute(formHiddenFieldAttributeName, this);
                }
            }
            else
            {
                // How to know which Submit link was actually
                // clicked?  When submitted, it sets its elementId into a hidden field

                String value = cycle.getRequestContext().getParameter("_linkSubmit");

                // If the value isn't the elementId of this component, then this link wasn't
                // selected.

                if (value != null && value.equals(name))
                {
                    IBinding selectedBinding = getSelectedBinding();
                    if (selectedBinding != null)
                        selectedBinding.setObject(getTag());
                    IActionListener listener = getListener();
                    if (listener != null)
                        listener.actionTriggered(this, cycle);
                }
            }

            writer.begin("a");
            writer.attribute(
                "href",
                "javascript:submitLink(document." + formName + ",\"" + name + "\");");

            // Allow the wrapped components a chance to render.
            // Along the way, they may interact with this component
            // and cause the name variable to get set.

            wrappedWriter = writer.getNestedWriter();
        }
        else
            wrappedWriter = writer;

        renderBody(wrappedWriter, cycle);

        if (!disabled)
        {
            // Generate additional attributes from informal parameters.

            renderInformalParameters(writer, cycle);

            // Dump in HTML provided by wrapped components

            wrappedWriter.close();

            // Close the <a> tag

            writer.end();
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    public abstract boolean isDisabled();

    public abstract void setDisabled(boolean disabled);

    public abstract IActionListener getListener();

    public abstract void setListener(IActionListener listener);

    public abstract Object getTag();

    public abstract void setTag(Object tag);

    public abstract void setSelectedBinding(IBinding value);

    public abstract IBinding getSelectedBinding();

}
