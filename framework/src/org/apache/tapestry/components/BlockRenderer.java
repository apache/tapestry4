/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * An implementation of IRender that renders a Block component.
 * 
 * <p>The BlockRenderer allows the contents of a {@link Block} to be rendered
 * via {@link IRender}. It can be used in cases when an {@link IRender} object is
 * required as an argument or a binding to render a part of a Component. 
 * To provide a complicated view, it could be defined in a {@link Block} and then
 * returned encapsulated in a BlockRenderer.
 * 
 * <p>It is important to note that a special care has to be taken if 
 * the BlockRenderer is used within an inner class of a component or a page. 
 * In such a case the instance of the component that created the inner class 
 * may not be the currently active instance in the RequestCycle when the 
 * BlockRenderer is required. Thus, calling getComponent("blockName") to get the
 * block component may return a Block component that is not initialized for this 
 * RequestCycle.
 * 
 * <p>To avoid similar problems, the ComponentAddress class could be used in
 * conjunction with BlockRenderer. 
 * Here is a quick example of how BlockRenderer could be used with ComponentAddress:
 * <p>
 * <code>
 * <br>// Create a component address for the current component
 * <br>final ComponentAddress address = new ComponentAddress(this);
 * <br>return new SomeClass() {
 * <br>&nbsp;&nbsp;IRender getRenderer(IRequestCycle cycle) {
 * <br>&nbsp;&nbsp;&nbsp;&nbsp;MyComponent component = (MyComponent) address.findComponent(cycle);
 * <br>&nbsp;&nbsp;&nbsp;&nbsp;// initialize variables in the component that will be used by the block here
 * <br>&nbsp;&nbsp;&nbsp;&nbsp;return new BlockRenderer(component.getComponent("block"));
 * <br>&nbsp;&nbsp;}
 * <br>}
 * </code>
 * 
 * @version $Id$
 * @author mindbridge
 * @since 2.2
 */
public class BlockRenderer implements IRender
{
	private Block m_objBlock;

	/**
	 * Creates a new BlockRenderer that will render the content of the argument
	 * @param objBlock the Block to be rendered
	 */
	public BlockRenderer(Block objBlock)
	{
		m_objBlock = objBlock;
	}

	/**
	 * @see org.apache.tapestry.IRender#render(IMarkupWriter, IRequestCycle)
	 */
	public void render(IMarkupWriter writer, IRequestCycle cycle)
	{
		m_objBlock.renderBody(writer, cycle);
	}

}
