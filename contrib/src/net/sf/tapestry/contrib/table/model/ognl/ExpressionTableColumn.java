package net.sf.tapestry.contrib.table.model.ognl;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ognl.Ognl;
import ognl.OgnlException;

import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 * @author mindbridge
 *
 */
public class ExpressionTableColumn extends SimpleTableColumn
{
    private static final Logger LOG = LogManager.getLogger(ExpressionTableColumn.class);

    private String m_strExpression;
    transient private Object m_objParsedExpression = null;
    
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
        m_strExpression = strExpression;
    }

	/**
	 * @see net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn#getColumnValue(Object)
	 */
	public Object getColumnValue(Object objRow)
	{
        // If no expression is given, then this is dummy column. Return something.
        if (m_strExpression == null || m_strExpression.equals(""))
            return "";
        
        if (m_objParsedExpression == null) {
            synchronized (this) {
                if (m_objParsedExpression == null) {
                    // no need to synchronize, since the results should be identical
                    m_objParsedExpression = OgnlUtils.getParsedExpression(m_strExpression);
                }
            }
        }
        
		try
		{
			return Ognl.getValue(m_objParsedExpression, objRow);
		}
		catch (OgnlException e)
		{
            LOG.error("Cannot use column expression '" + m_strExpression + "' in row", e);
            return "";
		}
	}

}
