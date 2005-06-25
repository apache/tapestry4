/*
 * Created on Jun 4, 2005
 */
package org.apache.tapestry.contrib.table.components;

import org.apache.tapestry.contrib.table.model.IAdvancedTableColumn;
import org.apache.tapestry.contrib.table.model.IAdvancedTableColumnSource;
import org.apache.tapestry.contrib.table.model.ognl.ExpressionTableColumn;
import org.apache.tapestry.services.ExpressionEvaluator;

public class DefaultTableColumnSource implements IAdvancedTableColumnSource
{
    /** @since 4.0 */
    private ExpressionEvaluator _expressionEvaluator;

    /** @since 4.0 */
    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator)
    {
        _expressionEvaluator = expressionEvaluator;
    }
	
	public IAdvancedTableColumn generateTableColumn(String strName, String strDisplayName, 
			boolean bSortable, String strExpression) {
		return new ExpressionTableColumn(strName, strDisplayName, strExpression, bSortable, _expressionEvaluator);
	}
}
