package org.apache.tapestry.contrib.tree.simple;

import org.apache.tapestry.contrib.tree.model.*;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * Created on Sep 24, 2002
 *
 * @author ceco
 *
 */
public class SimpleSessionStateManager implements ITreeSessionStateManager {

	/**
	 * Constructor for SimpleSessionStateManager.
	 */
	public SimpleSessionStateManager() {
		super();
	}

	/**
	 * @see com.rushmore.components.tree.ITreeSessionStateManager#getSessionState(ITreeModel)
	 */
	public Object getSessionState(ITreeModel objModel) {
		return objModel;
	}

	/**
	 * @see com.rushmore.components.tree.ITreeSessionStateManager#getModel(Object)
	 */
	public ITreeModel getModel(Object objSessionState) {
		return (ITreeModel)objSessionState;
	}

}
