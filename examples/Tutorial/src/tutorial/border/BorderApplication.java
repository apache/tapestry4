package tutorial.border;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.app.*;

public class BorderApplication extends SimpleApplication
{
  private static final String[] pageNames =
    { "home", "credo", "legal" };
  
  public BorderApplication(RequestContext context, Locale locale)
  {
    super(context, locale);
  }

  protected String getSpecificationAttributeName()
  {
    return "Border.specification";
  }

  protected String getSpecificationResourceName()
  {
    return "/tutorial/border/Border.application";
  }
  
  public String[] getPageNames()
  {
    return pageNames;
  }
  
}
