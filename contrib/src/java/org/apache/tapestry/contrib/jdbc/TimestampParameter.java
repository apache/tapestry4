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

package org.apache.tapestry.contrib.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 *  Used with Timestamp parameters.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TimestampParameter implements IParameter
{
    private Timestamp _timestamp;

    public TimestampParameter(Timestamp timestamp)
    {
        _timestamp = timestamp;
    }

    public void set(PreparedStatement statement, int index) throws SQLException
    {
        if (_timestamp == null)
            statement.setNull(index, Types.TIMESTAMP);
        else
            statement.setTimestamp(index, _timestamp);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Timestamp<");
        buffer.append(_timestamp);
        buffer.append('>');

        return buffer.toString();
    }
}
