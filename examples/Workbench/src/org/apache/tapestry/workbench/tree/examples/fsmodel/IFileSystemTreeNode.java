package org.apache.tapestry.workbench.tree.examples.fsmodel;

import java.util.Date;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

/**
 * @author ceco
 */
public interface IFileSystemTreeNode extends ITreeNode {
    String getAbsolutePath();
    AssetsHolder getAssets();
    Date getDate();
}
