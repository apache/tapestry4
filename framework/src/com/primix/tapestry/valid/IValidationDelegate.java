/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Interface used to communicate errors from an {@link IValidatingTextField} 
 *  component to some application-specific code. In addition,
 *  controls how fields that are in error are presented (they can be
 *  marked in various ways by the delegate; the default implementation
 *  adds to red asterisks to the right of the field).
 *
 *  <p>The interface is designed so that a single instance can be shared
 *  with many instances of {@link IValidatingTextField}.
 *
 *  <p>TBD: Add another method so that the delegate can write additional 
 *  attributes into the text field (i.e., to change its color or class).
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

package com.primix.tapestry.valid;

import com.primix.tapestry.*;

public interface IValidationDelegate
{
    /**
     *  The error notification method, invoked during the rewind phase
     *  (that is, while HTTP parameters are being extracted from the request
     *  and assigned to various object properties).  
     *
     *  <p>Typically, the listener simply arrainges
     *  to present the defaultErrorMessage to the user (as part of the HTML
     *  response).  Finicky listeners may, instead, use the constraint and
     *  displayName (from the field) to form their own error message.
     *
     *  @param field the field to which the update applies to.
     *  @param constraint the {@link ValidationConstraint} which was violated.
     *  @param defaultErrorMessage a default, localized, error message.
     *
     */

    public void invalidField(IValidatingTextField field, 
        ValidationConstraint constraint,
        String defaultErrorMessage);

    /**
     *  Invoked before the field is rendered, if the field is in error.
     *
     */

    public void writeErrorPrefix(IValidatingTextField field,
                IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException;

    /**
     *  Invoked after the field is rendered, if the field is in error.
     *
     */

    public void writeErrorSuffix(IValidatingTextField field,
                IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException;

    /**
     *  Invoked by a {@link FieldLabel} just before writing the name
     *  of the field.
     *
     */

    public void writeLabelPrefix(IValidatingTextField field, IResponseWriter writer,
                IRequestCycle cycle)
    throws RequestCycleException;

    /**
     *  Invoked by a {@link FieldLabel} just after writing the name
     *  of the field.
     *
     */

    public void writeLabelSuffix(IValidatingTextField field, IResponseWriter writer,
                IRequestCycle cycle)
    throws RequestCycleException;
}

