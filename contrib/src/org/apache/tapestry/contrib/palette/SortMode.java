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

package org.apache.tapestry.contrib.palette;

import org.apache.commons.lang.enum.Enum;

/**
 *  Defines different sorting strategies for the {@link Palette} component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class SortMode extends Enum
{
    /**
     *  Sorting is not relevant and no sort controls should be visible.
     *
     **/

    public static final SortMode NONE = new SortMode("NONE");

    /**
     * Options should be sorted by their label.
     *
     **/

    public static final SortMode LABEL = new SortMode("LABEL");

    /**
     *  Options should be sorted by thier value.
     *
     **/

    public static final SortMode VALUE = new SortMode("VALUE");

    /**
     *  The user controls sort order; additional controls are added
     *  to allow the user to control the order of options in the
     *  selected list.
     *
     **/

    public static final SortMode USER = new SortMode("USER");

    private SortMode(String name)
    {
        super(name);
    }

}