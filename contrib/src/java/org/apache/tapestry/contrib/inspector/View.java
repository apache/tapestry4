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

package org.apache.tapestry.contrib.inspector;

/**
 * Identifies different views for the inspector.
 * 
 * @author Howard Lewis Ship
 */

public class View
{
    /**
     * View that displays the basic specification information, plus formal and informal parameters
     * (and related bindings), and assets.
     */

    public static final String SPECIFICATION = "SPECIFICATION";

    /**
     * View that displays the HTML template for the component, if one exists.
     */

    public static final String TEMPLATE = "TEMPLATE";

    /**
     * View that shows the persistent properties of the page containing the inspected component.
     */

    public static final String PROPERTIES = "PROPERTIES";

    /**
     * View that shows information about the {@link org.apache.tapestry.IEngine}.
     */

    public static final String ENGINE = "ENGINE";
}