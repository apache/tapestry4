// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.contrib.table.model.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.common.AbstractTableModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableState;

/**
 * An implementation of ITableModel that obtains its data through SQL queries.
 * This is a very efficient model, since it uses SQL to perform
 * the data sorting (through ORDER BY) and obtains only the data
 * on the current page (through LIMIT/OFFSET). 
 * <p>
 * This object is typically created in the following manner:
 * <pre> 
 *    ISqlConnectionSource objConnSrc = 
 *        new SimpleSqlConnectionSource("jdbc:postgresql://localhost/testdb", "testdb", "testdb");
 *
 *    ISqlTableDataSource objDataSrc = 
 *        new SimpleSqlTableDataSource(objConnSrc, "test_table");
 *
 *    SqlTableColumnModel objColumnModel = 
 *        new SqlTableColumnModel(new SqlTableColumn[] {
 *            new SqlTableColumn("language", "Language", true),
 *            new SqlTableColumn("country", "Country", true),
 *            new SqlTableColumn("variant", "Variant", true),
 *            new SqlTableColumn("intvalue", "Integer", true),
 *            new SqlTableColumn("floatvalue", "Float", true)
 *        });
 *
 *    ITableModel objTableModel = new SqlTableModel(objDataSrc, objColumnModel);
 *
 *    return objTableModel;
 * </pre> 
 *  
 * @author mindbridge
 */
public class SqlTableModel extends AbstractTableModel 
{
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(SqlTableModel.class);

	private ISqlTableDataSource m_objDataSource;
	private SqlTableColumnModel m_objColumnModel;
    
    {
        try {
            Class.forName ( "org.hsqldb.jdbcDriver" );
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
        }
    }

	public SqlTableModel(
		ISqlTableDataSource objDataSource,
		SqlTableColumnModel objColumnModel)
	{
		this(objDataSource, objColumnModel, new SimpleTableState());
	}

	public SqlTableModel(
		ISqlTableDataSource objDataSource,
		SqlTableColumnModel objColumnModel,
		SimpleTableState objState)
	{
        super(objState);
		m_objDataSource = objDataSource;
		m_objColumnModel = objColumnModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableModel#getColumnModel()
	 */
	public ITableColumnModel getColumnModel()
	{
		return m_objColumnModel;
	}

	public SqlTableColumnModel getSqlColumnModel()
	{
		return m_objColumnModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableModel#getCurrentPageRows()
	 */
	public Iterator getCurrentPageRows()
	{
		try
		{
			ResultSet objResultSet =
				getSqlDataSource().getCurrentRows(
					getSqlColumnModel(),
					getState());

			return new ResultSetIterator(objResultSet)
			{
				protected void notifyEnd()
				{
					getSqlDataSource().closeResultSet(getResultSet());
				}
			};
		}
		catch (SQLException e)
		{
			LOG.error("Cannot get current page rows", e);
			return new ResultSetIterator(null);
		}
	}

	/**
	 * Returns the dataSource.
	 * @return ISqlTableDataSource
	 */
	public ISqlTableDataSource getSqlDataSource()
	{
		return m_objDataSource;
	}

    /**
     * @see org.apache.tapestry.contrib.table.model.common.AbstractTableModel#getRowCount()
     */
    protected int getRowCount()
    {
        try
        {
            return m_objDataSource.getRowCount();
        }
        catch (SQLException e)
        {
            LOG.error("Cannot get row count", e);
            return 1;
        }
    }

}
