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
 *  Default implementation of {@link IFieldTracking}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class FieldTracking implements IFieldTracking
{
    private IFormComponent _component;
    private String _input;
    private IRender _renderer;
    private String _fieldName;
    private ValidationConstraint _constraint;

	/**
	 *  Constructor used for unassociated errors; errors that are not about any particular
	 *  field within the form.
	 * 
	 **/
	
    FieldTracking()
    {
    }

	/**
	 *  Standard constructor for a field (with the given name), rendered
	 *  by the specified component.
	 * 
	 **/
	
    FieldTracking(String fieldName, IFormComponent component)
    {
        _fieldName = fieldName;
        _component = component;
    }

    public IFormComponent getComponent()
    {
        return _component;
    }

    public IRender getErrorRenderer()
    {
        return _renderer;
    }

    public void setErrorRenderer(IRender value)
    {
        _renderer = value;
    }

    public String getInput()
    {
        return _input;
    }

    public void setInput(String value)
    {
        _input = value;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    public ValidationConstraint getConstraint()
    {
        return _constraint;
    }

    public void setConstraint(ValidationConstraint constraint)
    {
        _constraint = constraint;
    }

    /** @since 2.4 **/

    public boolean isInError()
    {
        return _renderer != null;
    }

}