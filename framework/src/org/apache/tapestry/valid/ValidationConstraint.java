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

import org.apache.commons.lang.enum.Enum;

/**
 *  Defines an enumeration of different types of validation constraints
 *  that may be violated.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ValidationConstraint extends Enum
{
    /**
     *  Indicates that no value (or a value consisting only of white space) was
     *  provided for a field that requires a non-null value.
     *
     **/

    public static final ValidationConstraint REQUIRED = new ValidationConstraint("REQUIRED");

    /**
     *  Indicates that a non-null value was provided, but that (after removing
     *  leading and trailing whitespace), the value was not long enough.
     *
     **/

    public static final ValidationConstraint MINIMUM_WIDTH =
        new ValidationConstraint("MINUMUM_WIDTH");

    /**
     *  Indicates a general error in converting a String into a Date.
     *
     **/

    public static final ValidationConstraint DATE_FORMAT = new ValidationConstraint("DATE_FORMAT");

    /**
     *  Indicates a general error in the format of a string that is
     *  to be interpreted as a email.
     *
     **/

    public static final ValidationConstraint EMAIL_FORMAT =
        new ValidationConstraint("EMAIL_FORMAT");

    /**
     *  Indicates a general error in the format of a string that is
     *  to be interpreted as a number.
     *
     **/

    public static final ValidationConstraint NUMBER_FORMAT =
        new ValidationConstraint("NUMBER_FORMAT");

    /**
     *  Indicates that the value was too small (for a Date, too early).
     *
     **/

    public static final ValidationConstraint TOO_SMALL = new ValidationConstraint("TOO_SMALL");

    /**
     *  Indicates that the value was too large (for a Date, too late).
     *
     **/

    public static final ValidationConstraint TOO_LARGE = new ValidationConstraint("TOO_LARGE");

    /**
     *  Indicates an error in a string that does not fulfill a pattern.
     * 
     *  @since 3.0
     * 
     **/

    public static final ValidationConstraint PATTERN_MISMATCH =
        new ValidationConstraint("PATTERN_MISMATCH");

    /**
     *  Indicates a consistency error, usually between too different fields.
     * 
     *  @since 3.0
     * 
     **/

    public static final ValidationConstraint CONSISTENCY = new ValidationConstraint("CONSISTENCY");

	/**
	 *  Indicates that a URL is not of the correct format
	 * 
	 * @since 3.0
	 */
	
	public static final ValidationConstraint URL_FORMAT = new ValidationConstraint("URL_FORMAT");

	/**
	 *  Indicates that the URL does not use one of the specified protocols
	 * 
	 * @since 3.0
	 */

	public static final ValidationConstraint DISALLOWED_PROTOCOL = new ValidationConstraint("DISALLOWED_PROTOCOL");



	/**
	 *  Protected constructor, which allows new constraints to be created
	 *  as subclasses.
	 * 
	 **/

    protected ValidationConstraint(String name)
    {
        super(name);
    }

}