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

import java.io.Serializable;
import java.util.Comparator;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.*;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.valid.RenderString;

/**
 * A base implementation of {@link org.apache.tapestry.contrib.table.model.ITableColumn}
 * that allows renderers to be set via aggregation.
 * 
 * @see org.apache.tapestry.contrib.table.model.ITableRendererSource
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class AbstractTableColumn implements ITableColumn, Serializable
{
	private String m_strColumnName;
	private boolean m_bSortable;
	private Comparator m_objComparator;

	private ITableRendererSource m_objColumnRendererSource;
	private ITableRendererSource m_objValueRendererSource;

	public AbstractTableColumn()
	{
		this("", false, null);
	}

	public AbstractTableColumn(
		String strColumnName,
		boolean bSortable,
		Comparator objComparator)
	{
		this(strColumnName, bSortable, objComparator, null, null);
	}

	public AbstractTableColumn(
		String strColumnName,
		boolean bSortable,
		Comparator objComparator,
		ITableRendererSource objColumnRendererSource,
		ITableRendererSource objValueRendererSource)
	{
		setColumnName(strColumnName);
		setSortable(bSortable);
		setComparator(objComparator);
		setColumnRendererSource(objColumnRendererSource);
		setValueRendererSource(objValueRendererSource);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableColumn#getColumnName()
	 */
	public String getColumnName()
	{
		return m_strColumnName;
	}

	/**
	 * Sets the columnName.
	 * @param columnName The columnName to set
	 */
	public void setColumnName(String columnName)
	{
		m_strColumnName = columnName;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableColumn#getSortable()
	 */
	public boolean getSortable()
	{
		return m_bSortable;
	}

	/**
	 * Sets whether the column is sortable.
	 * @param sortable The sortable flag to set
	 */
	public void setSortable(boolean sortable)
	{
		m_bSortable = sortable;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableColumn#getComparator()
	 */
	public Comparator getComparator()
	{
		return m_objComparator;
	}

	/**
	 * Sets the comparator.
	 * @param comparator The comparator to set
	 */
	public void setComparator(Comparator comparator)
	{
		m_objComparator = comparator;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableColumn#getColumnRenderer(IRequestCycle, ITableModelSource)
	 */
	public IRender getColumnRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource)
	{
		ITableRendererSource objRendererSource =
			getColumnRendererSource();
		if (objRendererSource == null)
		{
			// log error
			return new RenderString("");
		}

		return objRendererSource.getRenderer(objCycle, objSource, this, null);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableColumn#getValueRenderer(IRequestCycle, ITableModelSource, Object)
	 */
	public IRender getValueRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		Object objRow)
	{
		ITableRendererSource objRendererSource = getValueRendererSource();
		if (objRendererSource == null)
		{
			// log error
			return new RenderString("");
		}

		return objRendererSource.getRenderer(
			objCycle,
			objSource,
			this,
			objRow);
	}

	/**
	 * Returns the columnRendererSource.
	 * @return ITableColumnRendererSource
	 */
	public ITableRendererSource getColumnRendererSource()
	{
		return m_objColumnRendererSource;
	}

	/**
	 * Sets the columnRendererSource.
	 * @param columnRendererSource The columnRendererSource to set
	 */
	public void setColumnRendererSource(ITableRendererSource columnRendererSource)
	{
		m_objColumnRendererSource = columnRendererSource;
	}

	/**
	 * Returns the valueRendererSource.
	 * @return ITableValueRendererSource
	 */
	public ITableRendererSource getValueRendererSource()
	{
		return m_objValueRendererSource;
	}

	/**
	 * Sets the valueRendererSource.
	 * @param valueRendererSource The valueRendererSource to set
	 */
	public void setValueRendererSource(ITableRendererSource valueRendererSource)
	{
		m_objValueRendererSource = valueRendererSource;
	}

}
