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

package net.sf.tapestry.form;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.RequestCycleException;

/**
 *  Form element used to upload files.  For the momement, it is necessary to
 *  explicitly set the form's enctype to "multipart/form-data".
 * 
 *  [<a href="../../../../../ComponentReference/Upload.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public class Upload extends AbstractFormComponent
{
    private IBinding _fileBinding;
	private boolean _disabled;
    private String _name;

    public String getName()
    {
        return _name;
    }

    public void setFileBinding(IBinding value)
    {
        _fileBinding = value;
    }

    public IBinding getFileBinding()
    {
        return _fileBinding;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);
 
        boolean rewinding = form.isRewinding();

        _name = form.getElementId(this);

        if (rewinding)
        {
            if (!_disabled)
            {
                IUploadFile file = cycle.getRequestContext().getUploadFile(_name);

                _fileBinding.setObject(file);
            }

            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "file");
        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        // Size, width, etc. can be specified as informal parameters
        // (Not making the same mistake here I made with Text and friends).

        generateAttributes(writer, cycle);
    }

    public boolean getDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

}