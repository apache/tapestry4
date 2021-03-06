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
import org.apache.tapestry.engine.DirectService;


/**
 * Service used by {@link DirectService} to determine type of ajax request
 * receive and determine the appropriate {@link ResponseBuilder} to handle
 * the response.
 *
 * @author jkuhnert
 */
public interface ResponseDelegateFactory {
    
    /**
     * Creates an appropriate {@link ResponseBuilder} for the incoming 
     * ajax request, if possible.
     * 
     * @param cycle
     *          The request cycle for this request.
     * @return The configured response builder.
     */
    ResponseBuilder getResponseBuilder(IRequestCycle cycle)
    throws IOException;
}
