//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
