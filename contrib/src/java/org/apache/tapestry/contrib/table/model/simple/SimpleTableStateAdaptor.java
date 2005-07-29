// Copyright 2005 The Apache Software Foundation
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

/*
 * Created on Jul 6, 2005
 */
package org.apache.tapestry.contrib.table.model.simple;


import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.tapestry.contrib.table.components.TableMessages;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.ISqueezeAdaptor;

public class SimpleTableStateAdaptor implements ISqueezeAdaptor {

    private static final String PREFIX = "t";
	
	public String squeeze(DataSqueezer squeezer, Object data)
			throws IOException {
		SimpleTableState objState = (SimpleTableState) data;
		
		StringBuffer buf = new StringBuffer();
		buf.append(objState.getPagingState().getPageSize());
		buf.append(":");
		buf.append(objState.getPagingState().getCurrentPage());
		buf.append(":");
		String strSortColumn = objState.getSortingState().getSortColumn();
		if (strSortColumn == null)
			strSortColumn = "";
		buf.append(strSortColumn);
		buf.append(":");
		buf.append(objState.getSortingState().getSortOrder() ? 'T' : 'F');
		
		return buf.toString();
	}

	public Object unsqueeze(DataSqueezer squeezer, String string)
			throws IOException {
		StringTokenizer strTok = new StringTokenizer(string, ":");
		if (strTok.countTokens() != 4)
			throw new IOException(TableMessages.invalidTableStateFormat(string));
		int nPageSize = Integer.parseInt(strTok.nextToken());
		int nCurrentPage = Integer.parseInt(strTok.nextToken());
		String strSortColumn = strTok.nextToken();
		if (strSortColumn.equals(""))
			strSortColumn = null;
		boolean bSortOrder = strTok.nextToken().equals("T");
		
		return new SimpleTableState(nPageSize, nCurrentPage, strSortColumn, bSortOrder);
	}

	public void register(DataSqueezer squeezer) {
		squeezer.register(PREFIX, SimpleTableState.class, this);
	}

}
