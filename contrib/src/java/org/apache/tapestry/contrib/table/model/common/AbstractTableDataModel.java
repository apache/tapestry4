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

package org.apache.tapestry.contrib.table.model.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.contrib.table.model.CTableDataModelEvent;
import org.apache.tapestry.contrib.table.model.ITableDataModel;
import org.apache.tapestry.contrib.table.model.ITableDataModelListener;

/**
 * An implementation of the listener support in the ITableDataModel interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public abstract class AbstractTableDataModel implements ITableDataModel
{
	private List m_arrListeners;

	public AbstractTableDataModel()
	{
		m_arrListeners = new ArrayList();
	}

	/**
	 * Method fireTableDataModelEvent.
	 * Fires a change event to all listeners
	 * @param objEvent the event to pass to the listeners
	 */
	protected void fireTableDataModelEvent(CTableDataModelEvent objEvent)
	{
        synchronized (m_arrListeners) {
            List arrEmptyReferences = null;
        
    		for (Iterator it = m_arrListeners.iterator(); it.hasNext();)
    		{
                WeakReference objRef = (WeakReference) it.next();
    			ITableDataModelListener objListener =
    				(ITableDataModelListener) objRef.get();
                if (objListener != null) 
                    objListener.tableDataChanged(objEvent);
                else {
                    if (arrEmptyReferences == null)
                        arrEmptyReferences = new ArrayList();
                    arrEmptyReferences.add(objRef);
                }
    		}

            if (arrEmptyReferences != null)
                m_arrListeners.removeAll(arrEmptyReferences);
        }
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableDataModel#addTableDataModelListener(ITableDataModelListener)
	 */
	public void addTableDataModelListener(ITableDataModelListener objListener)
	{
        synchronized (m_arrListeners) {
    		m_arrListeners.add(new WeakReference(objListener));
        }
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableDataModel#removeTableDataModelListener(ITableDataModelListener)
	 */
	public void removeTableDataModelListener(ITableDataModelListener objListener)
	{
        synchronized (m_arrListeners) {
            List arrEmptyReferences = null;
        
            for (Iterator it = m_arrListeners.iterator(); it.hasNext();)
            {
                WeakReference objRef = (WeakReference) it.next();
                ITableDataModelListener objStoredListener =
                    (ITableDataModelListener) objRef.get();
                if (objListener == objStoredListener || objStoredListener == null) { 
                    if (arrEmptyReferences == null)
                        arrEmptyReferences = new ArrayList();
                    arrEmptyReferences.add(objRef);
                }
            }

            if (arrEmptyReferences != null)
                m_arrListeners.removeAll(arrEmptyReferences);
        }
	}

}
