package tutorial.workbench.table;

import java.util.HashSet;

import net.sf.tapestry.contrib.table.model.simple.SimpleSetTableDataModel;
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
