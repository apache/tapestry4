/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
