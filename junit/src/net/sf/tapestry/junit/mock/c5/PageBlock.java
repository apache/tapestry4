package net.sf.tapestry.junit.mock.c5;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Block;

/**
 *  An actual, reusable kind of component for the test suite.  Works like
 *  a {@link net.sf.tapestry.components.RenderBlock}, but specified the 
 *  {@link net.sf.tapestry.components.Block} to render in terms of
 *  a page name and a nested component id.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class PageBlock extends BaseComponent
{
    private String _targetPageName;
    private String _targetBlockId = "block";
    
    public String getTargetBlockId()
    {
        return _targetBlockId;
    }

    public String getTargetPageName()
    {
        return _targetPageName;
    }

    public void setTargetBlockId(String targetBlockId)
    {
        _targetBlockId = targetBlockId;
    }

    public void setTargetPageName(String targetPageName)
    {
        _targetPageName = targetPageName;
    }

    /**
     *  Used the targetPageName and targetBlockId (which defaults to "block")
     *  to obtain a reference to a Block instance, which is returned.
     * 
     **/
    
    public Block getTargetBlock()
    {
        IRequestCycle cycle = getPage().getRequestCycle();
        IPage targetPage = cycle.getPage(_targetPageName);
        
        return (Block)targetPage.getNestedComponent(_targetBlockId);        
    }
}
