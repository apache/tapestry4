/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.contrib.table.model;

import java.util.Comparator;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * The interface defining a table column. 
 * 
 * A column is responsible for presenting a particular part of the data
 * from the objects in the table. This is done via the getValueRender() method.
 * 
 * A column may be sortable, in which case it defines the way in which the
 * objects in the table must be sorted by providing a Comparator.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableColumn
{
	/**
	 * Method getColumnName provides the name of the column. 
	 *
	 * The column name must be unique and is generally used for the identification 
	 * of the column. It does not have to be the same as the display name 
	 * via which the column is identified to the user (see the getColumnRender() method).
	 * @return String the name of the column
	 */
	String getColumnName();

	/**
	 * Method getSortable declares whether the column allows sorting.
	 * If the column allows sorting, it must also return a valid Comparator
	 * via the getComparator() method.
	 * @return boolean whether the column is sortable or not
	 */
	boolean getSortable();

	/**
	 * Method getComparator returns the Comparator to be used to sort 
	 * the data in the table according to this column. The Comparator must
	 * accept two different rows, compare them according to this column, 
	 * and return the appropriate value.
	 * @return Comparator the Comparator used to sort the table data
	 */
	Comparator getComparator();

	/**
	 * Method getColumnRenderer provides a renderer that takes care of rendering 
	 * the column in the table header. If the column is sortable, the renderer
	 * may provide a mechanism to sort the table in an ascending or descending 
	 * manner.
	 * @param objCycle the current request cycle
	 * @param objSource a component that can provide the table model (typically TableView)
	 * @return IRender the renderer to present the column header
	 */
	IRender getColumnRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource);

	/**
	 * Method getValueRenderer provides a renderer for presenting the value of a 
	 * particular row in the current column.
	 * 
	 * @param objCycle the current request cycle
	 * @param objSource a component that can provide the table model (typically TableView)
	 * @param objRow the row data
	 * @return IRender the renderer to present the value of the row in this column
	 */
	IRender getValueRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		Object objRow);
}
