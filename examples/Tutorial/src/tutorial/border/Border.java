package tutorial.border;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;

public class Border extends BaseComponent
{
    private String pageName;
  
    public Border(IPage page, IComponent container,
                  String id, ComponentSpecification specification)
    {
        super(page, container, id, specification);
    }
  
    public void setPageName(String value)
    {
        pageName = value;
    }
  
    public String getPageName()
    {
        return pageName;
    }
  
    public boolean getEnablePageLink()
    {
        return !pageName.equals(page.getName());
    }
}
