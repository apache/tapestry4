// Copyright Apr 21, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;


/**
 * Represents a tapestry component that delegates part of its
 * functionality / UI to a corresponding browser <a href="http://dojotoolkit.org">dojo</a>
 * widget instance.
 * 
 * @author jkuhnert
 */
public interface IWidget
{
    /**
     * Similar to the semantics involved with normal <code>AbstractComponent.renderComponent</code>
     * method writing component state to the output stream.
     * 
     * @param writer
     *          The markup writer used to write any output.
     * @param cycle
     *          The corresponding request.
     */
    void renderWidget(IMarkupWriter writer, IRequestCycle cycle);
}
