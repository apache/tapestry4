package net.sf.tapestry.contrib.table.model.ognl;

import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.simple.ITableColumnEvaluator;
import net.sf.tapestry.util.prop.OgnlUtils;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mindbridge
 *
 */
public class OgnlTableColumnEvaluator implements ITableColumnEvaluator
{
	private static final Log LOG =
		LogFactory.getLog(ExpressionTableColumn.class);

	private String m_strExpression;
	transient private Object m_objParsedExpression = null;

	public OgnlTableColumnEvaluator(String strExpression)
	{
		m_strExpression = strExpression;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.simple.ITableColumnEvaluator#getColumnValue(ITableColumn, Object)
	 */
	public Object getColumnValue(ITableColumn objColumn, Object objRow)
	{
		// If no expression is given, then this is dummy column. Return something.
		if (m_strExpression == null || m_strExpression.equals(""))
			return "";

		if (m_objParsedExpression == null)
		{
			synchronized (this)
			{
				if (m_objParsedExpression == null)
					m_objParsedExpression =
						OgnlUtils.getParsedExpression(m_strExpression);
			}
		}

		try
		{
			Object objValue = Ognl.getValue(m_objParsedExpression, objRow);
			return objValue;
		}
		catch (OgnlException e)
		{
			LOG.error(
				"Cannot use column expression '" + m_strExpression + "' in row",
				e);
			return "";
		}
	}
}
