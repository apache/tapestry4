package net.sf.tapestry.contrib.table.example.minimal;

import java.util.Locale;

import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ognl.ExpressionTableColumnModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableModel;
import net.sf.tapestry.html.BasePage;

/**
 * @author mindbridge
 * @version $Id$
 *
 */
public class Home extends BasePage
{
    // Return the model of the table
	public ITableModel getTableModel()
	{
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
	}
}
