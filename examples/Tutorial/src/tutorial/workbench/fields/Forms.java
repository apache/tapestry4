package tutorial.workbench.fields;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  PageLink to demonstrate various form element components.
 *
 *  @author Howard Lewis Ship
 *  @version Forms.java,v 1.1 2002/08/23 22:18:31 hship Exp
 *  @since 2.2
 * 
 **/

public class Forms extends BasePage
{
    private String _favoriteColor;
 
    public void detach()
    {
        _favoriteColor = null;
        
        super.detach();
    }   
    
    public String getFavoriteColor()
    {
        return _favoriteColor;
    }

    public void setFavoriteColor(String favoriteColor)
    {
        _favoriteColor = favoriteColor;
    }

    public void formSubmit(IRequestCycle cycle)
    {
        // Do nothing, redisplay this page.
    }

}
