package net.sf.tapestry.contrib.table.model.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public class ResultSetIterator implements Iterator
{
	private static final Log LOG = LogFactory.getLog(ResultSetIterator.class);

	private ResultSet m_objResultSet;
	private boolean m_bFetched;
	private boolean m_bAvailable;

	public ResultSetIterator(ResultSet objResultSet)
	{
		m_objResultSet = objResultSet;
		m_bFetched = false;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public synchronized boolean hasNext()
	{
        if (getResultSet() == null) return false;
        
		if (!m_bFetched)
		{
			m_bFetched = true;

			try
			{
				m_bAvailable = !getResultSet().isLast();
			}
			catch (SQLException e)
			{
				LOG.warn(
					"SQLException while testing for end of the ResultSet",
					e);
				m_bAvailable = false;
			}

			if (!m_bAvailable)
				notifyEnd();
		}

		return m_bAvailable;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public synchronized Object next()
	{
		ResultSet objResultSet = getResultSet();

		try
		{
			if (!objResultSet.next())
				return null;
		}
		catch (SQLException e)
		{
			LOG.warn("SQLException while iterating over the ResultSet", e);
			return null;
		}

		m_bFetched = false;
		return objResultSet;
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		try
		{
			getResultSet().deleteRow();
		}
		catch (SQLException e)
		{
			LOG.error("Cannot delete record", e);
		}
	}

	/**
	 * Returns the resultSet.
	 * @return ResultSet
	 */
	public ResultSet getResultSet()
	{
		return m_objResultSet;
	}

	protected void notifyEnd()
	{
	}

}
