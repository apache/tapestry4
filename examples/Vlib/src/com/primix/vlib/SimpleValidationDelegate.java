package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.components.html.valid.*;

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