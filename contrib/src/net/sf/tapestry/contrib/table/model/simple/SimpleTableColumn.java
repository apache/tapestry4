package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Comparator;

import net.sf.tapestry.contrib.table.model.common.AbstractTableColumn;

/**
 * A simple minimal implementation of the 
 * {@link net.sf.tapestry.contrib.table.model.ITableColumn} interface that
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
		setComparator(new DefaultComparator());
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

	private class DefaultComparator implements Comparator, Serializable
	{
		public int compare(Object objRow1, Object objRow2)
		{
			Object objValue1 = getColumnValue(objRow1);
			Object objValue2 = getColumnValue(objRow2);

			if (!(objValue1 instanceof Comparable)
				|| !(objValue2 instanceof Comparable))
			{
				// error
				return 0;
			}

			return ((Comparable) objValue1).compareTo(objValue2);
		}
	}

}
