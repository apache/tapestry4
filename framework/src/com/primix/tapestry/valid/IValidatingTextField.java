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

package com.primix.tapestry.valid;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;

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
     *  Forces the component to re-read through its value binding (whose
	 *  name and type is dependant on the implementation).  Normally
     *  this only occurs once per request cycle.  This does not clear 
	 *  the error property.
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