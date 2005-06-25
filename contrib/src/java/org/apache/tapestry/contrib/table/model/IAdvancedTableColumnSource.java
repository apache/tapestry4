/*
 * Created on Jun 4, 2005
 */
package org.apache.tapestry.contrib.table.model;


public interface IAdvancedTableColumnSource 
{
    IAdvancedTableColumn generateTableColumn(String strName, String strDisplayName,
    		boolean bSortable, String strExpression);
}
