package com.primix.tapestry.components.validating;

import com.primix.foundation.*;

/**
 *  Defines an enumeration of different types of validation constraints
 *  that may be violated.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class ValidationConstraint
extends Enum
{
    /**
     *  Indicates that no value (or a value consisting only of white space) was
     *  provided for a field that requires a non-null value.
     *
     */

    public static final ValidationConstraint REQUIRED = 
        new ValidationConstraint("REQUIRED");
    
    /**
     *  Indicates that a non-null value was provided, but that (after removing
     *  leading and trailing whitespace), the value was not long enough.
     *
     */

    public static final ValidationConstraint MINIMUM_WIDTH =
        new ValidationConstraint("MINUMUM_WIDTH");

    /**
     *  Indicates a general error in converting a String into a Date.
     *
     */

    public static final ValidationConstraint DATE_FORMAT =
        new ValidationConstraint("DATE_FORMAT");

    /**
     *  Indicates a general error in the format of a string that is
     *  to be interpreted as a number.
     *
     */

    public static final ValidationConstraint NUMBER_FORMAT = 
        new ValidationConstraint("NUMBER_FORMAT");

    /**
     *  Indicates that the value was too small (for a Date, too early).
     *
     */

    public static final ValidationConstraint TOO_SMALL =
        new ValidationConstraint("TOO_SMALL");

    /**
     *  Indicates that the value was too large (for a Date, too late).
     *
     */

    public static final ValidationConstraint TOO_LARGE =
        new ValidationConstraint("TOO_LARGE");

    private ValidationConstraint(String enumerationId)
    {
        super(enumerationId);
    }

    private Object readResolve()
    {
        return getSingleton();
    }


}

