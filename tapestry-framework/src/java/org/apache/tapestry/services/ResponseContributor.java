// Copyright 2004, 2005 The Apache Software Foundation
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
package org.apache.tapestry.services;

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;


/**
 * Determines if the particular incoming ajax request is handled by this
 * contributor/library response type. If it is will also provide an instance
 * of the appropriate ResponseBuilder.
 *
 * @author jkuhnert
 */
public interface ResponseContributor {
    
    /**
     * Determines if the incoming ajax request is capable of being
     * handled by the {@link ResponseBuilder} this contributor
     * manages.
     * 
     * @param cycle 
     *          Main request cycle for this request.
     * @return True if can handle request, false otherwise.
     */
    boolean handlesResponse(IRequestCycle cycle);
    
    /**
     * Creates the appropriate {@link ResponseBuilder} instance to handle the
     * incoming ajax request.
     * 
     * @param cycle
     *          The incoming request cycle for this request.
     * @return
     */
    ResponseBuilder createBuilder(IRequestCycle cycle)
    throws IOException;
}
