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

package tutorial.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Foreach;

/**
 * Simple ArrayViewer object, for the Component chapter of the Tutorial
 * @author neil clayton
 */
public class ArrayViewer extends BaseComponent {
	/**
	 * Return the bound heading if there is one, else return a static default heading
	 */
	public String getHeading() {
		IBinding binding = (IBinding)getBinding("heading");
		if(binding.getObject() != null) {
			return binding.getObject().toString();
		}
		return heading;
	}

	/**
	 * Sets the heading.
	 * @param heading The heading to set
	 */
	public void setHeading(String heading) {
		this.heading = heading;
	}

	/**
	 * @see net.sf.tapestry.AbstractComponent#cleanupAfterRender(IRequestCycle)
	 */
	protected void cleanupAfterRender(IRequestCycle cycle) {
		source = null;
		heading = "Array Viewer";
		super.cleanupAfterRender(cycle);
	}

	/**
	 * Returns the source.
	 * @return Object
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * @param source The source to set
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	private String heading = "Array Viewer";
	private Object source;
}
