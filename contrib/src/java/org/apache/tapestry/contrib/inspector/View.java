//  Copyright 2004 The Apache Software Foundation
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

import org.apache.commons.lang.enum.Enum;

/**
 *  Identifies different views for the inspector.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class View extends Enum
{
    /**
     *  View that displays the basic specification information, plus
     *  formal and informal parameters (and related bindings), and 
     *  assets.
     *
     **/

    public static final View SPECIFICATION = new View("SPECIFICATION");

    /**
     *  View that displays the HTML template for the component, if one
     *  exists.
     *
     **/

    public static final View TEMPLATE = new View("TEMPLATE");

    /**
     *  View that shows the persistent properties of the page containing
     *  the inspected component.
     *
     **/

    public static final View PROPERTIES = new View("PROPERTIES");

    /**
     *  View that shows information about the 
     *  {@link org.apache.tapestry.IEngine}.
     *
     **/

    public static final View ENGINE = new View("ENGINE");


    private View(String name)
    {
        super(name);
    }

}