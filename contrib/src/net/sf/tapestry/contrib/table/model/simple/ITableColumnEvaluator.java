package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITableColumn;

/**
 * @author mindbridge
 *
 */
public interface ITableColumnEvaluator extends Serializable
{
	Object getColumnValue(ITableColumn objColumn, Object objRow);
}
