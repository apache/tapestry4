package net.sf.tapestry.contrib.table.model.common;

import java.io.Serializable;
import java.util.Comparator;

import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.model.*;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.valid.RenderString;

/**
 * A base implementation of {@link net.sf.tapestry.contrib.table.model.ITableColumn}
 * that allows renderers to be set via aggregation.
 * 
 * @see net.sf.tapestry.contrib.table.model.ITableRendererSource
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
	 * @see net.sf.tapestry.contrib.table.model.ITableColumn#getColumnName()
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
	 * @see net.sf.tapestry.contrib.table.model.ITableColumn#getSortable()
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
	 * @see net.sf.tapestry.contrib.table.model.ITableColumn#getComparator()
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
	 * @see net.sf.tapestry.contrib.table.model.ITableColumn#getColumnRenderer(IRequestCycle, ITableModelSource)
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
	 * @see net.sf.tapestry.contrib.table.model.ITableColumn#getValueRenderer(IRequestCycle, ITableModelSource, Object)
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
