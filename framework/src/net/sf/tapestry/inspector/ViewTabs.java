package net.sf.tapestry.inspector;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;

/**
 *  Component of the {@link Inspector} page used to select the view.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ViewTabs extends BaseComponent
{
    private static View[] views =
        {
            View.SPECIFICATION,
            View.TEMPLATE,
            View.PROPERTIES,
            View.ENGINE };

    private View view;

    public View[] getViews()
    {
        return views;
    }

    public void setView(View value)
    {
        view = value;
    }

    // We don't worry about cleaning this up at the end of the request cycle
    // because the value is an Enum, a singleton that would stay in memory
    // anyway.

    public View getView()
    {
        return view;
    }

    private IAsset getImageForView(boolean focus)
    {
        StringBuffer buffer;
        Inspector inspector;
        boolean selected;
        String key;

        inspector = (Inspector) getPage();

        selected = (view == inspector.getView());

        buffer = new StringBuffer(view.getName());

        if (selected)
            buffer.append("_selected");

        if (focus)
            buffer.append("_focus");

        key = buffer.toString();

        return (IAsset) getAssets().get(key);
    }

    public IAsset getViewImage()
    {
        return getImageForView(false);
    }

    public IAsset getFocusImage()
    {
        return getImageForView(true);
    }

    public IAsset getBannerImage()
    {
        Inspector inspector;
        View selectedView;
        String key;

        inspector = (Inspector) getPage();
        selectedView = inspector.getView();
        key = selectedView.getName() + "_banner";

        return (IAsset) getAssets().get(key);
    }

    public void selectTab(IRequestCycle cycle)
    {
        Inspector inspector;

        inspector = (Inspector) getPage();
        inspector.setView(view);
    }
}