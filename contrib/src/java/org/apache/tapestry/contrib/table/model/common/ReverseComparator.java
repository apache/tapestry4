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

package org.apache.tapestry.contrib.table.model.common;

import java.util.Comparator;

/**
 * @version $Id$
 * @author mindbridge
 *
 */
public class ReverseComparator implements Comparator
{
	private Comparator m_objComparator;

	public ReverseComparator(Comparator objComparator)
	{
		m_objComparator = objComparator;
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object objValue1, Object objValue2)
	{
		return -m_objComparator.compare(objValue1, objValue2);
	}

}
