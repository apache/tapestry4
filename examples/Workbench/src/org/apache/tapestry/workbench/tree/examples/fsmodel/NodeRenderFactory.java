package org.apache.tapestry.workbench.tree.examples.fsmodel;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.PrivateAsset;
import org.apache.tapestry.contrib.tree.components.INodeRenderFactory;
import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;

/**
 * @author ceco
 */
public class NodeRenderFactory implements INodeRenderFactory {

    /**
     * Constructor for NodeRenderFactory.
     */
    public NodeRenderFactory() {
        super();
    }

    public IRender getRenderByID(Object objUniqueKey,
								 ITreeModelSource objTreeModelSource,
                                 IRequestCycle objCycle) {
        Object objValue = objTreeModelSource.getTreeModel().getTreeDataModel().getObject(objUniqueKey);
        return getRender(objValue, objTreeModelSource, objCycle);
    }

    public IRender getRender(Object objValue,
							 ITreeModelSource objTreeModelSource,
                             IRequestCycle objCycle) {
        return new CFileSystemRender(objValue, objTreeModelSource);
    }


    public class CFileSystemRender implements IRender{
        private Object m_objNode;
        private ITreeModelSource m_objTreeModelSource;

        public CFileSystemRender(Object objNode, ITreeModelSource objTreeModelSource) {
            super();
            m_objNode = objNode;
			m_objTreeModelSource = objTreeModelSource;
        }

        public boolean isOpen() {
            ITreeDataModel objDataModel =
				m_objTreeModelSource.getTreeModel().getTreeDataModel();
            ITreeStateModel objStateModel =
				m_objTreeModelSource.getTreeModel().getTreeStateModel();
            Object objUniqueKey = objDataModel.getUniqueKey(m_objNode, null);
            return objStateModel.isUniqueKeyExpanded(objUniqueKey);
        }

        public boolean isSelected(){
            ITreeDataModel objDataModel =
				m_objTreeModelSource.getTreeModel().getTreeDataModel();
            ITreeStateModel objStateModel =
				m_objTreeModelSource.getTreeModel().getTreeStateModel();
            Object objUniqueKey = objDataModel.getUniqueKey(m_objNode, null);
            return objUniqueKey.equals(objStateModel.getSelectedNode());
        }

        public void render(IMarkupWriter objWriter, IRequestCycle objCycle) {
            PrivateAsset objAsset = getAsset();
            objWriter.begin("img");
            objWriter.attribute("border", "0");
            objWriter.attribute("src", objAsset.buildURL(objCycle));
            objWriter.attribute("align", "bottom");
            objWriter.end();
            objWriter.print(" ");
            objWriter.begin("span");
            String strClassName = "fsNodeValue";
            objWriter.attribute("class", strClassName);
            objWriter.closeTag();
            objWriter.print(getNode().toString().trim());
            objWriter.end();
        }

        public IFileSystemTreeNode getNode(){
            return (IFileSystemTreeNode) m_objNode;
        }

        private PrivateAsset getAsset(){
            PrivateAsset objAsset;

            if (!isOpen()) {
                objAsset = getNode().getAssets().getAssetForCloseNode();
            } else {
                objAsset = getNode().getAssets().getAssetForOpenNode();
            }

            return objAsset;
        }
    }
}
