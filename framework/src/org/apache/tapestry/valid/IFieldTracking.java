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

package org.apache.tapestry.valid;

import org.apache.tapestry.IRender;
import org.apache.tapestry.form.IFormComponent;

/**
 *  Defines the interface for an object that tracks input fields.  This 
 *  interface is now poorly named, in that it tracks errors that may <em>not</em>
 *  be associated with a specific field.
 * 
 *  <p>For each field, a flag is stored indicating if the field is, in fact, in error.
 *  The input supplied by the client is stored so that if the form is re-rendered
 *  (as is typically done when there are input errors), the value entered by the user
 *  is displayed back to the user.  An error message renderer is stored; this is
 *  an object that can render the error message (it is usually
 *  a {@link org.apache.tapestry.valid.RenderString} wrapper around a simple string).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public interface IFieldTracking
{
	/**
	 *  Returns true if the field is in error (that is,
	 *  if it has an error message {@link #getRenderer() renderer}.
	 * 
	 **/
	
	public boolean isInError();
	
	
    /**
     *  Returns the field component.  This may return null if the error
     *  is not associated with any particular field.
     * 
     **/

    public IFormComponent getComponent();

    /**
     *  Returns an object that will render the error message.
     * 
     *  @since 1.0.9
     * 
     **/

    public IRender getErrorRenderer();


    /**
     *  Returns the invalid input recorded for the field.  This is stored
     *  so that, on a subsequent render, the smae invalid input can be presented
     *  to the client to be corrected.
     * 
     **/

    public String getInput();

    /**
     *  Returns the name of the field, that is, the name assigned by the form
     *  (this will differ from the component's id when any kind of looping operation
     *  is in effect).
     * 
     **/

    public String getFieldName();

    /**
     *  Returns the validation constraint that was violated by the input.  This
     *  may be null if the constraint isn't known.
     * 
     **/

    public ValidationConstraint getConstraint();
}