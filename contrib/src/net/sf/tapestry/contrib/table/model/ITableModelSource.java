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

package net.sf.tapestry.contrib.table.model;

import net.sf.tapestry.IComponent;

/**
 * A Tapestry component that provides the current table model.
 * This interface is used for obtaining the table model source by
 * components wrapped by it, as well as by external renderers,
 * such as those provided by the column implementations
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableModelSource extends IComponent
{
    final static String TABLE_MODEL_SOURCE_PROPERTY = "net.sf.tapestry.contrib.table.model.ITableModelSource";

	/**
	 * Method getTableModel.
	 * @return ITableModel the current table model
	 */
	ITableModel getTableModel();

}
