/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Renders the text and components wrapped by a {@link Block} component.
 *
 *  [<a href="../../../../../ComponentReference/RenderBlock.html">Component Reference</a>]
 *
 *  <p>It is possible for an RenderBlock to obtain a Block
 *  from a page <em>other than</em> the render page.  This works, even when
 *  the Block contains links, forms and form components.  The action and
 *  direct services will create URLs that properly address this situation.
 *
 *  <p>However, because the rendering page can't know
 *  ahead of time about these foriegn Blocks,
 *  {@link org.apache.tapestry.event.PageRenderListener} methods
 *  (for components and objects of the foriegn page)
 *  via RenderBlock will <em>not</em> be executed.  This specifically
 *  affects the methods of the {@link org.apache.tapestry.event.PageRenderListener} 
 *  interface.
 * 
 *  <p>Before rendering its {@link Block}, RenderBlock will set itself as the 
 *  Block's inserter, and will reset the inserter after the {@link Block} is 
 *  rendered.  This gives the components contained in the {@link Block} access
 *  to its inserted environment via the RenderBlock.  In particular this allows
 *  the contained components to access the informal parameters of the RenderBlock
 *  which effectively allows parameters to be passed to the components contained
 *  in a Block.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class RenderBlock extends AbstractComponent
{
    /**
     *  If block is not null,
     *  then the block's inserter is set (to this),
     * {@link org.apache.tapestry.IComponent#renderBody(IMarkupWriter, IRequestCycle)}
     *  is invoked on it, and the Block's inserter is set back to its previous state.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    	Block block = getBlock();
    	
        if (block != null)
        {
            // make a copy of the inserter so we don't overwrite completely
            IComponent previousInserter = block.getInserter();
            block.setInserter(this);
            block.renderBody(writer, cycle);
            // reset the inserter as it was before we changed it
            block.setInserter(previousInserter);
        }
    }

    public abstract Block getBlock();

}