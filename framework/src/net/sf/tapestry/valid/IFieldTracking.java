/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.valid;

import net.sf.tapestry.IRender;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Defines the interface for an object that tracks validation errors.  This 
 *  interface is now poorly named, in that it tracks errors that may <em>not</em>
 *  be associated with a specific field.
 * 
 *  <p>The initial release (1.0.8) stored an error <em>message</em>.  Starting in
 *  release 1.0.9, this was changed to an error <em>renderrer</em>.  This increases
 *  the complexity slightly, but allows for much, much greater flexibility in how
 *  errors are ultimately presented to the user.  For example, you could devote part
 *  of a template to a {@link net.sf.tapestry.components.Block} 
 *  that contained a detail error message and links
 *  to other parts of the application (for example, perhaps a pop-up help message).
 * 
 *  <p>However, in most cases, the renderrer will simply be a wrapper 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public interface IFieldTracking
{
    /**
     *  Returns the field component.  This may return null if the error
     *  is not associated with any particular field.
     * 
     **/

    public IFormComponent getFormComponent();

    /**
     *  Returns an object that will render the error message.
     * 
     *  @since 1.0.9
     * 
     **/

    public IRender getRenderer();

    /**
     *  Sets the error renderrer, the object that will render the error message.
     *  Typically, this is just a {@link RenderString}, but it could be a component
     *  or virtually anything.
     * 
     *  @since 1.0.9
     * 
     **/

    public void setRenderer(IRender value);

    /**
     *  Returns the invalid input recorded for the field.  This is stored
     *  so that, on a subsequent render, the smae invalid input can be presented
     *  to the client to be corrected.
     * 
     **/

    public String getInvalidInput();

    public void setInvalidInput(String value);

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

    public void setConstraint(ValidationConstraint value);
}