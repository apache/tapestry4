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

package org.apache.tapestry.contrib.table.model.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SqlTableColumn extends SimpleTableColumn
{
	private static final Log LOG = LogFactory.getLog(SqlTableColumn.class);

	/**
	 * Creates an SqlTableColumn
	 * @param strSqlField the identifying name of the column and the SQL field it refers to
	 * @param strDisplayName the display name of the column
	 */
	public SqlTableColumn(String strSqlField, String strDisplayName)
	{
		super(strSqlField, strDisplayName);
	}

	/**
	 * Creates an SqlTableColumn
	 * @param strSqlField the identifying name of the column and the SQL field it refers to
	 * @param strDisplayName the display name of the column
	 * @param bSortable whether the column is sortable
	 */
	public SqlTableColumn(
		String strSqlField,
		String strDisplayName,
		boolean bSortable)
	{
		super(strSqlField, strDisplayName, bSortable);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn#getColumnValue(Object)
	 */
	public Object getColumnValue(Object objRow)
	{
		try
		{
			ResultSet objRS = (ResultSet) objRow;
            String strColumnName = getColumnName();
			Object objValue = objRS.getObject(strColumnName);
			if (objValue == null)
				objValue = "";
			return objValue;
		}
		catch (SQLException e)
		{
			LOG.error("Cannot get the value for column: " + getColumnName(), e);
			return "";
		}
	}

}
