package net.sf.tapestry.contrib.table.model.simple;

import net.sf.tapestry.IRender;
import net.sf.tapestry.contrib.table.model.ITableModelSource;

/**
 * An interface for initializing a renderer used by SimpleTableColumn
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ISimpleTableColumnRenderer extends IRender
{
	void initializeColumnRenderer(
		SimpleTableColumn objColumn,
		ITableModelSource objSource);
}
