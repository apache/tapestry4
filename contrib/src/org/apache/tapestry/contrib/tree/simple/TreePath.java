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

package org.apache.tapestry.contrib.tree.simple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author ceco
 * @version $Id$
 */
public class TreePath extends Object implements Serializable {
	private TreePath parentPath;
	transient private Object lastPathComponent;

	public TreePath(Object[] path) {
		if (path == null || path.length == 0)
			throw new IllegalArgumentException("path in TreePath must be non null and not empty.");
		lastPathComponent = path[path.length - 1];
		if (path.length > 1)
			parentPath = new TreePath(path, path.length - 1);
	}

	public TreePath(Object singlePath) {
		if (singlePath == null)
			throw new IllegalArgumentException("path in TreePath must be non null.");
		lastPathComponent = singlePath;
		parentPath = null;
	}

	protected TreePath(TreePath parent, Object lastElement) {
		if (lastElement == null)
			throw new IllegalArgumentException("path in TreePath must be non null.");
		parentPath = parent;
		lastPathComponent = lastElement;
	}

	protected TreePath(Object[] path, int length) {
		lastPathComponent = path[length - 1];
		if (length > 1)
			parentPath = new TreePath(path, length - 1);
	}

	protected TreePath() {
	}

	public Object[] getPath() {
		int i = getPathCount();
		Object[] result = new Object[i--];

		for (TreePath path = this; path != null; path = path.parentPath) {
			result[i--] = path.lastPathComponent;
		}
		return result;
	}

	public Object getLastPathComponent() {
		return lastPathComponent;
	}

	/**
	 * Returns the number of elements in the path.
	 *
	 * @return an int giving a count of items the path
	 */
	public int getPathCount() {
		int result = 0;
		for (TreePath path = this; path != null; path = path.parentPath) {
			result++;
		}
		return result;
	}

	public Object getPathComponent(int element) {
		int pathLength = getPathCount();

		if (element < 0 || element >= pathLength)
			throw new IllegalArgumentException(
				"Index " + element + " is out of the specified range");

		TreePath path = this;

		for (int i = pathLength - 1; i != element; i--) {
			path = path.parentPath;
		}
		return path.lastPathComponent;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof TreePath) {
			TreePath oTreePath = (TreePath) o;

			if (getPathCount() != oTreePath.getPathCount())
				return false;
			for (TreePath path = this; path != null; path = path.parentPath) {
				if (!(path.lastPathComponent.equals(oTreePath.lastPathComponent))) {
					return false;
				}
				oTreePath = oTreePath.parentPath;
			}
			return true;
		}
		return false;
	}

	public int hashCode() {
		return lastPathComponent.hashCode();
	}

	public boolean isDescendant(TreePath aTreePath) {
		if (aTreePath == this)
			return true;

		if (aTreePath != null) {
			int pathLength = getPathCount();
			int oPathLength = aTreePath.getPathCount();

			if (oPathLength < pathLength)
				// Can't be a descendant, has fewer components in the path.
				return false;
			while (oPathLength-- > pathLength)
				aTreePath = aTreePath.getParentPath();
			return equals(aTreePath);
		}
		return false;
	}

	public TreePath pathByAddingChild(Object child) {
		if (child == null)
			throw new NullPointerException("Null child not allowed");

		return new TreePath(this, child);
	}

	public TreePath getParentPath() {
		return parentPath;
	}

	public String toString() {
		StringBuffer tempSpot = new StringBuffer("[");

		for (int counter = 0, maxCounter = getPathCount(); counter < maxCounter; counter++) {
			if (counter > 0)
				tempSpot.append(", ");
			tempSpot.append(getPathComponent(counter));
		}
		tempSpot.append("]");
		return tempSpot.toString();
	}

	// Serialization support.  
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();

		Vector values = new Vector();
		//boolean writePath = true;

		if (lastPathComponent != null && (lastPathComponent instanceof Serializable)) {
			values.addElement("lastPathComponent");
			values.addElement(lastPathComponent);
		}
		s.writeObject(values);
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();

		Vector values = (Vector) s.readObject();
		int indexCounter = 0;
		int maxCounter = values.size();

		if (indexCounter < maxCounter
			&& values.elementAt(indexCounter).equals("lastPathComponent")) {
			lastPathComponent = values.elementAt(++indexCounter);
			indexCounter++;
		}
	}
}
