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

package org.apache.tapestry.spec;

import org.apache.commons.lang.enum.Enum;
import org.apache.tapestry.Tapestry;

/**
 *  Represents different types of parameters.  Currently only 
 *  in and custom are supported, but this will likely change
 *  when Tapestry supports out parameters is some form (that reflects
 *  form style processing).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/
 
 
public class Direction extends Enum
{
    /**
     *  The parameter value is input only; the component property value
     *  is unchanged or not relevant after the component renders.
     *  The property is set from the binding before the component renders,
     *  then reset to initial value after the component renders.
     * 
     **/
    
	public static final Direction IN = new Direction("IN");
	
    
    /**
     *  Encapsulates the semantics of a form component's value parameter.
     * 
     *  <p>The parameter is associated with a {@link org.apache.tapestry.form.IFormComponent}.
     *  The property value is set from the binding before the component renders (when renderring,
     *  but not when rewinding).
     *  The binding is updated from the property value
     *  after after the component renders when the
     *  <b>containing form</b> is <b>rewinding</b>, <i>and</i>
     *  the component is not {@link org.apache.tapestry.form.IFormComponent#isDisabled() disabled}.
     * 
     *  @since 2.2
     * 
     **/

    public static final Direction FORM = new Direction("FORM");    
    
	/**
	 *  Processing of the parameter is entirely the responsibility
	 *  of the component, which must obtain an manipulate
	 *  the {@link org.apache.tapestry.IBinding} (if any) for the parameter.
	 * 
	 **/
	
	public static final Direction CUSTOM = new Direction("CUSTOM");


	/**
	 *  Causes a synthetic property to be created that automatically
	 *  references and de-references the underlying binding.
	 * 
	 *  @since 3.0
	 * 
	 **/
	
	public static final Direction AUTO = new Direction("AUTO");
	
    protected Direction(String name)
    {
        super(name);
    }

    /**
     *  Returns a user-presentable name for the direction.
     * 
     **/
    
    public String getDisplayName()
    {
        return Tapestry.getMessage("Direction." + getName());
    }
}
