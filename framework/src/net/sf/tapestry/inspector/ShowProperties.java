package net.sf.tapestry.inspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageChange;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.util.prop.IPublicBean;

/**
 *  Component of the {@link Inspector} page used to display
 *  the persisent properties of the page.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowProperties extends BaseComponent implements PageRenderListener
{
    private List _properties;
    private IPageChange _change;
    private IPage _inspectedPage;

    /**
     *  Registers this component as a {@link PageRenderListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        getPage().addPageRenderListener(this);
    }

    /**
     *  Does nothing.
     *
     *  @since 1.0.5
     *
     **/

    public void pageBeginRender(PageEvent event)
    {
    }

    /**
     *  @since 1.0.5
     *
     **/

    public void pageEndRender(PageEvent event)
    {
        _properties = null;
        _change = null;
        _inspectedPage = null;
    }

    private void buildProperties()
    {
        Inspector inspector = (Inspector) getPage();

        _inspectedPage = inspector.getInspectedPage();

        IEngine engine = getPage().getEngine();
        IPageRecorder recorder = engine.getPageRecorder(_inspectedPage.getName());

        // No page recorder?  No properties.

        if (recorder == null)
        {
            _properties = Collections.EMPTY_LIST;
            return;
        }
        
        if (recorder.getHasChanges())
            _properties = new ArrayList(recorder.getChanges());
    }

    /**
     *  Returns a {@link List} of {@link IPageChange} objects.
     *
     *  <p>Sort order is not defined.
     *
     **/

    public List getProperties()
    {
        if (_properties == null)
            buildProperties();

        return _properties;
    }

    public void setChange(IPageChange value)
    {
        _change = value;
    }

    public IPageChange getChange()
    {
        return _change;
    }

    /**
     *  Returns the name of the value's class, if the value is non-null.
     *
     **/

    public String getValueClassName()
    {
        Object value;

        value = _change.getNewValue();

        if (value == null)
            return "<null>";

        return convertClassToName(value.getClass());
    }

    private String convertClassToName(Class cl)
    {
        // TODO: This only handles one-dimensional arrays
        // property.

        if (cl.isArray())
            return "array of " + cl.getComponentType().getName();

        return cl.getName();
    }

}