package org.apache.tapestry.workbench.fields;

import org.apache.tapestry.html.BasePage;

/**
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class FieldsResults extends BasePage
{
    /**
     * Flag indicating whether the form was submitted via a link.
     */

    public abstract void setByLink(boolean byLink);
}
