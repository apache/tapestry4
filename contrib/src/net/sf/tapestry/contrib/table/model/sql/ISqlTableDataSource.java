package net.sf.tapestry.contrib.table.model.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.tapestry.contrib.table.model.simple.SimpleTableState;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ISqlTableDataSource
{
	int getRowCount() throws SQLException;
	ResultSet getCurrentRows(
		SqlTableColumnModel objColumnModel,
		SimpleTableState objState)
		throws SQLException;
	void closeResultSet(ResultSet objResultSet);
}
