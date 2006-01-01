// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * @author mindbridge
 */
public class ExpressionTableColumn extends SimpleTableColumn
{
	private static final long serialVersionUID = 1L;
	
    /** @since 4.0 */

    public ExpressionTableColumn(String strColumnName, String strExpression,
            ExpressionEvaluator expressionEvaluator)
    {
        this(strColumnName, strExpression, false, expressionEvaluator);
    }

    public ExpressionTableColumn(String strColumnName, String strExpression, boolean bSortable,
            ExpressionEvaluator expressionEvaluator)
    {
        this(strColumnName, strColumnName, strExpression, bSortable, expressionEvaluator);
    }

    public ExpressionTableColumn(String strColumnName, String strDisplayName, String strExpression,
            ExpressionEvaluator expressionEvaluator)
    {
        this(strColumnName, strDisplayName, strExpression, false, expressionEvaluator);
    }

    public ExpressionTableColumn(String strColumnName, String strDisplayName, String strExpression,
            boolean bSortable, ExpressionEvaluator expressionEvaluator

    )
    {
        super(strColumnName, strDisplayName, bSortable);

        setEvaluator(new OgnlTableColumnEvaluator(strExpression, expressionEvaluator));
    }
}