/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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
 * @version $Id$
 * @author mindbridge
 */
public class SqlTableModel extends AbstractTableModel 
{
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
