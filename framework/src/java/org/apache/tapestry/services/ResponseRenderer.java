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

import javax.servlet.ServletException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 * Responsible for rendering the active page as the response.  Works with
 * the {@link org.apache.tapestry.services.RequestLocaleManager}
 * to persist any change to the selected locale.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ResponseRenderer
{
    /**
     * Renders the reponse, using the current active page defined by the request cycle.
     */

    public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
            throws ServletException, IOException;

}