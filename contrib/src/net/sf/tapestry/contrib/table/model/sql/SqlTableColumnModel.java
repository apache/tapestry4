package net.sf.tapestry.contrib.table.model.sql;

import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumnModel;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SqlTableColumnModel extends SimpleTableColumnModel
{
	public SqlTableColumnModel(SqlTableColumn[] arrColumns)
	{
		super(arrColumns);
	}

	public SqlTableColumn getSqlColumn(int nColumn)
	{
		return (SqlTableColumn) getColumn(nColumn);
	}

	public SqlTableColumn getSqlColumn(String strColumn)
	{
		return (SqlTableColumn) getColumn(strColumn);
	}
}
