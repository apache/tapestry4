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

/**
 *  Thrown by a {@link IValidator} when submitted input is not valid.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class ValidatorException extends Exception
{
    private IRender _errorRenderer;
    private ValidationConstraint _constraint;

    public ValidatorException(String errorMessage)
    {
        this(errorMessage, null, null);
    }

    public ValidatorException(String errorMessage, ValidationConstraint constraint)
    {
        this(errorMessage, null, constraint);
    }

    /**
     *  Creates a new instance.
     *  @param errorMessage the default error message to be used (this may be
     *  overriden by the {@link IValidationDelegate})
     *  @param errorRenderer to use to render the error message (may be null)
     *  @param constraint a validation constraint that has been compromised, or
     *  null if no constraint is applicable
     * 
     **/

    public ValidatorException(
        String errorMessage,
        IRender errorRenderer,
        ValidationConstraint constraint)
    {
        super(errorMessage);

        _errorRenderer = errorRenderer;
        _constraint = constraint;
    }

    public ValidationConstraint getConstraint()
    {
        return _constraint;
    }

    /** @since 3.0 **/

    public IRender getErrorRenderer()
    {
        return _errorRenderer;
    }
}