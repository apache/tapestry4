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

    public static final ValidationConstraint REQUIRED =
        new ValidationConstraint("REQUIRED");

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

    public static final ValidationConstraint DATE_FORMAT =
        new ValidationConstraint("DATE_FORMAT");

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

    public static final ValidationConstraint TOO_SMALL =
        new ValidationConstraint("TOO_SMALL");

    /**
     *  Indicates that the value was too large (for a Date, too late).
     *
     **/

    public static final ValidationConstraint TOO_LARGE =
        new ValidationConstraint("TOO_LARGE");

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