package org.apache.tapestry.contrib.table.model;

import org.apache.tapestry.IComponent;

public interface IAdvancedTableColumn extends ITableColumn
{
	ITableRendererSource getColumnRendererSource();
	void setColumnRendererSource(ITableRendererSource columnRendererSource);

	ITableRendererSource getValueRendererSource();
	void setValueRendererSource(ITableRendererSource valueRendererSource);
	
	void loadSettings(IComponent objSettingsContainer);
}
