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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @version $Id : $
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
	 * @see org.apache.tapestry.contrib.table.model.sql.ISqlConnectionSource#obtainConnection()
	 */
	public Connection obtainConnection() throws SQLException
	{
		if (m_strUser == null)
			return DriverManager.getConnection(m_strUrl);
		else
			return DriverManager.getConnection(m_strUrl, m_strUser, m_strPwd);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.sql.ISqlConnectionSource#returnConnection(Connection)
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
