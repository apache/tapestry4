package net.sf.tapestry.contrib.table.model.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @ version $Id : $
 * @author mindbridge
 */
public class SimpleSqlConnectionSource implements ISqlConnectionSource
{
	private static final Log LOG =
		LogFactory.getLog(SimpleSqlConnectionSource.class);

	private String m_strUrl;
	private String m_strUser;
	private String m_strPwd;

	public SimpleSqlConnectionSource(String strUrl)
	{
		this(strUrl, null, null);
	}

	public SimpleSqlConnectionSource(
		String strUrl,
		String strUser,
		String strPwd)
	{
		m_strUrl = strUrl;
		m_strUser = strUser;
		m_strPwd = strPwd;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.sql.ISqlConnectionSource#obtainConnection()
	 */
	public Connection obtainConnection() throws SQLException
	{
		if (m_strUser == null)
			return DriverManager.getConnection(m_strUrl);
		else
			return DriverManager.getConnection(m_strUrl, m_strUser, m_strPwd);
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.sql.ISqlConnectionSource#returnConnection(Connection)
	 */
	public void returnConnection(Connection objConnection)
	{
		try
		{
			objConnection.close();
		}
		catch (SQLException e)
		{
			LOG.warn("Could not close connection", e);
		}
	}

}
