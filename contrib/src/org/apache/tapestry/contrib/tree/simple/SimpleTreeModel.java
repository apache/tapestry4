package org.apache.tapestry.contrib.tree.simple;

import java.io.Serializable;

import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * Created on Sep 25, 2002
 *
 * @author ceco
 *
 */
public class SimpleTreeModel implements ITreeModel, Serializable{

	private ITreeDataModel m_objDataModel;
	private ITreeStateModel m_objTreeStateModel;
	
	/**
	 * Constructor for SimpleTreeModel.
	 */
	public SimpleTreeModel(ITreeDataModel objDataModel) {
		this(objDataModel, new SimpleTreeStateModel());
	}

	public SimpleTreeModel(ITreeDataModel objDataModel, ITreeStateModel objTreeStateModel) {
		super();
		m_objDataModel = objDataModel;
		m_objTreeStateModel = objTreeStateModel;
	}
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeModel#getTreeDataModel()
	 */
	public ITreeDataModel getTreeDataModel() {
		return m_objDataModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeModel#getTreeStateModel()
	 */
	public ITreeStateModel getTreeStateModel() {
		return m_objTreeStateModel;
	}

}
