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

package org.apache.tapestry.pageload;

import java.util.Iterator;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.binding.ExpressionBinding;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;

/**
 *  For all parameters in the examined component that have default values, but are not bound,
 *  automatically add an ExpressionBinding with the default value.
 *   
 *  @author mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class EstablishDefaultParameterValuesVisitor implements IComponentVisitor
{
    private IResourceResolver _resolver;
    
    public EstablishDefaultParameterValuesVisitor(IResourceResolver resolver)
    {
        _resolver = resolver;
    }
    
    /**
     * @see org.apache.tapestry.pageload.IComponentVisitor#visitComponent(org.apache.tapestry.IComponent)
     */
    public void visitComponent(IComponent component)
    {
        IComponentSpecification spec = component.getSpecification();

        Iterator i = spec.getParameterNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            IParameterSpecification parameterSpec = spec.getParameter(name);

            String defaultValue = parameterSpec.getDefaultValue(); 
            if (defaultValue != null && component.getBinding(name) == null) {
                // there is no binding for this parameter. bind it to the default value
                IBinding binding = new ExpressionBinding(_resolver, component, defaultValue, parameterSpec.getLocation());
                component.setBinding(name, binding);
            }
                
        }
    }

}
