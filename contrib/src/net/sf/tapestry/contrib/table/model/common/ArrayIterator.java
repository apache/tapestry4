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

package net.sf.tapestry.contrib.table.model.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @version $Id$
 * @author mindbridge
 */
public class ArrayIterator implements Iterator {
    private Object[] m_arrValues;
    private int m_nFrom;
    private int m_nTo;
    private int m_nCurrent;

    public ArrayIterator(Object[] arrValues) {
        this(arrValues, 0, arrValues.length);
    }

    public ArrayIterator(Object[] arrValues, int nFrom, int nTo) {
        m_arrValues = arrValues;
        m_nFrom = nFrom;
        m_nTo = nTo;
        
        if (m_nFrom < 0) m_nFrom = 0;
        if (m_nTo < m_nFrom) m_nTo = m_nFrom;
        if (m_nTo > m_arrValues.length) m_nTo = m_arrValues.length;

        m_nCurrent = m_nFrom;
    }
    
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return m_nCurrent < m_nTo;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
        //System.out.println("index: " + m_nCurrent + "   size: " + m_arrValues.length + "  to: " + m_nTo);
		if (!hasNext()) throw new NoSuchElementException();
        return m_arrValues[m_nCurrent++];
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
        throw new UnsupportedOperationException();
	}

}
