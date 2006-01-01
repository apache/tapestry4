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

package org.apache.tapestry.contrib.table.model;

import java.util.Iterator;

/**
 * An extension of ITableModel that provides the ability to get the full
 * list of row objects, rather than just the displayed ones.
 * 
 * @author mb
 */
public interface IFullTableModel extends ITableModel {
	/**
	 * Iterates over all of the rows in the model
	 * @return Iterator the iterator for access to the data
	 */
	Iterator getRows();
}
