package net.sf.tapestry.contrib.table.model.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	 * @see net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn#getColumnValue(Object)
	 */
	public Object getColumnValue(Object objRow)
	{
		try
		{
			ResultSet objRS = (ResultSet) objRow;
			Object objValue = objRS.getObject(getColumnName());
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
