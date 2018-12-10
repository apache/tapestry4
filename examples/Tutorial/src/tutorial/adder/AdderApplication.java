package tutorial.adder;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.app.*;

public class AdderApplication extends SimpleApplication
{
  public AdderApplication(RequestContext context, Locale locale)
  {
    super(context, locale);
  }

  protected String getSpecificationAttributeName()
  {
    return "Adder.specification";
  }

  protected String getSpecificationResourceName()
  {
    return "/tutorial/adder/Adder.application";
  }
}
