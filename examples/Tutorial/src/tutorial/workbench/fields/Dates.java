package tutorial.workbench.fields;

import java.util.Date;

import net.sf.tapestry.html.BasePage;

/**
 *  Page to demonstrate DateEdit form component.
 *
 *  @author Malcolm Edgar
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class Dates extends BasePage
{
    private Date _startDate;
    private Date _endDate;
 
    public void detach()
    {
        _startDate = null;
        _endDate = null;       
        super.detach();
    }   
    
    public Date getStartDate()
    {
        return _startDate;
    }

    public void setStartDate(Date date)
    {
        _startDate = date;
    }
    
    public Date getEndDate()
    {
        return _endDate;
    }

    public void setEndDate(Date date)
    {
        _endDate = date;
    }   
}
