/*
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Implementation of {@link IValidationDelegate} that
 *  works with pages that implements {@link IErrorProperty}.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.valid.*;

public class SimpleValidationDelegate
extends BaseValidationDelegate
{
    IErrorProperty page;

    public SimpleValidationDelegate(IErrorProperty page)
    {
        this.page = page;
    }

    /**
     *  Checks to see if the page already has an error; if not,
     *  invokes <code>setError()</code> to update it.
     *
     */

    public void invalidField(IValidatingTextField field, 
        ValidationConstraint constraint,
        String defaultErrorMessage)
    {
        if (page.getError() != null)
            return;

        page.setError(defaultErrorMessage);
    }


}