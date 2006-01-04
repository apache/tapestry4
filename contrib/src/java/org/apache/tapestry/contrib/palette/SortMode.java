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

package org.apache.tapestry.contrib.palette;

/**
 * Defines different sorting strategies for the {@link Palette} component.
 * 
 * @author Howard Lewis Ship
 */

public class SortMode
{
    /**
     * Sorting is not relevant and no sort controls should be visible.
     */

    public static final String NONE = "NONE";

    /**
     * Options should be sorted by their label.
     */

    public static final String LABEL = "LABEL";

    /**
     * Options should be sorted by thier value.
     */

    public static final String VALUE = "VALUE";

    /**
     * The user controls sort order; additional controls are added to allow the user to control the
     * order of options in the selected list.
     */

    public static final String USER = "USER";

}