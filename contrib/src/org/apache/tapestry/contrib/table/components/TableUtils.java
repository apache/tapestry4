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

package org.apache.tapestry.contrib.table.components;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.ognl.ExpressionTableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  A placeholder for a static methods related to the Table component
 *
 *  @since 3.0
 *  @version $Id$
 *  @author Mindbridge
 **/
public class TableUtils
{

    /**
     *  Contains strings loaded from TableStrings.properties.
     *
     **/

    private static ResourceBundle s_objStrings = null;

    /**
     *  Gets a string from the TableStrings resource bundle.
     *
     **/

    public static String format(String key, Object[] args)
    {
        if (s_objStrings == null) {
            synchronized (TableUtils.class) {
                if (s_objStrings == null)
                    s_objStrings = ResourceBundle.getBundle("org.apache.tapestry.contrib.table.components.TableStrings");
            }
        }

        String pattern = s_objStrings.getString(key);

        if (args == null)
            return pattern;

        return MessageFormat.format(pattern, args);
    }

    /**
     *  Convienience method for invoking {@link #format(String, Object[])}.
     **/

    public static String getMessage(String key)
    {
        return format(key, null);
    }

    /**
     *  Convienience method for invoking {@link #format(String, Object[])}.
     **/

    public static String format(String key, Object arg)
    {
        return format(key, new Object[] { arg });
    }

    /**
     *  Convienience method for invoking {@link #format(String, Object[])}.
     **/

    public static String format(String key, Object arg1, Object arg2)
    {
        return format(key, new Object[] { arg1, arg2 });
    }

    /**
     *  Convienience method for invoking {@link #format(String, Object[])}.
     **/

    public static String format(String key, Object arg1, Object arg2, Object arg3)
    {
        return format(key, new Object[] { arg1, arg2, arg3 });
    }

    /**
     *  Generate a table column model out of the description string provided.
     *  Entries in the description string are separated by commas.
     *  Each column entry is of the format name, name:expression, 
     *  or name:displayName:expression.
     *  An entry prefixed with ! represents a non-sortable column.
     *  If the whole description string is prefixed with *, it represents
     *  columns to be included in a Form. 
     * 
     *  @param strDesc the description of the column model to be generated
     *  @param objComponent the component ordering the generation
     *  @param objColumnSettingsContainer the component containing the column settings
     *  @return a table column model based on the provided parameters
     */
    public static ITableColumnModel generateTableColumnModel(String strDesc, IComponent objComponent, IComponent objColumnSettingsContainer)
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
                IResourceResolver objResolver = objColumnSettingsContainer.getPage().getEngine().getResourceResolver();

                Object objColumn =
                    OgnlUtils.get(strColumnExpression, objResolver, objColumnSettingsContainer);
                if (!(objColumn instanceof ITableColumn))
                    throw new ApplicationRuntimeException(
                        format("not-a-column", objComponent, strColumnExpression));

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

            ExpressionTableColumn objColumn =
                new ExpressionTableColumn(strName, strDisplayName, strExpression, bSortable);
            if (bFormColumns)
                objColumn.setColumnRendererSource(SimpleTableColumn.FORM_COLUMN_RENDERER_SOURCE);
            if (objColumnSettingsContainer != null)
                objColumn.loadSettings(objColumnSettingsContainer);

            arrColumns.add(objColumn);
        }

        return new SimpleTableColumnModel(arrColumns);
    }

}
