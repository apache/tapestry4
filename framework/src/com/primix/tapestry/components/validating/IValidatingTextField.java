package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

/**
 *  Interface for a number of components that act as a normal
 *  {@link TextField} component, but perform extra validation.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public interface IValidatingTextField extends IComponent
{
    /**
     *  Returns true if entering a non-null value into the field
     *  is required.
     *
     */

    public boolean isRequired();

    /**
     *  Forces the component to re-read through its text binding.  Normally
     *  this only occurs once per request cycle.  This clears the text property,
     *  but not the error property.
     *
     */

    public void refresh();

    /**
     *  Returns true if an error has been detected in this component.
     *
     */

    public boolean getError();

    /**
     *  Used to force an error flag on or off for this component.
     *
     */

    public void setError(boolean value);

    /**
     *  Returns the display name for the component, suitable for display
     *  to a user.
     *
     */

    public String getDisplayName();

    /**
     *  Returns the form name for the component; this is for constructing
     *  JavaScript event handlers and such.  This value won't be set until
     *  after the component renders.
     *
     */

    public String getName();


}