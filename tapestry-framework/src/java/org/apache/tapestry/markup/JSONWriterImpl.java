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

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.json.JSONArray;
import org.apache.tapestry.json.JSONObject;

/**
 * Implementation of {@link IJSONWriter}.
 * 
 * @author jkuhnert
 */
public class JSONWriterImpl implements IJSONWriter
{   
    /** Outputstream writer. */
    protected PrintWriter _writer;
    
    /**
     * Delegate object that handles object json renders.
     */
    private JSONObject _json;
    
    /**
     * Delegate array object that handles object array json renders.
     */
    private JSONArray _array;
    
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
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void close()
    {
        if (_json != null && _json.length() > 0) {
            
            _writer.write(_json.toString());
        }
        
        if (_array != null && _array.length() > 0) {
            
            _writer.write(_array.toString());
        }
        
        _writer.flush();
        _writer.close();
    }
    
    /**
     * {@inheritDoc}
     */
    public JSONObject object()
    {
        if (_json == null)
            _json = new JSONObject();
        
        return _json;
    }
    
    /**
     * {@inheritDoc}
     */
    public JSONArray array()
    {
        if (_array == null)
            _array = new JSONArray();
        
        return _array;
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
