//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.table.example.minimal;

import java.util.Locale;

import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ognl.ExpressionTableColumnModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleListTableDataModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableModel;
import net.sf.tapestry.contrib.table.model.sql.ISqlConnectionSource;
import net.sf.tapestry.contrib.table.model.sql.ISqlTableDataSource;
import net.sf.tapestry.contrib.table.model.sql.SimpleSqlConnectionSource;
import net.sf.tapestry.contrib.table.model.sql.SimpleSqlTableDataSource;
import net.sf.tapestry.contrib.table.model.sql.SqlTableColumn;
import net.sf.tapestry.contrib.table.model.sql.SqlTableColumnModel;
import net.sf.tapestry.contrib.table.model.sql.SqlTableModel;
import net.sf.tapestry.html.BasePage;

/**
 * @author mindbridge
 * @version $Id$
 *
 */
public class Home extends BasePage
{
    public Home()
    {
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
            // can't happen, can it?
		}
    }
    
    // Return the model of the table
	public ITableModel getTableModel()
	{
        /*
		// Generate the list of data
		Locale[] arrLocales = Locale.getAvailableLocales();
        
        // Generate a simple sorting column model that uses OGNL to get the column data.
        // The columns are defined using pairs of strings.
        // The first string in the pair is the column name.
        // The second is an OGNL expression used to obtain the column value.
        ITableColumnModel objColumnModel = 
            new ExpressionTableColumnModel(new String[] {
                "Locale", "toString()",
                "Language", "language",
                "Country", "country",
                "Variant", "variant",
                "ISO Language", "ISO3Language",
                "ISO Country", "ISO3Country"
            }, true);

		// Create the table model and return it
		return new SimpleTableModel(arrLocales, objColumnModel);
        */
        
        ISqlConnectionSource objConnSrc = 
            new SimpleSqlConnectionSource("jdbc:postgresql://localhost/testdb", "testdb", "testdb");
            
        ISqlTableDataSource objDataSrc = 
            new SimpleSqlTableDataSource(objConnSrc, "test_table");
            
        SqlTableColumnModel objColumnModel = 
            new SqlTableColumnModel(new SqlTableColumn[] {
                new SqlTableColumn("language", "Language", true),
                new SqlTableColumn("country", "Country", true),
                new SqlTableColumn("variant", "Variant", true),
                new SqlTableColumn("intvalue", "Integer", true),
                new SqlTableColumn("floatvalue", "Float", true)
            });
        
        ITableModel objTableModel = new SqlTableModel(objDataSrc, objColumnModel);
        
        return objTableModel;
	}
}
