package org.apache.tapestry.contrib.tree.simple;

import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * Created on Sep 27, 2002
 *
 * @author ceco
 *
 */
public class NullSessionStateManager implements ITreeSessionStateManager {

	/**
	 * Constructor for NullSessionStateManager.
	 */
	public NullSessionStateManager() {
		super();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager#getSessionState(ITreeModel)
	 */
	public Object getSessionState(ITreeModel objModel) {
		return null;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager#getModel(Object)
	 */
	public ITreeModel getModel(Object objSessionState) {
		return null;
	}

}
