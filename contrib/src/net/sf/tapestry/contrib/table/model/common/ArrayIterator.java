package net.sf.tapestry.contrib.table.model.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @version $Id$
 * @author mindbridge
 */
public class ArrayIterator implements Iterator
{
	private Object[] m_arrValues;
	private int m_nFrom;
	private int m_nTo;
	private int m_nCurrent;

	public ArrayIterator(Object[] arrValues)
	{
		this(arrValues, 0, arrValues.length);
	}

	public ArrayIterator(Object[] arrValues, int nFrom, int nTo)
	{
		m_arrValues = arrValues;
		m_nFrom = nFrom;
		m_nTo = nTo;

		if (m_nFrom < 0)
			m_nFrom = 0;
		if (m_nTo < m_nFrom)
			m_nTo = m_nFrom;
		if (m_nTo > m_arrValues.length)
			m_nTo = m_arrValues.length;

		m_nCurrent = m_nFrom;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		return m_nCurrent < m_nTo;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		//System.out.println("index: " + m_nCurrent + "   size: " + m_arrValues.length + "  to: " + m_nTo);
		if (!hasNext())
			throw new NoSuchElementException();
		return m_arrValues[m_nCurrent++];
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

}
