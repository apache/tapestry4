//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.informal;

import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 * A version of the Any component that inherits the informal attributes of its parent
 * 
 * @version $Id$
 * @author mindbridge
 */
public class InheritInformalAny extends AbstractComponent 
{
    // Bindings
	private IBinding m_objElementBinding;

	public IBinding getElementBinding() {
		return m_objElementBinding;
	}

	public void setElementBinding(IBinding objElementBinding) {
		m_objElementBinding = objElementBinding;
	}

	protected void generateParentAttributes(IMarkupWriter writer, IRequestCycle cycle) {
		String attribute;

		IComponent objParent = getContainer();
		if (objParent == null)
			return;

		ComponentSpecification specification = objParent.getSpecification();
		Map bindings = objParent.getBindings();
		if (bindings == null)
			return;

		Iterator i = bindings.entrySet().iterator();

		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			String name = (String) entry.getKey();

			// Skip over formal parameters stored in the bindings
			// Map.  We're just interested in informal parameters.

			if (specification.getParameter(name) != null)
				continue;

			IBinding binding = (IBinding) entry.getValue();

			Object value = binding.getObject();
			if (value == null)
				continue;

			if (value instanceof IAsset) {
				IAsset asset = (IAsset) value;

				// Get the URL of the asset and insert that.
				attribute = asset.buildURL(cycle);
			}
			else
				attribute = value.toString();

			writer.attribute(name, attribute);
		}

	}

	public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException 
    {
		String strElement = m_objElementBinding.getObject().toString();

		writer.begin(strElement);
		generateParentAttributes(writer, cycle);
		generateAttributes(writer, cycle);

		renderWrapped(writer, cycle);

		writer.end();
	}

}
