package org.apache.tapestry.contrib.tree.simple;

import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * Created on Sep 25, 2002
 *
 * @author ceco
 *
 */
public class FullTreeSessionStateManager implements ITreeSessionStateManager {

	/**
	 * Constructor for FullTreeSessionStateManager.
	 */
	public FullTreeSessionStateManager() {
		super();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager#getSessionState(ITreeModel)
	 */
	public Object getSessionState(ITreeModel objModel) {
		return objModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager#getModel(Object)
	 */
	public ITreeModel getModel(Object objSessionState) {
		return (ITreeModel)objSessionState;
	}

}
