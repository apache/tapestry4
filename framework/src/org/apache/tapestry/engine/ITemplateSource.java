/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.parse.ComponentTemplate;

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
 * 
 **/

public interface ITemplateSource
{
    /**
     *  Name of an {@link org.apache.tapestry.IAsset} of a component that provides the template
     *  for the asset.  This overrides the default (that the template is in
     *  the same directory as the specification).  This allows
     *  pages or component templates to be located properly, relative to static
     *  assets (such as images and stylesheets).
     * 
     *  @since 2.2
     * 
     **/
    
    public static final String TEMPLATE_ASSET_NAME = "$template";

    /**
     * 
     *  @since 3.0
     * 
     **/
    
    public static final String TEMPLATE_TAG_PARAMETER_NAME = "templateTag";
    
    /**
     *  Locates the template for the component.
     * 
     *  @param cycle The request cycle loading the template; this is required
     *  in some cases when the template is loaded from an {@link org.apache.tapestry.IAsset}.
     *  @param component The component for which a template should be loaded.
     *
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the resource cannot be located or loaded.
     * 
     **/

    public ComponentTemplate getTemplate(IRequestCycle cycle, IComponent component);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     **/

    public void reset();
}