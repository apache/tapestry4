package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  Base implementation of the {@link IValidationDelegate} interface.
 *
 */

public class BaseValidationDelegate
implements IValidationDelegate
{
    /**
     *  Does nothing!  Subclasses will almost always want to override this
     *  to capture the defaultErrorMessage and store it where
     *  it can be displayed when the page is rendered.
     *
     */

    public void invalidField(IValidatingTextField field,
            ValidationConstraint constraint,
            String defaultErrorMessage)
	{
        // Do nothing!
	}

    /**
     *  Does nothing, rarely overriden.
     *
     */

    public void writeErrorPrefix(IValidatingTextField field,
                IResponseWriter writer, IRequestCycle cycle)
    {
        // Does nothing.
    }

    /**
     *  Writes out two red asterisks, as &lt;font color="red"&gt;**&lt;/font&gt;.
     *
     */

    public void writeErrorSuffix(IValidatingTextField field,
                IResponseWriter writer, IRequestCycle cycle)
    {
        writer.begin("font");
        writer.attribute("color", "red");
        writer.print("**");
        writer.end();
    }
}