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

package org.apache.tapestry.vlib.ejb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Used with {@link org.apache.tapestry.vlib.ejb.IBookQuery} to represent
 *  the order in which columns are sorted in the result set.  SortOrdering
 *  is immutable.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class SortOrdering implements Serializable
{
	private static final long serialVersionUID = -1621923918904975133L;
	
    private SortColumn _column;
    private boolean _descending;

    public SortOrdering(SortColumn column)
    {
        this(column, false);
    }

    public SortOrdering(SortColumn column, boolean descending)
    {
        _column = column;
        _descending = descending;
    }

    public boolean isDescending()
    {
        return _descending;
    }

    public SortColumn getColumn()
    {
        return _column;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("column", _column);
        builder.append("descending", _descending);

        return builder.toString();
    }

}
