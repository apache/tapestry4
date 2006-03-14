//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.table.model.ognl;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator;
import org.apache.tapestry.util.prop.OgnlUtils;

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
	 * @see org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator#getColumnValue(ITableColumn, Object)
	 */
	public Object getColumnValue(ITableColumn objColumn, Object objRow)
	{
		// If no expression is given, then this is dummy column. Return something.
		if (m_strExpression == null || m_strExpression.equals(""))
			return "";

			synchronized (this)
			{
				if (m_objParsedExpression == null)
					m_objParsedExpression =
						OgnlUtils.getParsedExpression(m_strExpression);
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
