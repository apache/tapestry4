package net.sf.tapestry.contrib.table.model.ognl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;

/**
 * @author mindbridge
 *
 */
public class ExpressionTableColumn extends SimpleTableColumn
{
    private static final Log LOG = LogFactory.getLog(ExpressionTableColumn.class);

    public ExpressionTableColumn(String strColumnName, String strExpression) {
        this(strColumnName, strExpression, false);
    }

    public ExpressionTableColumn(String strColumnName, String strExpression, boolean bSortable) {
        this(strColumnName, strColumnName, strExpression, bSortable);
    }

    public ExpressionTableColumn(String strColumnName, String strDisplayName, String strExpression) {
        this(strColumnName, strDisplayName, strExpression, false);
    }

    public ExpressionTableColumn(String strColumnName, String strDisplayName, String strExpression, boolean bSortable) {
        super(strColumnName, strDisplayName, bSortable);
        setEvaluator(new OgnlTableColumnEvaluator(strExpression));
    }
}
