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

package net.sf.tapestry.util.prop;

import net.sf.tapestry.Tapestry;

/**
 *  Describes a case where the necessary accessor or mutator 
 *  method could not be located when dynamically getting or setting a property.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class MissingAccessorException extends RuntimeException
{
    private Object rootObject;
    private String propertyPath;

    private Object object;
    private String propertyName;

    /**
     *  @param rootObject the initial object for which a property was being set or retrieved.
     *  @param propertyPath the full property name.  The failure may occur when 
     *     processing any term within the name.
     *  @param object  the specific object being accessed at the time of the failure
     *  @param propertyName the specific property for which no accessor was available
     *
     **/

    public MissingAccessorException(
        Object rootObject,
        String propertyPath,
        Object object,
        String propertyName)
    {
        super(Tapestry.getString("MissingAccessorException.message", propertyPath));

        this.rootObject = rootObject;
        this.propertyPath = propertyPath;
        this.object = object;
        this.propertyName = propertyName;
    }

    public MissingAccessorException(
        String message,
        Object object,
        String propertyName)
    {
        super(message);

        this.object = object;
        this.propertyName = propertyName;
    }

    public Object getObject()
    {
        return object;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public String getPropertyPath()
    {
        return propertyPath;
    }

    public Object getRootObject()
    {
        return rootObject;
    }
}