// Copyright Aug 27, 2006 The Apache Software Foundation
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

import java.util.Collection;


/**
 * Represents "something" that can cause dynamic XHR/JSON requests to be executed
 * as a result of whatever actions the thing that it is attached to normally 
 * does.
 * 
 * <p>
 * This interface is more commonly implemented by components like {@link DirectLink} or
 * {@Form} to decorate existing functionality.
 * </p>
 * 
 * @author jkuhnert
 */
public interface IDynamicInvoker
{
    
    /**
     * If set, will be used to update/refresh portions of a response during XHR requests.
     * 
     * <p>
     *  For instance, if you have a page listing projects and you wanted to update an 
     *  {@link Any} components contents whenever one of the project links was clicked 
     *  you would use a {@link DirectLink} with the parameters:
     * </p>
     * 
     * <pre>
     *      updateComponents="{'projectDetails'}"
     *      async="true"
     * </pre>
     * 
     * @return The list of components to update, if any.
     */
    Collection getUpdateComponents();
    
    /**
     * Used to specify whether or not the result of this invocation should be returned asynchronously
     * or use normal browser page reload semantics. 
     * 
     * <p>
     *  Async being true means responses will be encoded as XML using XmlHttpRequests. If you would like
     *  your request/response to be in another format - like JSON - you must also specify the additional 
     *  parameter {@link #isJson()}.  Without setting the {@link #getUpdateComponents()} parameter
     * this parameter is pretty useless.
     * </p>
     * 
     * @see #isJson()
     * @return True if invocation should be processed asynchronously.
     */
    boolean isAsync();
    
    /**
     * Used to specify that the return invocation of the response created should be in the
     * {@linkplain http://json.org} format. Without setting the {@link #getUpdateComponents()} parameter
     * this parameter is pretty useless.
     * 
     * @see {@link org.apache.IJSONRender}
     * @return True if response should be encoded using JSON semantics.
     */
    boolean isJson();
}
