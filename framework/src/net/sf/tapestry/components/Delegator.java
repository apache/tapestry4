package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  A component which delegates it's behavior to another object.
 *
 *  [<a href="../../../../../ComponentReference/Delegator.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Delegator extends AbstractComponent
{
	private IRender _delegate;

    /**
     *  Gets its delegate and invokes {@link IRender#render(IMarkupWriter, IRequestCycle)}
     *  on it.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (_delegate != null)
        	_delegate.render(writer, cycle);
    }
    
    public IRender getDelegate()
    {
        return _delegate;
    }

    public void setDelegate(IRender delegate)
    {
        _delegate = delegate;
    }

}