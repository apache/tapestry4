// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.vlib.ejb;

/**
 * Represents the different columns which may be sorted.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public enum SortColumn {

    /**
     * Sort by book title.
     */

    TITLE,

    /**
     * Sort by author name.
     */

    AUTHOR,

    /**
     * Sort by publisher name.
     */

    PUBLISHER,

    /**
     * Sort by holder name (last name, then first). Not applicable to all queries.
     */

    HOLDER,

    /**
     * Sort by book owner (last name, then first). Not applicable to all queries.
     */

    OWNER;
}
