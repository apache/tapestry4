package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;

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
}

