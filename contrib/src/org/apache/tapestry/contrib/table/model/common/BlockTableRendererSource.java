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

package org.apache.tapestry.contrib.table.model.common;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.BlockRenderer;
import org.apache.tapestry.util.ComponentAddress;

import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;

/**
 * 
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class BlockTableRendererSource implements ITableRendererSource
{
	private ComponentAddress m_objBlockAddress;
	private ComponentAddress m_objListenerAddress;

	public BlockTableRendererSource(Block objBlock)
	{
		this(new ComponentAddress(objBlock));
	}

	public BlockTableRendererSource(
		Block objBlock,
		ITableRendererListener objListener)
	{
		this(new ComponentAddress(objBlock), new ComponentAddress(objListener));
	}

	public BlockTableRendererSource(ComponentAddress objBlockAddress)
	{
		this(objBlockAddress, null);
	}

	public BlockTableRendererSource(
		ComponentAddress objBlockAddress,
		ComponentAddress objListenerAddress)
	{
		setBlockAddress(objBlockAddress);
		setListenerAddress(objListenerAddress);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableRendererSource#getRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
	 */
	public IRender getRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow)
	{
		ComponentAddress objListenerAddress = getListenerAddress();
		if (objListenerAddress != null)
		{
			ITableRendererListener objListener =
				(ITableRendererListener) objListenerAddress.findComponent(
					objCycle);
			objListener.initializeRenderer(
				objCycle,
				objSource,
				objColumn,
				objRow);
		}

		Block objBlock = (Block) getBlockAddress().findComponent(objCycle);
		return new BlockRenderer(objBlock);
	}

	/**
	 * Returns the blockAddress.
	 * @return ComponentAddress
	 */
	public ComponentAddress getBlockAddress()
	{
		return m_objBlockAddress;
	}

	/**
	 * Sets the blockAddress.
	 * @param blockAddress The blockAddress to set
	 */
	public void setBlockAddress(ComponentAddress blockAddress)
	{
		m_objBlockAddress = blockAddress;
	}

	/**
	 * Returns the listenerAddress.
	 * @return ComponentAddress
	 */
	public ComponentAddress getListenerAddress()
	{
		return m_objListenerAddress;
	}

	/**
	 * Sets the listenerAddress.
	 * @param listenerAddress The listenerAddress to set
	 */
	public void setListenerAddress(ComponentAddress listenerAddress)
	{
		m_objListenerAddress = listenerAddress;
	}

}
