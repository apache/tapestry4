//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.valid;

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
    private ValidationConstraint constraint;
    private String invalidInput;

    public ValidatorException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     *  Creates a new instance.
     *  @param errorMessage the default error message to be used (this may be
     *  overriden by the {@link IValidationDelegate}
     *  @param constraint a validation constraint that has been compromised, or
     *  null if no constraint is applicable
     *  @param invalidInput the input received by the component that was invalid; this
     *  is stored during the request cycle and will be used as the default value
     *  for the input field during the page render
     **/

    public ValidatorException(
        String errorMessage,
        ValidationConstraint constraint,
        String invalidInput)
    {
        super(errorMessage);

        this.constraint = constraint;
        this.invalidInput = invalidInput;
    }

    public ValidationConstraint getConstraint()
    {
        return constraint;
    }

    public String getInvalidInput()
    {
        return invalidInput;
    }
}