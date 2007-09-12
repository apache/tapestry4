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

package org.apache.tapestry.parse;

import org.apache.hivemind.Location;

/**
 * Provides a {@link TemplateParser}with additional information about dynamic components.
 * 
 * @author Howard Lewis Ship
 */

public interface ITemplateParserDelegate
{
    /**
     * Returns true if the component id is valid, false if the component id is not recognized.
     */

    boolean getKnownComponent(String componentId);

    /**
     * Returns true if the specified component allows a body, false otherwise. The parser uses this
     * information to determine if it should ignore the body of a tag.
     */

    boolean getAllowBody(String componentId, Location location);

    /**
     * Used with implicit components to determine if the component allows a body or not.
     * 
     * @param libraryId
     *            the specified library id, possibly null
     * @param type
     *            the component type
     * @since 3.0
     */

    boolean getAllowBody(String libraryId, String type, Location location);

    /**
     * Returns the name of the attribute used to indicate a component. The default is "jwcid", but
     * this can be overriden in a number of ways.
     */

    String getComponentAttributeName();
}
