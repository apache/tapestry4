//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
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
    public static final ITableRendererSource DEFAULT_COLUMN_RENDERER_SOURCE = 
        new SimpleTableColumnRendererSource();

    public static final ITableRendererSource FORM_COLUMN_RENDERER_SOURCE = 
        new SimpleTableColumnFormRendererSource();

    public static final ITableRendererSource DEFAULT_VALUE_RENDERER_SOURCE = 
        new SimpleTableValueRendererSource();

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
		setColumnRendererSource(DEFAULT_COLUMN_RENDERER_SOURCE);
		setValueRendererSource(DEFAULT_VALUE_RENDERER_SOURCE);
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
     * Sets a comparator that compares the values of this column rather than 
     * the objects representing the full rows. <br>
     * This method allows easier use of standard comparators for sorting
     * the column. It simply wraps the provided comparator with a row-to-column 
     * convertor and invokes the setComparator() method.
     * @param comparator The column value comparator
     */
    public void setColumnComparator(Comparator comparator)
    {
        setComparator(new ColumnComparator(this, comparator));
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

    /**
     *  Use the column name to get the display name, as well as 
     *  the column and value renderer sources from the provided component.
     *   
     *  @param objSettingsContainer the component from which to get the settings 
     */
    public void loadSettings(IComponent objSettingsContainer)
    {
        String strDisplayName = objSettingsContainer.getMessages().getMessage(getColumnName(), null);
        if (strDisplayName != null)
            setDisplayName(strDisplayName);
        
        super.loadSettings(objSettingsContainer);
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
            // than the comparable ones
            if (!bComparable1)
                return -1;

            if (!bComparable2)
                return 1;

			return ((Comparable) objValue1).compareTo(objValue2);
		}
	}

}
