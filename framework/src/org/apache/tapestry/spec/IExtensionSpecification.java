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

package org.apache.tapestry.spec;

import java.util.Map;

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocationHolder;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.util.IPropertyHolder;

/**
 *  Defines an "extension", which is much like a helper bean, but 
 *  is part of a library or application specification (and has the same
 *  lifecycle as the application).
 * 
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IExtensionSpecification extends IPropertyHolder, ILocationHolder, ILocatable
{
    public abstract String getClassName();
    public abstract void setClassName(String className);
    public abstract void addConfiguration(String propertyName, Object value);
    /**
     *  Returns an immutable Map of the configuration; keyed on property name,
     *  with values as properties to assign.
     * 
     **/
    public abstract Map getConfiguration();
    /**
     *  Invoked to instantiate an instance of the extension and return it.
     *  It also configures properties of the extension.
     * 
     **/
    public abstract Object instantiateExtension(IResourceResolver resolver);
    /**
     *  Returns true if the extensions should be instantiated
     *  immediately after the containing 
     *  {@link org.apache.tapestry.spec.LibrarySpecification}
     *  if parsed.  Non-immediate extensions are instantiated
     *  only as needed.
     * 
     **/
    public abstract boolean isImmediate();
    public abstract void setImmediate(boolean immediate);
}