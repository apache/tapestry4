package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Renders the text and components wrapped by a component.
 *
 *  [<a href="../../../../../ComponentReference/RenderBody.html">Component Reference</a>]
 *
 *  <p>Prior to release 2.2, this component was named <b>RenderBody</b>.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class RenderBody extends AbstractComponent
{
    /**
     *  Finds this <code>RenderBody</code>'s container, and invokes
     *  {@link IComponent#renderWrapped(IMarkupWriter, IRequestCycle)}
     *  on it.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IComponent container = getContainer();

        container.renderBody(writer, cycle);
    }
}