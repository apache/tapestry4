package net.sf.tapestry.contrib.table.model;

import net.sf.tapestry.IComponent;

/**
 * A Tapestry component that provides the current table model.
 * This interface is used for obtaining the table model source by
 * components wrapped by it, as well as by external renderers,
 * such as those provided by the column implementations
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableModelSource extends IComponent
{
    final static String TABLE_MODEL_SOURCE_ATTRIBUTE = "net.sf.tapestry.contrib.table.model.ITableModelSource";

	/**
	 * Returns the table model currently used
	 * @return ITableModel the current table model
	 */
	ITableModel getTableModel();

	/**
	 * Notifies the model source that the model state has changed, and 
     * that it should consider saving it.<p>
     * This method was added to allow using the table within a Block when 
     * the pageBeginRender() listener of the implementation will not be called
     * and automatic state storage will therefore be hard to implement.
	 */
    void fireObservedStateChange();
}
