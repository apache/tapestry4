package org.apache.tapestry.workbench.tree.examples;

import java.util.Collection;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 4, 2003
 * 
 * @author ceco
 */
public interface ISelectedFolderSource {
	Collection getSelectedFolderChildren();
	String getSelectedNodeName();
}
