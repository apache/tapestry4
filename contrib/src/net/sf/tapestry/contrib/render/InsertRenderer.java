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

package net.sf.tapestry.contrib.render;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 * @version $Id$
 * @author mindbridge
 *
 */
public class InsertRenderer extends AbstractComponent {

    private IRender m_objRenderer;

	/**
	 * Returns the renderer.
	 * @return IRender
	 */
	public IRender getRenderer() {
		return m_objRenderer;
	}

	/**
	 * Sets the renderer.
	 * @param renderer The renderer to set
	 */
	public void setRenderer(IRender renderer) {
		m_objRenderer = renderer;
	}

    /**
     * @see net.sf.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter objWriter, IRequestCycle objCycle)
        throws RequestCycleException {
        // use span for informal parameters?
        getRenderer().render(objWriter, objCycle);
    }

}
