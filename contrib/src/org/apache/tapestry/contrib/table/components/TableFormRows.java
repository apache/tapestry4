/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
 * 
 * <p>
 * <table border=1 align="center">
 * <tr>
 *    <th>Parameter</th>
 *    <th>Type</th>
 *    <th>Direction </th>
 *    <th>Required</th>
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>row</td>
 *  <td>Object</td>
 *  <td>out</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The value object of the current row.</td> 
 * </tr>
 *
 * <tr>
 *  <td>element</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>tr</td>
 *  <td align="left">The tag to use to wrap the rows in.</td> 
 * </tr>
 *
 * </table> 
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
    
    public IPrimaryKeyConvertor getCachedConvertor()
    {
        IPrimaryKeyConvertor objConvertor = getConvertorCache();
        
        if (objConvertor == null) {
            objConvertor = getConvertor();
            setConvertorCache(objConvertor);
        }
        
        return objConvertor;
    }
    
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
