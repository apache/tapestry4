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
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 2, 2003
 * 
 * @author ceco
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

	/**
	 * @return
	 */
	public ITableModel getTableModel() {
		if(m_objTableModel == null){
			ISelectedFolderSource objSelectedFolderSource = getSelectedFolderSource();
			Collection colChildrens = objSelectedFolderSource.getSelectedFolderChildren();
			
			m_objTableModel = new SimpleTableModel(colChildrens.toArray(), getColumns());
		}
		return m_objTableModel;
	}

	/**
	 * @return
	 */
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

	/**
	 * @return
	 */
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
