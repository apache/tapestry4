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

package org.apache.tapestry.json;


/**
 * JavaScript Object Notation writer which manages two core object response 
 * types, {@link JSONObject} or {@link JSONArray}. It is up to the components
 * participating in a particular response to decide how to cooperate and build
 * a JSON structure that their client side will accept.
 * 
 * @see "http://www.json.org/"
 * @author JSON.org, jkuhnert
 */
public interface IJSONWriter
{   
    /**
     * Provides access to the core outer {@link JSONObject} being 
     * rendered to a response. The object may not necessarily be instantiated
     * until requested, for instances where a response should be a pure array
     * or other.
     * 
     * @return The {@link JSONObject} being delegated to.
     */
    JSONObject object();
    
    /**
     * Provides access to the core outer {@link JSONArray} being 
     * rendered to a response. The object may not necessarily be instantiated
     * until requested.
     * 
     * @return The {@link JSONArray} being delegated to.
     */
    JSONArray array();
    
    /**
     * Causes any un-ended blocks to be closed, as well as 
     * any reasources associated with writer to be flushed/written.
     */
    void close();
    
    /**
     * Forwards <code>flush()</code> to this <code>IMarkupWriter</code>'s
     * <code>PrintWriter</code>.
     */
    void flush();
}
