package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 2, 2003
 * 
 * @author ceco
 */
public class TreeTableNodeViewDelegator extends BaseComponent implements ITableRendererListener{

	/**
	 * 
	 */
	public TreeTableNodeViewDelegator() {
		super();
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(org.apache.tapestry.IRequestCycle, org.apache.tapestry.contrib.table.model.ITableModelSource, org.apache.tapestry.contrib.table.model.ITableColumn, java.lang.Object)
	 */
	public void initializeRenderer(
		IRequestCycle arg0,
		ITableModelSource arg1,
		ITableColumn arg2,
		Object arg3) {
	}

}
