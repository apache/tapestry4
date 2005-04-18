// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * @author mindbridge
 */
public class OgnlTableColumnEvaluator implements ITableColumnEvaluator
{
    /** @since 4.0 */

    private ExpressionEvaluator _expressionEvaluator;

    private static final Log LOG = LogFactory.getLog(ExpressionTableColumn.class);

    private String m_strExpression;

    public OgnlTableColumnEvaluator(String strExpression, ExpressionEvaluator expressionEvaluator)
    {
        m_strExpression = strExpression;
        _expressionEvaluator = expressionEvaluator;
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator#getColumnValue(ITableColumn,
     *      Object)
     */
    public synchronized Object getColumnValue(ITableColumn objColumn, Object objRow)
    {
        // If no expression is given, then this is dummy column. Return something.
        if (m_strExpression == null || m_strExpression.equals(""))
            return "";

        try
        {
            return _expressionEvaluator.read(objRow, m_strExpression);
        }
        catch (Exception e)
        {
            LOG.error("Cannot use column expression '" + m_strExpression + "' in row", e);
            return "";
        }
    }
}