package org.apache.tapestry.contrib.tree.simple;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.tree.components.INodeRenderFactory;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.valid.RenderString;

/**
 * @author ceco
 */

public class SimpleNodeRenderFactory implements INodeRenderFactory {

	/**
	 * Constructor for SimpleNodeRenderFactory.
	 */
	public SimpleNodeRenderFactory() {
		super();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.INodeRenderFactory#getRender(ITreeNode, ITreeModel, IRequestCycle)
	 */
	public IRender getRenderByID(
		Object objUniqueKey,
		ITreeModelSource objTreeModelSource,
		IRequestCycle cycle)
	{
		Object objValue = objTreeModelSource.getTreeModel().getTreeDataModel().getObject(objUniqueKey);
		return getRender(objValue, objTreeModelSource, cycle);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.INodeRenderFactory#getRender(Object, IRequestCycle)
	 */
	public IRender getRender(Object objValue, ITreeModelSource objTreeModelSource, IRequestCycle objCycle) {
		return new RenderString(objValue.toString());
	}

}
