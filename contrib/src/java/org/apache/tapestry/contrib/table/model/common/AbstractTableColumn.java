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

package org.apache.tapestry.contrib.table.model.common;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
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
    /**
     *  The suffix of the name of the Block that will be used as the column renderer
     *  for this column 
     */
    public final static String COLUMN_RENDERER_BLOCK_SUFFIX = "ColumnHeader";

    /**
     *  The suffix of the name of the Block that will be used as the value renderer 
     *  for this column 
     */
    public final static String VALUE_RENDERER_BLOCK_SUFFIX = "ColumnValue";
    
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
     * 
	 * @return the valueRendererSource of this column
	 */
	public ITableRendererSource getValueRendererSource()
	{
		return m_objValueRendererSource;
	}

	/**
	 * Sets the valueRendererSource.
     * 
	 * @param valueRendererSource The valueRendererSource to set
	 */
	public void setValueRendererSource(ITableRendererSource valueRendererSource)
	{
		m_objValueRendererSource = valueRendererSource;
	}

    /**
     *  Use the column name to get the column and value renderer sources 
     *  from the provided component.
     *   
     *  @param objSettingsContainer the component from which to get the settings 
     */
    public void loadSettings(IComponent objSettingsContainer)
    {
        IComponent objColumnRendererSource = (IComponent) objSettingsContainer.getComponents().get(getColumnName() + COLUMN_RENDERER_BLOCK_SUFFIX);
        if (objColumnRendererSource != null && objColumnRendererSource instanceof Block)
            setColumnRendererSource(new BlockTableRendererSource((Block) objColumnRendererSource));

        IComponent objValueRendererSource = (IComponent) objSettingsContainer.getComponents().get(getColumnName() + VALUE_RENDERER_BLOCK_SUFFIX);
        if (objValueRendererSource != null && objValueRendererSource instanceof Block)
            setValueRendererSource(new BlockTableRendererSource((Block) objValueRendererSource));
    }

}
