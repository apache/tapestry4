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
     *  Indicates a consistency error, usually between too different fields.
     * 
     *  @since 3.0
     * 
     **/

    public static final ValidationConstraint CONSISTENCY = new ValidationConstraint("CONSISTENCY");

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