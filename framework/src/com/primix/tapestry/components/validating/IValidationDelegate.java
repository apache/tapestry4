package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;

/**
 *  Interface used to communicate errors from a {@link ValidatingTextField} (or 
 *  similar) component to some application-specific listener.
 *
 *  <p>The interface is designed so that a single instance can be shared
 *  with many instances of {@link ValidatingTextField}.
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
     *  displayName to form their own error message.
     *
     *  @param component the {@link ValidatingTextField} which the update applies to.
     *  @param constraint the {@link ValidationConstraint} which was violated.
     *  @param displayName a user presentable name/label for the field.
     *  @param defaultErrorMessage a default, localized, error message.
     *
     */

    public void invalidField(IValidatingTextField field, 
        ValidationConstraint constraint,
        String defaultErrorMessage);

    /**
     *  Invoked before the text field is rendered, if the field is in error.
     *
     */

    public void writeErrorPrefix(IValidatingTextField field,
                IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException;

    /**
     *  Invoked after the text field is rendered, if the field is in error.
     *
     */

    public void writeErrorSuffix(IValidatingTextField field,
                IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException;
            

}

