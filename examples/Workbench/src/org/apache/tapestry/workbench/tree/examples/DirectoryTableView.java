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

package org.apache.tapestry.workbench.tree.examples;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.contrib.table.components.Table;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableModel;
import org.apache.tapestry.workbench.tree.examples.fsmodel.SFObject;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 * @author ceco
 * @version $Id$
 */
public class DirectoryTableView extends BaseComponent implements PageDetachListener{

	private ITableModel m_objTableModel = null;
	private ITableColumn[] m_arrColumns = null;
	private ISelectedFolderSource m_objSelectedFolderSource = null;
	/**
	 * 
	 */
	public DirectoryTableView() {
		super();
		initialize();
	}


	private void initialize(){
		m_objTableModel = null;
		m_objSelectedFolderSource = null;
	}
	
	/**
	 * @see org.apache.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad() {
		super.finishLoad();
		getPage().addPageDetachListener(this);
	}


	/**
	 * @see org.apache.tapestry.event.PageDetachListener#pageDetached(org.apache.tapestry.event.PageEvent)
	 */
	public void pageDetached(PageEvent arg0) {
		initialize();
	}

	public ITableModel getTableModel() {
		if(m_objTableModel == null){
			ISelectedFolderSource objSelectedFolderSource = getSelectedFolderSource();
			Collection colChildrens = objSelectedFolderSource.getSelectedFolderChildren();
			
			m_objTableModel = new SimpleTableModel(colChildrens.toArray(), getColumns());
		}
		return m_objTableModel;
	}

	public ITableColumn[] getColumns() {
		if(m_arrColumns == null){
			ArrayList arrColumnsList = new ArrayList();
			arrColumnsList.add(new SimpleTableColumn ("Name", true) 
				{ 
					public Object getColumnValue(Object objValue) {
						SFObject objSFObject = (SFObject) objValue;
						return objSFObject.getName();
					}
				});

			arrColumnsList.add(new SimpleTableColumn ("Date", true) 
				{ 
					public Object getColumnValue(Object objValue) {
						SFObject objSFObject = (SFObject) objValue;
						return objSFObject.getDate();
					}
				});

			m_arrColumns = new SimpleTableColumn[arrColumnsList.size()];
			arrColumnsList.toArray(m_arrColumns);
		}
		return m_arrColumns;
	}

	public ISelectedFolderSource getSelectedFolderSource() {
		if(m_objSelectedFolderSource == null){
			IBinding objBinding = getBinding("selectedFolderSource");
			m_objSelectedFolderSource = (ISelectedFolderSource)objBinding.getObject();
		}
		return m_objSelectedFolderSource;
	}


	public void resetState(){
		initialize();
		Table objTable = (Table)getComponent("table");
		objTable.reset();
	}
	
	public String getSelectedNodeName(){
		return getSelectedFolderSource().getSelectedNodeName();
	}
}
