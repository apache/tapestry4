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

package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Comparator;

import net.sf.tapestry.INamespace;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.valid.RenderString;

/**
 * @version $Id$
 * @author mindbridge
 *
 */
public abstract class SimpleTableColumn implements ITableColumn, Serializable
{
    private String m_strColumnName;
    private String m_strDisplayName;
    private boolean m_bSortable;
    private Comparator m_objComparator;

    public SimpleTableColumn(String strColumnName)
    {
        this(strColumnName, strColumnName);
    }

    public SimpleTableColumn(String strColumnName, boolean bSortable)
    {
        this(strColumnName, strColumnName, bSortable);
    }

    public SimpleTableColumn(String strColumnName, String strDisplayName)
    {
        this(strColumnName, strDisplayName, false);
    }

    public SimpleTableColumn(String strColumnName, String strDisplayName, boolean bSortable)
    {
        m_strColumnName = strColumnName;
        m_strDisplayName = strDisplayName;
        m_bSortable = bSortable;

        setComparator(new DefaultComparator());
    }

    public String getColumnName()
    {
        return m_strColumnName;
    }

    public String getDisplayName()
    {
        return m_strDisplayName;
    }

    public boolean getSortable()
    {
        return m_bSortable;
    }

    public Comparator getComparator()
    {
        return m_objComparator;
    }

    protected void setComparator(Comparator objComparator)
    {
        m_objComparator = objComparator;
    }

    public Object getColumnValue(Object objRow)
    {
        return objRow.toString();
    }

    public IRender getColumnRenderer(IRequestCycle objCycle, ITableModelSource objSource)
    {
        // to be implemented
        //return new RenderString(getDisplayName());
        INamespace objNamespace = objSource.getNamespace();
        String strNamespace = objNamespace.getExtendedId();
        if (strNamespace == null)
            strNamespace = "";
        else
            strNamespace = strNamespace + ":";

        IPage objPage = objCycle.getPage(strNamespace + "SimpleTableColumnPage");
        ISimpleTableColumnRenderer objRenderer =
            (ISimpleTableColumnRenderer) objPage.getComponent("tableColumnComponent");
        objRenderer.initializeColumnRenderer(this, objSource);
        return objRenderer;
    }

    public IRender getValueRenderer(IRequestCycle objCycle, ITableModelSource objSource, Object objRow)
    {
        return new RenderString(getColumnValue(objRow).toString());
    }

    private class DefaultComparator implements Comparator, Serializable
    {
        public int compare(Object objRow1, Object objRow2)
        {
            Object objValue1 = getColumnValue(objRow1);
            Object objValue2 = getColumnValue(objRow2);

            if (!(objValue1 instanceof Comparable) || !(objValue2 instanceof Comparable))
            {
                // error
                return 0;
            }

            return ((Comparable) objValue1).compareTo(objValue2);
        }
    }
}
