// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  Wrapper around a boolean parameter.
 *
 *
 *  @author Howard Lewis Ship
 *  @since 2.3
 *
 */

public class BooleanParameter implements IParameter
{
    private boolean _value;

    public static final BooleanParameter TRUE = new BooleanParameter(true);

    public static final BooleanParameter FALSE = new BooleanParameter(false);

    private BooleanParameter(boolean value)
    {
        _value = value;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        statement.setBoolean(index, _value);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Boolean<");
        buffer.append(_value);
        buffer.append('>');

        return buffer.toString();
    }

}
