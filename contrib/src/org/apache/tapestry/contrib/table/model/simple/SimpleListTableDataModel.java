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

package org.apache.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry.contrib.table.model.CTableDataModelEvent;
import org.apache.tapestry.contrib.table.model.common.AbstractTableDataModel;
import org.apache.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal list implementation of the 
 * {@link org.apache.tapestry.contrib.table.model.ITableDataModel} interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleListTableDataModel extends AbstractTableDataModel implements Serializable
{
	private List m_arrRows;

	public SimpleListTableDataModel(Object[] arrRows)
	{
		this(Arrays.asList(arrRows));
	}

	public SimpleListTableDataModel(List arrRows)
	{
		m_arrRows = arrRows;
	}

    public SimpleListTableDataModel(Collection arrRows)
    {
        m_arrRows = new ArrayList(arrRows);
    }

    public SimpleListTableDataModel(Iterator objRows)
    {
        m_arrRows = new ArrayList();
        CollectionUtils.addAll(m_arrRows, objRows);
    }

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableDataModel#getRowCount()
	 */
	public int getRowCount()
	{
		return m_arrRows.size();
	}

	/**
	 * Returns the row element at the given position
     * @param nRow the index of the element to return
	 */
	public Object getRow(int nRow)
	{
		if (nRow < 0 || nRow >= m_arrRows.size())
		{
			// error message
			return null;
		}
		return m_arrRows.get(nRow);
	}

	/**
	 * Returns an Iterator with the elements from the given range
     * @param nFrom the start of the range (inclusive)
     * @param nTo the stop of the range (exclusive)
	 */
	public Iterator getRows(int nFrom, int nTo)
	{
		return new ArrayIterator(m_arrRows.toArray(), nFrom, nTo);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableDataModel#getRows()
	 */
	public Iterator getRows()
	{
		return m_arrRows.iterator();
	}

	/**
	 * Method addRow.
     * Adds a row object to the model at its end
	 * @param objRow the row object to add
	 */
	public void addRow(Object objRow)
	{
		m_arrRows.add(objRow);

		CTableDataModelEvent objEvent = new CTableDataModelEvent();
		fireTableDataModelEvent(objEvent);
	}

    public void addRows(Collection arrRows)
    {
        m_arrRows.addAll(arrRows);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

	/**
	 * Method removeRow.
     * Removes a row object from the model
	 * @param objRow the row object to remove
	 */
	public void removeRow(Object objRow)
	{
		m_arrRows.remove(objRow);

		CTableDataModelEvent objEvent = new CTableDataModelEvent();
		fireTableDataModelEvent(objEvent);
	}

    public void removeRows(Collection arrRows)
    {
        m_arrRows.removeAll(arrRows);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

}
