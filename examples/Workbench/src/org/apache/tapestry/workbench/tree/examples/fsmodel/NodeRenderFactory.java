/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
