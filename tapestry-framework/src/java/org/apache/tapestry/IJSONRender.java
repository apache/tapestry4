// Copyright Mar 18, 2006 The Apache Software Foundation
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

import org.apache.tapestry.json.IJSONWriter;


/**
 * Defines a component/page/class that is capable of rendering json
 * responses via {@link IJSONWriter} writers.
 * 
 * @author jkuhnert
 */
public interface IJSONRender
{

    /**
     * Renders a JSON object response back to the client. It should be assumed that
     * if this method is called on a component it or one of the components it contains
     * has been requested explicitly for rendering.
     * 
     * @param writer
     *          The json object writer used to write valid JSON responses.
     * @param cycle
     *          Contextual request object.
     */
    void renderComponent(IJSONWriter writer, IRequestCycle cycle);
}
