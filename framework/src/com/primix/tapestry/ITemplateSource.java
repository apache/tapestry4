/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 * A source of localized HTML templates for components.  
 * The cache is the means of access for components to load thier templates,
 * which they need not do until just before rendering.
 *
 * <p>The template cache must be able to locate and parse templates as needed.
 * It may maintain templates in memory.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.tapestry;

import java.util.Locale;
import com.primix.tapestry.parse.ComponentTemplate;

public interface ITemplateSource
{
    /**
    *  Locates the template for the component.
    *
    *  @throws ResourceException if the resource cannot be located or loaded.
    */

    public ComponentTemplate getTemplate(IComponent component)
    throws ResourceUnavailableException;

    /**
    *  Invoked to have the source clear any internal cache.  This is most often
    *  used when debugging an application.
    *
    */

    public void reset();
}
