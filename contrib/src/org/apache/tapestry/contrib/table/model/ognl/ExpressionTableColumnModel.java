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

import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;

/**
 * @author mindbridge
 *
 */
public class ExpressionTableColumnModel extends SimpleTableColumnModel
{
    /**
     * Constructs a table column model containting OGNL expression columns. <br>
     * The data for the columns is provided in the form of a string array,
     * where the info of each column is stored in two consecutive fields in
     * the array, hence its size must be even. The expected info is the following:
     * <ul>
     *   <li> Column Name
     *   <li> OGNL expression
     * </ul>
     * @param arrColumnInfo The information to construct the columns from
     * @param bSorted Whether all columns are sorted or not
     */
    public ExpressionTableColumnModel(String[] arrColumnInfo, boolean bSorted)
    {
        this(convertToDetailedArray(arrColumnInfo, bSorted));
    }

    /**
     * Constructs a table column model containting OGNL expression columns. <br>
     * The data for the columns is provided in the form of a string array,
     * where the info of each column is stored in four consecutive fields in
     * the array, hence its size must be divisible by 4. <br>
     * The expected info is the following:
     * <ul>
     *   <li> Column Name
     *   <li> Display Name
     *   <li> OGNL expression
     *   <li> Sorting of the column. This is either a Boolean, 
     *        or a String representation of a boolean.
     * </ul>
     * @param arrColumnInfo
     */
    public ExpressionTableColumnModel(Object[] arrColumnInfo)
    {
        super(convertToColumns(arrColumnInfo));
    }

    /**
     * Method convertToDetailedArray.
     * @param arrColumnInfo
     * @param bSorted
     * @return Object[]
     */
    protected static Object[] convertToDetailedArray(String[] arrColumnInfo, boolean bSorted)
    {
        int nColumns = arrColumnInfo.length / 2;
        int nSize = nColumns * 4;
        Object[] arrDetailedInfo = new Object[nSize];

        for (int i = 0; i < nColumns; i++)
        {
            int nInputBaseIndex = 2 * i;
            String strColumnName = arrColumnInfo[nInputBaseIndex];
            String strExpression = arrColumnInfo[nInputBaseIndex + 1];

            int nOutputBaseIndex = 4 * i;
            arrDetailedInfo[nOutputBaseIndex] = strColumnName;
            arrDetailedInfo[nOutputBaseIndex + 1] = strColumnName;
            arrDetailedInfo[nOutputBaseIndex + 2] = strExpression;
            arrDetailedInfo[nOutputBaseIndex + 3] = bSorted ? Boolean.TRUE : Boolean.FALSE;
        }

        return arrDetailedInfo;
    }

    /**
     * Method convertToColumns.
     * @param arrDetailedInfo
     * @return ITableColumn[]
     */
    protected static ITableColumn[] convertToColumns(Object[] arrDetailedInfo)
    {
        int nColumns = arrDetailedInfo.length / 4;
        ITableColumn[] arrColumns = new ITableColumn[nColumns];

        for (int i = 0; i < nColumns; i++)
        {
            Object objTempValue;
            int nBaseIndex = 4 * i;

            String strColumnName = "";
            objTempValue = arrDetailedInfo[nBaseIndex];
            if (objTempValue != null)
                strColumnName = objTempValue.toString();

            String strDisplayName = "";
            objTempValue = arrDetailedInfo[nBaseIndex + 1];
            if (objTempValue != null)
                strDisplayName = objTempValue.toString();

            String strExpression = "";
            objTempValue = arrDetailedInfo[nBaseIndex + 2];
            if (objTempValue != null)
                strExpression = objTempValue.toString();

            boolean bSorted = false;
            objTempValue = arrDetailedInfo[nBaseIndex + 3];
            if (objTempValue != null)
            {
                if (objTempValue instanceof Boolean)
                    bSorted = ((Boolean) objTempValue).booleanValue();
                else
                    bSorted = Boolean.getBoolean(objTempValue.toString());
            }

            arrColumns[i] =
                new ExpressionTableColumn(strColumnName, strDisplayName, strExpression, bSorted);
        }

        return arrColumns;
    }
}
