/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
     * where the info of each column is stored in two consequtive fields in 
     * the array, hence its size must be even. The expected info is the following:
     * <ul>
     *   <li> Column Name
     *   <li> OGNL expression
     * </ul>
	 * @param arrColumnInfo The information to construct the columns from
	 * @param bSorted Whether all columns are sorted or not
	 */
    public ExpressionTableColumnModel(String[] arrColumnInfo, boolean bSorted) {
        this(convertToDetailedArray(arrColumnInfo, bSorted));
    }
    
	/**
     * Constructs a table column model containting OGNL expression columns. <br>
     * The data for the columns is provided in the form of a string array,
     * where the info of each column is stored in four consequtive fields in 
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
    public ExpressionTableColumnModel(Object[] arrColumnInfo) {
        super(convertToColumns(arrColumnInfo));
    }
    
	/**
	 * Method convertToDetailedArray.
	 * @param arrColumnInfo
	 * @param bSorted
	 * @return Object[]
	 */
    protected static Object[] convertToDetailedArray(String[] arrColumnInfo, boolean bSorted) {
        int nColumns = arrColumnInfo.length/2;
        int nSize = nColumns * 4;
        Object[] arrDetailedInfo = new Object[nSize];
        
        for (int i = 0; i < nColumns; i++) {
            int nInputBaseIndex = 2*i;
            String strColumnName = arrColumnInfo[nInputBaseIndex];
            String strExpression = arrColumnInfo[nInputBaseIndex+1];
            
            int nOutputBaseIndex = 4*i;
            arrDetailedInfo[nOutputBaseIndex] = strColumnName;
            arrDetailedInfo[nOutputBaseIndex+1] = strColumnName;
            arrDetailedInfo[nOutputBaseIndex+2] = strExpression;
            arrDetailedInfo[nOutputBaseIndex+3] = new Boolean(bSorted);
        }
        
        return arrDetailedInfo;
    }
    
	/**
	 * Method convertToColumns.
	 * @param arrDetailedInfo
	 * @return ITableColumn[]
	 */
    protected static ITableColumn[] convertToColumns(Object[] arrDetailedInfo) {
        int nColumns = arrDetailedInfo.length / 4;
        ITableColumn[] arrColumns = new ITableColumn[nColumns];
        
        for (int i = 0; i < nColumns; i++) {
            Object objTempValue;
            int nBaseIndex = 4*i;

            String strColumnName = "";
            objTempValue = arrDetailedInfo[nBaseIndex];
            if (objTempValue != null) 
                strColumnName = objTempValue.toString();
                
            String strDisplayName = "";
            objTempValue = arrDetailedInfo[nBaseIndex+1];
            if (objTempValue != null) 
                strDisplayName = objTempValue.toString();
            
            String strExpression = "";
            objTempValue = arrDetailedInfo[nBaseIndex+2];
            if (objTempValue != null) 
                strExpression = objTempValue.toString();
            
            boolean bSorted = false;
            objTempValue = arrDetailedInfo[nBaseIndex+3];
            if (objTempValue != null) {
                if (objTempValue instanceof Boolean)
                    bSorted = ((Boolean) objTempValue).booleanValue();
                else 
                    bSorted = Boolean.getBoolean(objTempValue.toString());
            }
            
            arrColumns[i] = new ExpressionTableColumn(strColumnName, strDisplayName, strExpression, bSorted);
        }
        
        return arrColumns;
    }
}
