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

package net.sf.tapestry.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.tapestry.IEngineService;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  Defines the configuration for a Tapestry application.  An ApplicationSpecification
 *  extends {@link LibrarySpecification} by adding new properties
 *  name and engineClassName.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ApplicationSpecification extends LibrarySpecification implements IApplicationSpecification
{
    private String _name;
    private String _engineClassName;
 
    public String getName()
    {
        return _name;
    }

    public void setEngineClassName(String value)
    {
        _engineClassName = value;
    }

    public String getEngineClassName()
    {
        return _engineClassName;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String toString()
    {
        return "ApplicationSpecification[" + _name + " " + _engineClassName + "]";
    }

}