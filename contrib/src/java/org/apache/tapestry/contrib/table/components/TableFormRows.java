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

package org.apache.tapestry.contrib.table.components;

import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.*;


/**
 * A low level Table component that generates the rows of the current page in the table.
 * 
 * This component is a variant of {@link org.apache.tapestry.contrib.table.components.TablePages},
 * but is designed for operation in a form. The displayed rows are stored in 
 * hidden form fields, which are then read during a rewind. This ensures that
 * the form will rewind in exactly the same was as it was rendered even if the 
 * TableModel has changed and no StaleLink exceptions will occur. 
 * 
 * The component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableView}.
 * 
 * <p>
 * The component iterates over the rows of the current page in the table. 
 * The rows are wrapped in 'tr' tags by default. 
 * You can define columns manually within, or
 * you can use {@link org.apache.tapestry.contrib.table.components.TableValues} 
 * to generate the columns automatically.
 * <p> 
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TableFormRows.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TableFormRows extends TableRows
{
    public abstract IPrimaryKeyConvertor getConvertor();
    public abstract IPrimaryKeyConvertor getConvertorCache();
    public abstract void setConvertorCache(IPrimaryKeyConvertor convertor);
    public abstract Map getConvertedValues();

    /**
     * Returns the PK convertor cached within the realm of the current request cycle.
     *  
     * @return the cached PK convertor
     */
    public IPrimaryKeyConvertor getCachedConvertor()
    {
        IPrimaryKeyConvertor objConvertor = getConvertorCache();
        
        if (objConvertor == null) {
            objConvertor = getConvertor();
            setConvertorCache(objConvertor);
        }
        
        return objConvertor;
    }

    /**
     * Get the list of all table rows to be displayed on this page, converted 
     * using the PK.convertor.
     * 
     * @return an iterator of all converted table rows
     */    
    public Iterator getConvertedTableRowsIterator()
    {
        final Iterator objTableRowsIterator = getTableRowsIterator(); 
        final IPrimaryKeyConvertor objConvertor = getCachedConvertor();
        if (objConvertor == null)
            return objTableRowsIterator;
            
        return new Iterator()
        {
            public boolean hasNext()
            {
                return objTableRowsIterator.hasNext();
            }

            public Object next()
            {
                Object objValue = objTableRowsIterator.next();
                Object objPrimaryKey = objConvertor.getPrimaryKey(objValue);
                Map mapConvertedValues = getConvertedValues(); 
                mapConvertedValues.put(objPrimaryKey, objValue);
                return objPrimaryKey;
            }

            public void remove()
            {
                objTableRowsIterator.remove();
            }
        };
    }

    /**
     * Sets the current table row PK and invokes {@link #setTableRow(Object)} as a result.
     * This method is for internal use only.
     * 
     * @param objConvertedTableRow The current converted table row (PK)
     */
    public void setConvertedTableRow(Object objConvertedTableRow)
    {
        Object objValue = objConvertedTableRow;

        IPrimaryKeyConvertor objConvertor = getCachedConvertor();
        if (objConvertor != null) {
            IRequestCycle objCycle = getPage().getRequestCycle();
            if (objCycle.isRewinding()) {
                objValue = objConvertor.getValue(objConvertedTableRow);  
            }
            else {
                Map mapConvertedValues = getConvertedValues(); 
                objValue = mapConvertedValues.get(objConvertedTableRow);
            }
        }

        setTableRow(objValue);
    }
}
