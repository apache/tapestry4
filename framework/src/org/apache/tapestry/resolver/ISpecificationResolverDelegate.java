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

package org.apache.tapestry.resolver;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Delegate interface used when a page or component specification
 *  can not be found by the normal means.  This allows hooks
 *  to support specifications from unusual locations, or generated
 *  on the fly.
 * 
 *  <p>The delegate must be coded in a threadsafe manner.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public interface ISpecificationResolverDelegate
{
    /**
     *  Invoked by {@link PageSpecificationResolver} to find the indicated
     *  page specification.  Returns
     *  the specification, or null.  The specification, if returned, is not cached by Tapestry
     *  (it is up to the delegate to cache the specification if desired).
     * 
     *  @parameter cycle used to gain access to framework and Servlet API objects
     *  @parameter namespace the namespace containing the page
     *  @parameter simplePageName the name of the page (without any namespace prefix)
     * 
     **/

    public IComponentSpecification findPageSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String simplePageName);

    /**
     *  Invoked by {@link PageSpecificationResolver} to find the indicated
     *  component specification.  Returns
     *  the specification, or null.  The specification, if returned, is not cached by Tapestry
     *  (it is up to the delegate to cache the specification if desired).
     * 
     *  <p>The delegate must be coded in a threadsafe manner.
     * 
     *  @parameter cycle used to gain access to framework and Servlet API objects
     *  @parameter namespace the namespace containing the component
     *  @parameter type the component type (without any namespace prefix)
     * 
     **/

    public IComponentSpecification findComponentSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String type);
}
