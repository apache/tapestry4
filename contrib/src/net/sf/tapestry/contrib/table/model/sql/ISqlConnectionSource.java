package net.sf.tapestry.contrib.table.model.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ISqlConnectionSource
{
	Connection obtainConnection() throws SQLException;
	void returnConnection(Connection objConnection);
}
