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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.json.JSONArray;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents a client side generated browser event.
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
    public static final String COMPONENT_ID = "bcomponentid";

    public static final String METHOD_ARGUMENTS="methodArguments";

    private String _name;
    private String _type;
    private String[] _keys;
    private String _charCode;
    private String _pageX;
    private String _pageY;
    private String _layerX;
    private String _layerY;
    private EventTarget _target;
    private String _componentId;
    
    private String _methodArguments;
    private JSONArray _methodArgumentsArray;

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
        _componentId = cycle.getParameter(COMPONENT_ID);

        Map props = new HashMap();
        _target = new EventTarget(props);

        String targetId = cycle.getParameter(TARGET + "." + TARGET_ATTR_ID);
        if (targetId != null)
        {
            props.put(TARGET_ATTR_ID, targetId);
        }

        _methodArguments = cycle.getParameter(METHOD_ARGUMENTS);
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
        this(name, null, target);
    }

    /**
     * Creates a new browser event with the specified
     * name/target properties.
     *
     * @param name The name of the event, ie "onClick", "onBlur", etc..
     * @param componentId Component targeted.
     * @param target The target of the client side event.
     */
    public BrowserEvent(String name, String componentId, EventTarget target)
    {
        _name = name;
        _target = target;
        _componentId = componentId;
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
     *
     * @return The target representation of the client side object event originally bound for.
     */
    public EventTarget getTarget()
    {
        return _target;
    }

    /**
     * Only when the event targeted a {@link org.apache.tapestry.IComponent} - will return the originating
     * components id as returned from {@link org.apache.tapestry.IComponent#getId()}.  <em>Not</em> present
     * on element events.
     *
     * @return The originating component id that generated the event.
     */
    public String getComponentId()
    {
        return _componentId;
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
     * @return the method arguments of an intercepted method-call, if any. If none
     *         are available, return an empty JSONArray, never null.
     *
     * @throws ApplicationRuntimeException when the JSON-String could not be
     *         parsed.
     */
    public JSONArray getMethodArguments()
    {
        if ( _methodArgumentsArray == null)
        {
            try
            {
                _methodArgumentsArray = _methodArguments != null
                                        ? new JSONArray( _methodArguments )
                                        : new JSONArray();
            }
            catch (ParseException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
        }
        
        return _methodArgumentsArray;
    }

    /**
     * Utility method to check if the current request contains
     * a browser event.
     *
     * @param cycle
     *          The associated request.
     * @return True if the request contains browser event data.
     */
    public static boolean hasBrowserEvent(IRequestCycle cycle)
    {
        Defense.notNull(cycle, "cycle");

        return cycle.getParameter(NAME) != null;
    }

    public String toString()
    {
        return "BrowserEvent[" +
               "_name='" + _name + '\'' +
               '\n' +
               ", _type='" + _type + '\'' +
               '\n' +
               ", _keys=" + (_keys == null ? null : Arrays.asList(_keys)) +
               '\n' +
               ", _charCode='" + _charCode + '\'' +
               '\n' +
               ", _pageX='" + _pageX + '\'' +
               '\n' +
               ", _pageY='" + _pageY + '\'' +
               '\n' +
               ", _layerX='" + _layerX + '\'' +
               '\n' +
               ", _layerY='" + _layerY + '\'' +
               '\n' +
               ", _target=" + _target +
               '\n' +
               ", _methodArguments='" + _methodArguments + '\'' +
               '\n' +
               ", _methodArgumentsArray=" + _methodArgumentsArray +
               '\n' +
               ']';
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrowserEvent event = (BrowserEvent) o;

        if (_charCode != null ? !_charCode.equals(event._charCode) : event._charCode != null) return false;
        if (!Arrays.equals(_keys, event._keys)) return false;
        if (_layerX != null ? !_layerX.equals(event._layerX) : event._layerX != null) return false;
        if (_layerY != null ? !_layerY.equals(event._layerY) : event._layerY != null) return false;
        if (_methodArguments != null ? !_methodArguments.equals(event._methodArguments) : event._methodArguments != null) return false;
        if (_methodArgumentsArray != null ? !_methodArgumentsArray.equals(event._methodArgumentsArray) : event._methodArgumentsArray != null) return false;
        if (_name != null ? !_name.equals(event._name) : event._name != null) return false;
        if (_pageX != null ? !_pageX.equals(event._pageX) : event._pageX != null) return false;
        if (_pageY != null ? !_pageY.equals(event._pageY) : event._pageY != null) return false;
        if (_target != null ? !_target.equals(event._target) : event._target != null) return false;
        if (_type != null ? !_type.equals(event._type) : event._type != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (_name != null ? _name.hashCode() : 0);
        result = 31 * result + (_type != null ? _type.hashCode() : 0);
        result = 31 * result + (_keys != null ? Arrays.hashCode(_keys) : 0);
        result = 31 * result + (_charCode != null ? _charCode.hashCode() : 0);
        result = 31 * result + (_pageX != null ? _pageX.hashCode() : 0);
        result = 31 * result + (_pageY != null ? _pageY.hashCode() : 0);
        result = 31 * result + (_layerX != null ? _layerX.hashCode() : 0);
        result = 31 * result + (_layerY != null ? _layerY.hashCode() : 0);
        result = 31 * result + (_target != null ? _target.hashCode() : 0);
        result = 31 * result + (_methodArguments != null ? _methodArguments.hashCode() : 0);
        result = 31 * result + (_methodArgumentsArray != null ? _methodArgumentsArray.hashCode() : 0);
        return result;
    }
}
