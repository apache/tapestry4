package net.sf.tapestry.contrib.table.model.common;

import java.util.Comparator;

/**
 * @version $Id$
 * @author mindbridge
 *
 */
public class ReverseComparator implements Comparator
{
	private Comparator m_objComparator;

	public ReverseComparator(Comparator objComparator)
	{
		m_objComparator = objComparator;
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object objValue1, Object objValue2)
	{
		return -m_objComparator.compare(objValue1, objValue2);
	}

}
