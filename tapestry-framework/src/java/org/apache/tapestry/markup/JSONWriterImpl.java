// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry.markup;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.json.JSONArray;
import org.apache.tapestry.json.JSONObject;

/**
 * Implementation of {@link IIJSONWriter}.
 * 
 * @author jkuhnert
 */
public class JSONWriterImpl implements IJSONWriter
{   
    /** Outputstream writer. */
    protected PrintWriter _writer;
    
    /**
     * Delegate object that handles all json logic.
     */
    private JSONObject _json;
    
    /**
     * Creates a new instance that will write all content to 
     * the specified {@link PrintWriter}.
     * 
     * @param writer The outputstream to write to.
     */
    public JSONWriterImpl(PrintWriter writer)
    {
        Defense.notNull(writer, "writer");
        
        _writer = writer;
        _json = new JSONObject();
    }
    
    /** 
     * {@inheritDoc}
     */
    public IJSONWriter accumulate(String key, Object value)
    {
        _json.accumulate(key, value);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean equals(Object o)
    {
        return _json.equals(o);
    }

    /** 
     * {@inheritDoc}
     */
    public Object get(String key)
    {
        return _json.get(key);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean getBoolean(String key)
    {
        return _json.getBoolean(key);
    }

    /** 
     * {@inheritDoc}
     */
    public double getDouble(String key)
    {
        return _json.getDouble(key);
    }

    /** 
     * {@inheritDoc}
     */
    public int getInt(String key)
    {
        return _json.getInt(key);
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray getJSONArray(String key)
    {
        return _json.getJSONArray(key);
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter getJSONObject(String key)
    {
        _json.getJSONObject(key);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public String getString(String key)
    {
        return _json.getString(key);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean has(String key)
    {
        return _json.has(key);
    }

    /** 
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return _json.hashCode();
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isNull(String key)
    {
        return _json.isNull(key);
    }

    /** 
     * {@inheritDoc}
     */
    public Iterator keys()
    {
        return _json.keys();
    }

    /** 
     * {@inheritDoc}
     */
    public int length()
    {
        return _json.length();
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray names()
    {
        return _json.names();
    }

    /** 
     * {@inheritDoc}
     */
    public Object opt(String key)
    {
        return _json.opt(key);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean optBoolean(String key, boolean defaultValue)
    {
        return _json.optBoolean(key, defaultValue);
    }

    /** 
     * {@inheritDoc}
     */
    public boolean optBoolean(String key)
    {
        return _json.optBoolean(key);
    }

    /** 
     * {@inheritDoc}
     */
    public double optDouble(String key, double defaultValue)
    {
        return _json.optDouble(key, defaultValue);
    }

    /** 
     * {@inheritDoc}
     */
    public double optDouble(String key)
    {
        return _json.optDouble(key);
    }

    /** 
     * {@inheritDoc}
     */
    public int optInt(String key, int defaultValue)
    {
        return _json.optInt(key, defaultValue);
    }

    /** 
     * {@inheritDoc}
     */
    public int optInt(String key)
    {
        return _json.optInt(key);
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray optJSONArray(String key)
    {
        return _json.optJSONArray(key);
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter optJSONObject(String key)
    {
        _json.optJSONObject(key);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public String optString(String key, String defaultValue)
    {
        return _json.optString(key, defaultValue);
    }

    /** 
     * {@inheritDoc}
     */
    public String optString(String key)
    {
        return _json.optString(key);
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter put(String key, boolean value)
    {
        _json.put(key, value);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter put(String key, double value)
    {
        _json.put(key, value);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter put(String key, int value)
    {
        _json.put(key, value);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter put(String key, Object value)
    {
        _json.put(key, value);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public IJSONWriter putOpt(String key, Object value)
    {
        _json.putOpt(key, value);
        return this;
    }

    /** 
     * {@inheritDoc}
     */
    public Object remove(String key)
    {
        return _json.remove(key);
    }

    /** 
     * {@inheritDoc}
     */
    public JSONArray toJSONArray(JSONArray names)
    {
        return _json.toJSONArray(names);
    }

    /** 
     * {@inheritDoc}
     */
    public String toString()
    {
        return _json.toString();
    }

    /** 
     * {@inheritDoc}
     */
    public String toString(int indentFactor)
    {
        return _json.toString(indentFactor);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void close()
    {
        _writer.write(_json.toString());
        
        _writer.flush();
        _writer.close();
    }
    
    /**
     * {@inheritDoc}
     */
    public JSONObject getJSONSource()
    {
        return _json;
    }
    
    /**
     * The outputstream being used to write this 
     * instance's content.
     * @return 
     */
    protected PrintWriter getWriter()
    {
        return _writer;
    }
}
