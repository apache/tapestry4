package tutorial.simple;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.app.*;

public class SimpleTutorialApplication extends SimpleApplication
{
  public SimpleTutorialApplication(RequestContext context, Locale locale)
  {
    super(context, locale);
  }
  
  protected String getSpecificationAttributeName()
  {
    return "Simple.specification";
  }
  
  protected String getSpecificationResourceName()
  {
    return "/tutorial/simple/Simple.application";
  }
}
