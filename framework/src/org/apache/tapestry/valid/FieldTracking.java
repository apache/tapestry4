//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.valid;

import org.apache.tapestry.IRender;
import org.apache.tapestry.form.IFormComponent;

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

    /** @since 3.0 **/

    public boolean isInError()
    {
        return _renderer != null;
    }

}