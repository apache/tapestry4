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

package org.apache.tapestry.contrib.table.model.sql;

import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;

/**
 * 
 * @author mindbridge
 */
public class SqlTableColumnModel extends SimpleTableColumnModel
{
	public SqlTableColumnModel(SqlTableColumn[] arrColumns)
	{
		super(arrColumns);
	}

	public SqlTableColumn getSqlColumn(int nColumn)
	{
		return (SqlTableColumn) getColumn(nColumn);
	}

	public SqlTableColumn getSqlColumn(String strColumn)
	{
		return (SqlTableColumn) getColumn(strColumn);
	}
}
