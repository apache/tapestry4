//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
            View.ENGINE,
            View.LOGGING };

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

        buffer = new StringBuffer(view.getEnumerationId());

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
        key = selectedView.getEnumerationId() + "_banner";

        return (IAsset) getAssets().get(key);
    }

    public void selectTab(IRequestCycle cycle)
    {
        Inspector inspector;

        inspector = (Inspector) getPage();
        inspector.setView(view);
    }
}