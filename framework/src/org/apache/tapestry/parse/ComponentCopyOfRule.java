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

package org.apache.tapestry.parse;

import java.util.Iterator;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  A rule for processing the copy-of attribute
 *  of the &lt;component&gt; element (in a page
 *  or component specification).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ComponentCopyOfRule extends AbstractSpecificationRule
{
    /**
     *  Validates that the element has either type or copy-of (not both, not neither).
     *  Uses the copy-of attribute to find a previously declared component
     *  and copies its type and bindings into the new component (on top of the stack).
     * 
     **/

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String id = getValue(attributes, "id");
        String copyOf = getValue(attributes, "copy-of");
        String type = getValue(attributes, "type");

        if (Tapestry.isNull(copyOf))
        {
            if (Tapestry.isNull(type))
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.missing-type-or-copy-of", id),
                    getResourceLocation());

            return;
        }

        if (!Tapestry.isNull(type))
            throw new DocumentParseException(
                Tapestry.format("SpecificationParser.both-type-and-copy-of", id),
                getResourceLocation());

        IComponentSpecification spec = (IComponentSpecification) digester.getRoot();

        IContainedComponent source = spec.getComponent(copyOf);
        if (source == null)
            throw new DocumentParseException(
                Tapestry.format("SpecificationParser.unable-to-copy", copyOf),
                getResourceLocation());

        IContainedComponent target = (IContainedComponent) digester.peek();

        target.setType(source.getType());
        target.setCopyOf(copyOf);

        Iterator i = source.getBindingNames().iterator();
        while (i.hasNext())
        {
            String bindingName = (String) i.next();
            IBindingSpecification binding = source.getBinding(bindingName);
            target.setBinding(bindingName, binding);
        }
    }

}
