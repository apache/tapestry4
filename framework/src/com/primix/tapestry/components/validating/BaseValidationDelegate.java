package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;

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