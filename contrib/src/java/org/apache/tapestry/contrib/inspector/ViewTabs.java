//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.inspector;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;

/**
 *  Component of the {@link Inspector} page used to select the view.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class ViewTabs extends BaseComponent
{
    private static View[] _views =
        {
            View.SPECIFICATION,
            View.TEMPLATE,
            View.PROPERTIES,
            View.ENGINE };

     public View[] getViews()
    {
        return _views;
    }

    public abstract void setView(View value);

    public abstract View getView();

    private IAsset getImageForView(boolean focus)
    {
        Inspector inspector = (Inspector) getPage();
		View view = getView();
		
        boolean selected = (view == inspector.getView());

        StringBuffer buffer = new StringBuffer(view.getName());

        if (selected)
            buffer.append("_selected");

        if (focus)
            buffer.append("_focus");

        String key = buffer.toString();

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
         Inspector inspector = (Inspector) getPage();
        View selectedView = inspector.getView();
        String key = selectedView.getName() + "_banner";

        return (IAsset) getAssets().get(key);
    }

    public void selectTab(IRequestCycle cycle)
    {
        Inspector inspector = (Inspector) getPage();
        inspector.setView(getView());
    }
}