package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  A component that can substitute for any HTML element.  
 *
 *  [<a href="../../../../../ComponentReference/Any.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Any extends AbstractComponent
{
    private String _element;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (!cycle.isRewinding())
        {
            writer.begin(_element);

            generateAttributes(writer, cycle);
        }

        renderBody(writer, cycle);

        if (!cycle.isRewinding())
        {
            writer.end(_element);
        }

    }

    public String getElement()
    {
        return _element;
    }

    public void setElement(String element)
    {
        _element = element;
    }

}