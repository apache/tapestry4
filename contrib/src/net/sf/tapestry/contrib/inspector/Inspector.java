package net.sf.tapestry.contrib.inspector;

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.components.Block;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  The Tapestry Inspector page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class Inspector extends BasePage
{
    private View _view = View.SPECIFICATION;
    private String _inspectedPageName;
    private String _inspectedIdPath;

    private static final int MAP_SIZE = 7;

    private Map _blocks;

    public void detach()
    {
        _view = View.SPECIFICATION;
        _inspectedPageName = null;
        _inspectedIdPath = null;

        super.detach();
    }

    public void finishLoad()
    {
        _blocks = new HashMap(MAP_SIZE);

        _blocks.put(View.TEMPLATE, getComponent("templateBlock"));
        _blocks.put(View.SPECIFICATION, getComponent("specificationBlock"));
        _blocks.put(View.ENGINE, getComponent("engineBlock"));
        _blocks.put(View.PROPERTIES, getComponent("propertiesBlock"));
    }

    public View getView()
    {
        return _view;
    }

    public void setView(View value)
    {
        _view = value;

        fireObservedChange("view", value);
    }

    public String getInspectedPageName()
    {
        return _inspectedPageName;
    }

    public void setInspectedPageName(String value)
    {
        _inspectedPageName = value;

        fireObservedChange("inspectedPageName", value);
    }

    public String getInspectedIdPath()
    {
        return _inspectedIdPath;
    }

    public void setInspectedIdPath(String value)
    {
        _inspectedIdPath = value;

        fireObservedChange("inspectedIdPath", value);
    }

    /** 
     *  Invoked to change the component being inspected within the current
     *  page.
     *
     *  @since 1.0.6
     **/

    public void selectComponent(String idPath)
    {
        setInspectedIdPath(idPath);
    }

    /**
     *  Method invoked by the {@link InspectorButton} component, 
     *  to begin inspecting a page.
     *
     **/

    public void inspect(String pageName, IRequestCycle cycle)
    {
        setInspectedPageName(pageName);
        selectComponent((String) null);

        cycle.setPage(this);
    }

    /**
     *  Listener for the component selection, which allows a particular component.  
     *  
     *  <p>The context is a single string,
     *  the id path of the component to be selected (or null to inspect
     *  the page itself).  This invokes
     *  {@link #selectComponent(String)}.
     *
     **/

    public void selectComponent(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();

        String newIdPath;

        // The up button may generate a null context.

        if (parameters == null)
            newIdPath = null;
        else
            newIdPath = (String) parameters[0];

        selectComponent(newIdPath);
    }

    /**
     *  Returns the {@link IPage} currently inspected by the Inspector, as determined
     *  from the inspectedPageName property.
     *
     **/

    public IPage getInspectedPage()
    {
        return getRequestCycle().getPage(_inspectedPageName);
    }

    /**
     *  Returns the {@link IComponent} current inspected; this is determined
     *  from the inspectedPageName and inspectedIdPath properties.
     *
     **/

    public IComponent getInspectedComponent()
    {
        return getInspectedPage().getNestedComponent(_inspectedIdPath);
    }

    public String getInspectorTitle()
    {
        return "Tapestry Inspector: " + getEngine().getSpecification().getName();
    }


    /**
     *  Returns the {@link Block} for the currently selected view.
     *
     **/

    public Block getBlockForView()
    {
        return (Block) _blocks.get(_view);
    }
}