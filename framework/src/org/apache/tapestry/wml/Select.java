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

import org.apache.tapestry.*;

/**
 *  The Select element lets users pick from a list of options. Each option
 *  is specified by an Option element. Each Option element may have one
 *  line of formatted text (which may be wrapped or truncated by the user
 *  agent if too long).
 *
 *  Unless multiple selections are required it is generally easier to use the {@link PropertySelection} component.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 2.4
 *
 **/
public abstract class Select extends AbstractComponent
{
    /**
     *  Used by the <code>Select</code> to record itself as a
     *  {@link IRequestCycle} attribute, so that the
     *  {@link Option} components it wraps can have access to it.
     *
     **/

    private final static String ATTRIBUTE_NAME = "org.apache.tapestry.active.Select";

    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
	{
		if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
			throw new ApplicationRuntimeException(Tapestry.getString("Select.may-not-nest"), this);

		cycle.setAttribute(ATTRIBUTE_NAME, this);

		boolean render = !cycle.isRewinding();

		if (render) {
			writer.begin("select");

			writer.attribute("name", getName());

            String value = getValue();
			if (value != null)
				writer.attribute("value", value);

            String title = getTitle();
			if (title != null)
				writer.attribute("title", title);

			boolean multiple = isMultiple();
            if (multiple)
				writer.attribute("multiple", multiple);

			generateAttributes(writer, cycle);
		}
	
        renderBody(writer, cycle);

        if (render)
	    {
            writer.end();
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
	}

    public abstract boolean isMultiple();

	public abstract String getName();

	public abstract String getValue();

	public abstract String getTitle();
}
