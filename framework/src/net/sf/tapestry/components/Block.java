package net.sf.tapestry.components;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/** 
 *  Prevents its contents from being rendered until triggered by
 *  an {@link RenderBlock} component.
 *
 *  [<a href="../../../../../ComponentReference/Block.html">Component Reference</a>]
 *
 *  <p>Block and {@link RenderBlock} are used to build a certain class
 *  of complicated component that can't be assembled using the normal
 *  wrapping containment.  Such a super component would have two or more
 *  sections that need to be supplied by the containing page (or component).
 *
 *  <p>Using Blocks, the blocks can be provided as parameters to the super
 *  component.
 * 
 *  <p>The inserter property gives the components inside the block access to
 *  the component (typically an {@link RenderBlock}) that inserted the block,
 *  including access to its informal bindings which allows components contained
 *  by the Block to be passed parameters.  Note - it is the responsibility of the
 *  inserting component to set itself as the Block's inserter.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 * 
 **/

public class Block extends AbstractComponent
{
	private IComponent _inserter;

    /**
     *  Does nothing; the idea of a Block is to defer the rendering of
     *  the body of the block until an {@link RenderBlock} forces it
     *  out.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        // Nothing!
    }
    
    public IComponent getInserter()
    {
    	return _inserter;
    }
    
    public void setInserter(IComponent value)
    {
    	_inserter = value;
    }
}