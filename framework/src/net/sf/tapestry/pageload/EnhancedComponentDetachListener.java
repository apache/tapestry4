package net.sf.tapestry.pageload;

import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;

/**
 * @author mindbridge
 *
 */
public class EnhancedComponentDetachListener implements PageDetachListener
{
    private IEnhancedComponent _enhancedComponent;
    
    public EnhancedComponentDetachListener(IEnhancedComponent enhancedComponent)
    {
        _enhancedComponent = enhancedComponent;
    }
    
    /**
	 * @see net.sf.tapestry.event.PageDetachListener#pageDetached(PageEvent)
	 */
	public void pageDetached(PageEvent event)
	{
        _enhancedComponent.resetParametersCache();
	}

}
