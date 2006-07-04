// Copyright May 20, 2006 The Apache Software Foundation
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
package org.apache.tapestry.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;


/**
 * Represents a client side generated browser event.
 * 
 * @author jkuhnert
 */
public class BrowserEvent
{
    public static final String NAME="beventname";
    public static final String TYPE="beventtype";
    public static final String KEYS="beventkeys";
    public static final String CHAR_CODE="beventcharCode";
    public static final String PAGE_X="beventpageX";
    public static final String PAGE_Y="beventpageY";
    public static final String LAYER_X="beventlayerX";
    public static final String LAYER_Y="beventlayerY";
    
    public static final String TARGET="beventtarget";
    public static final String TARGET_ATTR_ID="id";
    
    private String _name;
    private String _type;
    private String[] _keys;
    private String _charCode;
    private String _pageX;
    private String _pageY;
    private String _layerX;
    private String _layerY;
    private EventTarget _target;
    
    /**
     * Creates a new browser event that will extract its own
     * parameters.
     * 
     * @param cycle
     *          The request cycle to extract parameters from.
     */
    public BrowserEvent(IRequestCycle cycle)
    {
        Defense.notNull(cycle, "cycle");
        
        _name = cycle.getParameter(NAME);
        _type = cycle.getParameter(TYPE);
        _keys = cycle.getParameters(KEYS);
        _charCode = cycle.getParameter(CHAR_CODE);
        _pageX = cycle.getParameter(PAGE_X);
        _pageY = cycle.getParameter(PAGE_Y);
        _layerX = cycle.getParameter(LAYER_X);
        _layerY = cycle.getParameter(LAYER_Y);
        
        Map props = new HashMap();
        _target = new EventTarget(props);
        
        String targetId = cycle.getParameter(TARGET + "." + TARGET_ATTR_ID);
        if (targetId != null) {
            props.put(TARGET_ATTR_ID, targetId);
        }
    }
    
    /**
     * Creates a new browser event with the specified
     * name/target properties.
     * 
     * @param name The name of the event, ie "onClick", "onBlur", etc..
     * @param target The target of the client side event.
     */
    public BrowserEvent(String name, EventTarget target)
    {
        _name = name;
        _target = target;
    }
    
    /**
     * The name of the event that was generated. 
     * 
     * <p>
     * Examples would be <code>onClick,onSelect,onLoad,etc...</code>.
     * </p>
     * @return The event name.
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Returns the target of the client side event.
     * @return
     */
    public EventTarget getTarget()
    {
        return _target;
    }
    
    /**
     * @return the charCode
     */
    public String getCharCode()
    {
        return _charCode;
    }
    
    /**
     * @return the keys
     */
    public String[] getKeys()
    {
        return _keys;
    }

    /**
     * @return the layerX
     */
    public String getLayerX()
    {
        return _layerX;
    }

    /**
     * @return the layerY
     */
    public String getLayerY()
    {
        return _layerY;
    }

    /**
     * @return the pageX
     */
    public String getPageX()
    {
        return _pageX;
    }

    /**
     * @return the pageY
     */
    public String getPageY()
    {
        return _pageY;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return _type;
    }

    /**
     * Utility method to check if the current request contains
     * a browser event.
     * @param cycle
     *          The associated request.
     * @return True if the request contains browser event data.
     */
    public static boolean hasBrowserEvent(IRequestCycle cycle)
    {
        Defense.notNull(cycle, "cycle");
        
        return cycle.getParameter(NAME) != null;
    }
}
