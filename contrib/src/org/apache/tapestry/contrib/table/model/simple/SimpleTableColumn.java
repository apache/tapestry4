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
import java.util.Comparator;

import org.apache.tapestry.contrib.table.model.common.AbstractTableColumn;

/**
 * A simple minimal implementation of the 
 * {@link org.apache.tapestry.contrib.table.model.ITableColumn} interface that
 * provides all the basic services for displaying a column.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableColumn extends AbstractTableColumn
{
	private String m_strDisplayName;
	private ITableColumnEvaluator m_objEvaluator;

	/**
	 * Creates a SimpleTableColumn
	 * @param strColumnName the identifying name and display name of the column
	 */
	public SimpleTableColumn(String strColumnName)
	{
		this(strColumnName, strColumnName);
	}

	/**
	 * Creates a SimpleTableColumn
	 * @param strColumnName the identifying name and display name of the column
	 * @param bSortable whether the column is sortable
	 */
	public SimpleTableColumn(String strColumnName, boolean bSortable)
	{
		this(strColumnName, strColumnName, bSortable);
	}

	/**
	 * Creates a SimpleTableColumn
	 * @param strColumnName the identifying name and display name of the column
	 * @param bSortable whether the column is sortable
     * @param objEvaluator the evaluator to extract the column value from the row
	 */
	public SimpleTableColumn(
		String strColumnName,
        ITableColumnEvaluator objEvaluator,
		boolean bSortable)
	{
		this(strColumnName, strColumnName, objEvaluator, bSortable);
	}

	/**
	 * Creates a SimpleTableColumn
	 * @param strColumnName the identifying name of the column
	 * @param strDisplayName the display name of the column
	 */
	public SimpleTableColumn(String strColumnName, String strDisplayName)
	{
		this(strColumnName, strDisplayName, false);
	}

	/**
	 * Creates a SimpleTableColumn
	 * @param strColumnName the identifying name of the column
	 * @param strDisplayName the display name of the column
	 * @param bSortable whether the column is sortable
	 */
	public SimpleTableColumn(
		String strColumnName,
		String strDisplayName,
		boolean bSortable)
	{
		this(strColumnName, strDisplayName, null, bSortable);
	}

	/**
	 * Creates a SimpleTableColumn
	 * @param strColumnName the identifying name of the column
	 * @param strDisplayName the display name of the column
	 * @param bSortable whether the column is sortable
     * @param objEvaluator the evaluator to extract the column value from the row
	 */
	public SimpleTableColumn(
		String strColumnName,
		String strDisplayName,
		ITableColumnEvaluator objEvaluator,
		boolean bSortable)
	{
		super(strColumnName, bSortable, null);
		setComparator(new DefaultTableComparator());
		setDisplayName(strDisplayName);
		setColumnRendererSource(new SimpleTableColumnRendererSource());
		setValueRendererSource(new SimpleTableValueRendererSource());
		setEvaluator(objEvaluator);
	}

	/**
	 * Returns the display name of the column that will be used 
	 * in the table header.
	 * Override for internationalization.
	 * @return String the display name of the column
	 */
	public String getDisplayName()
	{
		return m_strDisplayName;
	}

	/**
	 * Sets the displayName.
	 * @param displayName The displayName to set
	 */
	public void setDisplayName(String displayName)
	{
		m_strDisplayName = displayName;
	}

	/**
	 * Returns the evaluator.
	 * @return ITableColumnEvaluator
	 */
	public ITableColumnEvaluator getEvaluator()
	{
		return m_objEvaluator;
	}

	/**
	 * Sets the evaluator.
	 * @param evaluator The evaluator to set
	 */
	public void setEvaluator(ITableColumnEvaluator evaluator)
	{
		m_objEvaluator = evaluator;
	}

	/**
	 * Extracts the value of the column from the row object
	 * @param objRow the row object
	 * @return Object the column value
	 */
	public Object getColumnValue(Object objRow)
	{
		ITableColumnEvaluator objEvaluator = getEvaluator();
		if (objEvaluator != null)
			return objEvaluator.getColumnValue(this, objRow);

		// default fallback
		return objRow.toString();
	}

	public class DefaultTableComparator implements Comparator, Serializable
	{
		public int compare(Object objRow1, Object objRow2)
		{
			Object objValue1 = getColumnValue(objRow1);
			Object objValue2 = getColumnValue(objRow2);

            if (objValue1 == objValue2)
                return 0;

            boolean bComparable1 = objValue1 instanceof Comparable;
            boolean bComparable2 = objValue2 instanceof Comparable;
                              
            // non-comparable values are considered equal 
			if (!bComparable1 && !bComparable2)
				return 0;

            // non-comparable values (null included) are considered smaller 
            // than the comparable one
            if (!bComparable1)
                return -1;

            if (!bComparable2)
                return 1;

			return ((Comparable) objValue1).compareTo(objValue2);
		}
	}

}
