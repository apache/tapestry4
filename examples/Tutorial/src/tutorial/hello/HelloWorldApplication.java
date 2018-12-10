package tutorial.hello;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.app.*;

public class HelloWorldApplication extends SimpleApplication
{
  public HelloWorldApplication(RequestContext context, Locale locale)
  {
    super(context, locale);
  }
  
  protected String getSpecificationAttributeName()
  {
    return "Hello.specification";
  }
  
  protected String getSpecificationResourceName()
  {
    return "/tutorial/hello/HelloWorld.application";
  }
}
