package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  A conditional element on a page which will render its wrapped elements
 *  zero or one times.
 *
 *  [<a href="../../../../../ComponentReference/Conditional.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Conditional extends AbstractComponent
{
	private boolean _condition;
	private boolean _invert;
	
    /**
     *  Renders its wrapped components only if the condition is true (technically,
     *  if condition matches invert).
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
      if (_condition != _invert)
            renderBody(writer, cycle);
    }
    
    public boolean getCondition()
    {
        return _condition;
    }

    public void setCondition(boolean condition)
    {
        _condition = condition;
    }

    public boolean getInvert()
    {
        return _invert;
    }

    public void setInvert(boolean invert)
    {
        _invert = invert;
    }

}