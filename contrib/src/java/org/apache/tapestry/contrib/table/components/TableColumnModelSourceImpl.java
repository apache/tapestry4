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

package org.apache.tapestry.contrib.table.components;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.ognl.ExpressionTableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * A placeholder for a static methods related to the Table component
 * 
 * @since 3.0
 * @author Mindbridge
 */
public class TableColumnModelSourceImpl implements TableColumnModelSource
{
    /** @since 3.1 */
    private ExpressionEvaluator _expressionEvaluator;

    /** @since 3.1 */

    public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator)
    {
        _expressionEvaluator = expressionEvaluator;
    }

    /**
     * Generate a table column model out of the description string provided. Entries in the
     * description string are separated by commas. Each column entry is of the format name,
     * name:expression, or name:displayName:expression. An entry prefixed with ! represents a
     * non-sortable column. If the whole description string is prefixed with *, it represents
     * columns to be included in a Form.
     * 
     * @param strDesc
     *            the description of the column model to be generated
     * @param objComponent
     *            the component ordering the generation
     * @param objColumnSettingsContainer
     *            the component containing the column settings
     * @return a table column model based on the provided parameters
     */
    public ITableColumnModel generateTableColumnModel(String strDesc, IComponent objComponent,
            IComponent objColumnSettingsContainer)
    {
        if (strDesc == null)
            return null;

        List arrColumns = new ArrayList();

        boolean bFormColumns = false;
        while (strDesc.startsWith("*"))
        {
            strDesc = strDesc.substring(1);
            bFormColumns = true;
        }

        StringTokenizer objTokenizer = new StringTokenizer(strDesc, ",");
        while (objTokenizer.hasMoreTokens())
        {
            String strToken = objTokenizer.nextToken().trim();

            if (strToken.startsWith("="))
            {
                String strColumnExpression = strToken.substring(1);

                Object objColumn = _expressionEvaluator.read(
                        objColumnSettingsContainer,
                        strColumnExpression);

                if (!(objColumn instanceof ITableColumn))
                    throw new ApplicationRuntimeException(TableMessages.notAColumn(
                            objComponent,
                            strColumnExpression));

                arrColumns.add(objColumn);
                continue;
            }

            boolean bSortable = true;
            if (strToken.startsWith("!"))
            {
                strToken = strToken.substring(1);
                bSortable = false;
            }

            StringTokenizer objColumnTokenizer = new StringTokenizer(strToken, ":");

            String strName = "";
            if (objColumnTokenizer.hasMoreTokens())
                strName = objColumnTokenizer.nextToken();

            String strExpression = strName;
            if (objColumnTokenizer.hasMoreTokens())
                strExpression = objColumnTokenizer.nextToken();

            String strDisplayName = strName;
            if (objColumnTokenizer.hasMoreTokens())
            {
                strDisplayName = strExpression;
                strExpression = objColumnTokenizer.nextToken();
            }

            ExpressionTableColumn objColumn = new ExpressionTableColumn(strName, strDisplayName,
                    strExpression, bSortable, _expressionEvaluator);
            if (bFormColumns)
                objColumn.setColumnRendererSource(SimpleTableColumn.FORM_COLUMN_RENDERER_SOURCE);
            if (objColumnSettingsContainer != null)
                objColumn.loadSettings(objColumnSettingsContainer);

            arrColumns.add(objColumn);
        }

        return new SimpleTableColumnModel(arrColumns);
    }

}