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

package org.apache.tapestry.junit.mock.c14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.EnumPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

/**
 *  Page for testing the {@link org.apache.tapestry.form.ListEdit} component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class ListEdit extends BasePage
{
    public abstract Map getColorMap();
    public abstract void setColorMap(Map colorMap);

	public abstract String getColorKey();

    private IPropertySelectionModel _colorModel;

    public IPropertySelectionModel getColorModel()
    {
        if (_colorModel == null)
            _colorModel = buildColorModel();

        return _colorModel;
    }

    private IPropertySelectionModel buildColorModel()
    {
        ResourceBundle bundle =
            ResourceBundle.getBundle(Color.class.getName() + "Strings", getLocale());

        return new EnumPropertySelectionModel(Color.ALL_COLORS, bundle);
    }

    public List getSortedColorKeys()
    {
        Map map = getColorMap();
        List result = new ArrayList(map.keySet());

        Collections.sort(result);

        return result;
    }

    protected void finishLoad()
    {
        Map colorMap = new HashMap();

        colorMap.put("Food", Color.RED);
        colorMap.put("Clothing", Color.BLACK);
        colorMap.put("Eye Color", Color.BLUE);

        setColorMap(colorMap);
    }
    
    /**
     *  Had to implement these cause I couldn't remember the OGNL syntax
     *  for accessing a Map key.
     * 
     **/
    
    public void setColor(Color color)
    {
    	getColorMap().put(getColorKey(), color);	
    }
    
    public Color getColor()
    {
    	return (Color)getColorMap().get(getColorKey());
    }
    

	public void formSubmit(IRequestCycle cycle)
	{
		ListEditResults results = (ListEditResults)cycle.getPage("ListEditResults");
		
		results.setColorMap(getColorMap());
		
		cycle.activate(results);
	}

}
