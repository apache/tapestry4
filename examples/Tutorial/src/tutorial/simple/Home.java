package tutorial.simple;

import java.util.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;

public class Home extends BasePage
{
  public Home(IApplication application, 
              ComponentSpecification componentSpecification)
  {
    super(application, componentSpecification);
  }
  
  public Date getCurrentDate()
  {
    return new Date();
  }
}
