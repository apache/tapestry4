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

package org.apache.tapestry.form;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;

/**
 *  A common interface implemented by all form components (components that
 *  create interactive elements in the rendered page).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IFormComponent extends IComponent
{
    /**
     *  Returns the {@link org.apache.tapestry.IForm} which contains the component,
     *  or null if the component is not contained by a form,
     *  of if the containing Form is not currently renderring.
     * 
     **/

    public IForm getForm();

    /**
     *  Returns the name of the component, which is automatically generated
     *  during renderring.
     *
     *  <p>This value is set inside the component's render method and is
     *  <em>not</em> cleared.  If the component is inside a {@link org.apache.tapestry.components.Foreach}, the
     *  value returned is the most recent name generated for the component.
     *
     *  <p>This property is made available to facilitate writing JavaScript that
     *  allows components (in the client web browser) to interact.
     *
     *  <p>In practice, a {@link org.apache.tapestry.html.Script} component
     *  works with the {@link org.apache.tapestry.html.Body} component to get the
     *  JavaScript code inserted and referenced.
     *
     **/

    public String getName();
    
    /**
     *  Invoked by {@link IForm#getElementId(IComponent)} when a name is created
     *  for a form component.
     * 
     *  @since 3.0
     * 
     **/
    
    public void setName(String name);

    /**
     *  May be implemented to return a user-presentable, localized name for the component,
     *  which is used in labels or error messages.  Most components simply return null.
     * 
     *  @since 1.0.9
     * 
     **/

    public String getDisplayName();
    
    /**
     *  Returns true if the component is disabled.  This is important when the containing
     *  form is submitted, since disabled parameters do not update their bindings.
     * 
     *  @since 2.2
     * 
     **/
    
    public boolean isDisabled();
}