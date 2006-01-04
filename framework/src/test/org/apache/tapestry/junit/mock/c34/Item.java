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

package org.apache.tapestry.junit.mock.c34;

import java.io.Serializable;

/**
 * A serializable Item with one volatile field that is not serialized.
 * 
 * Used to test whether the value string representation is matched,
 * or whether it is unsqueezed instead.
 * 
 * @author Mindbridge
 */
public class Item implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private transient String _id = "unknown";
	private String _value = "unknown";

	public Item(String id, String value) {
		_id = id;
		_value = value;
	}

	public String getId() {
		return _id;
	}

	public String getValue() {
		return _value;
	}

	public String toString() {
		return "Item[" + _id + " : " + _value + "]";
	}
}
