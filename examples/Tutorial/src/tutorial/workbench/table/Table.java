package tutorial.workbench.table;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;
import net.sf.tapestry.contrib.table.model.ognl.ExpressionTableColumnModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleListTableDataModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleSetTableDataModel;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableSessionStateManager;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableState;
import net.sf.tapestry.html.BasePage;

/**
 * @author mindbridge
 *
 */
public class Table extends BasePage
{
    private SimpleSetTableDataModel m_objLocaleDataModel;
    
    public Table() 
    {
        m_objLocaleDataModel = new SimpleSetTableDataModel(new HashSet());
    }

    public ILocaleSelectionListener getLocaleSelectionListener()
    {
        return (ILocaleSelectionListener) getComponent("localeSelection");
    }

}
