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

package org.apache.tapestry.contrib.informal;

import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * 
 *  A version of the Any component that inherits the informal attributes of its parent.
 *  This component has been deprecated in favour of the 'inherit-informal-parameters' 
 *  tag that indicates that a particular component must inherit the informal parameters
 *  of its parent. This tag is available in the page or component specification file.
 * 
 *  @deprecated
 *  @version $Id$
 *  @author mindbridge
 *  @since 2.2
 * 
 **/

public class InheritInformalAny extends AbstractComponent
{
    // Bindings
    private IBinding m_objElementBinding;

    public IBinding getElementBinding()
    {
        return m_objElementBinding;
    }

    public void setElementBinding(IBinding objElementBinding)
    {
        m_objElementBinding = objElementBinding;
    }

    protected void generateParentAttributes(IMarkupWriter writer, IRequestCycle cycle)
    {
        String attribute;

        IComponent objParent = getContainer();
        if (objParent == null)
            return;

        IComponentSpecification specification = objParent.getSpecification();
        Map bindings = objParent.getBindings();
        if (bindings == null)
            return;

        Iterator i = bindings.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();

            // Skip over formal parameters stored in the bindings
            // Map.  We're just interested in informal parameters.

            if (specification.getParameter(name) != null)
                continue;

            IBinding binding = (IBinding) entry.getValue();

            Object value = binding.getObject();
            if (value == null)
                continue;

            if (value instanceof IAsset)
            {
                IAsset asset = (IAsset) value;

                // Get the URL of the asset and insert that.
                attribute = asset.buildURL(cycle);
            }
            else
                attribute = value.toString();

            writer.attribute(name, attribute);
        }

    }

    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String strElement = m_objElementBinding.getObject().toString();

        writer.begin(strElement);
        generateParentAttributes(writer, cycle);
        renderInformalParameters(writer, cycle);

        renderBody(writer, cycle);

        writer.end();
    }

}
