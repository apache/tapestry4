package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.util.ComponentAddress;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 8, 2003
 * 
 * @author ceco
 */
public class TreeTableColumn extends SimpleTableColumn {

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TreeTableColumn(String arg0, boolean arg1, ComponentAddress objComponentAddress) {
		super(arg0, arg1);
		setValueRendererSource(new TreeTableValueRenderSource(objComponentAddress));
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.common.AbstractTableColumn#getValueRenderer(org.apache.tapestry.IRequestCycle, org.apache.tapestry.contrib.table.model.ITableModelSource, java.lang.Object)
	 */
	public IRender getValueRenderer(IRequestCycle arg0, ITableModelSource arg1, Object arg2) {
		return super.getValueRenderer(arg0, arg1, arg2);
	}

}
