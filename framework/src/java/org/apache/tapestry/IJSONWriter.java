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

package org.apache.tapestry;


/**
 * JavaScript Object Notation writer interface that defines an object capable of
 * writing JSON style output. 
 * 
 * @see <a href="http://www.json.org/">http://www.json.org/</a>
 * @author jkuhnert
 */
public interface IJSONWriter
{
    /** begin json block. */
    String BEGIN = "{";
    /** end of json block. */
    String END = "}";
    
    /**
     * Writes the key value pair as a <code>key:value</code> 
     * property pair.
     * 
     * @param key 
     *          The key value of the JSON property
     * @param value
     *          The value of the key property
     */
    void write(Object key, Object value);
    
    /**
     * Begins a new block using the {@link #BEGIN} character.
     */
    void begin();
    
    /**
     * Ends any open blocks using the {@link #END} character.
     */
    void end();
    
    /**
     * Causes any un-ended blocks to be closed, as well as 
     * any reasources associated with writer to be flushed/written.
     */
    void close();
}
