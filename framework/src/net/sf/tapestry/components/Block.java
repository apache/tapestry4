//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
    	return this._inserter;
    }
    
    public void setInserter(IComponent value)
    {
    	_inserter = value;
    }
}