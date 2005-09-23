package org.apache.tapestry.annotations;

import org.apache.tapestry.html.BasePage;

/**
 * Used by {@link org.apache.tapestry.annotations.MetaAnnotationWorkerTest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Meta(
{ "foo=bar", "biff=bazz" })
public abstract class MetaPage extends BasePage
{

}
