/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.junit;

import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IExtensionSpecification;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.ILocation;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MockApplicationSpecification implements IApplicationSpecification {
    private Map extensions = new HashMap();

    public String getLibrarySpecificationPath(String id) {
        return null;
    }

    public void setLibrarySpecificationPath(String id, String path) {
    }

    public List getLibraryIds() {
        return null;
    }

    public String getPageSpecificationPath(String name) {
        return null;
    }

    public void setPageSpecificationPath(String name, String path) {
    }

    public List getPageNames() {
        return null;
    }

    public void setComponentSpecificationPath(String type, String path) {
    }

    public String getComponentSpecificationPath(String type) {
        return null;
    }

    public List getComponentTypes() {
        return null;
    }

    public String getServiceClassName(String name) {
        return null;
    }

    public List getServiceNames() {
        return null;
    }

    public void setServiceClassName(String name, String className) {
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public Map getExtensionSpecifications() {
        return null;
    }

    public void addExtensionSpecification(String name, IExtensionSpecification extension) {
    }

    public List getExtensionNames() {
        return null;
    }

    public IExtensionSpecification getExtensionSpecification(String name) {
        return null;
    }

    public Object getExtension(String name) {
        return getExtension(name, null);
    }

    /**
     * Ignore type constraint for now, just returns item from
     * map from {@link #setExtensions(Map)} the specified name.
     */
    public Object getExtension(String name, Class typeConstraint) {
        return extensions.get(name);
    }

    public boolean checkExtension(String name) {
        return extensions.get(name) != null;
    }

    public void setExtensions(Map extensions) {
        this.extensions = extensions;
    }

    public void instantiateImmediateExtensions() {
    }

    public IResourceResolver getResourceResolver() {
        return null;
    }

    public void setResourceResolver(IResourceResolver resolver) {
    }

    public String getPublicId() {
        return null;
    }

    public void setPublicId(String value) {
    }

    public IResourceLocation getSpecificationLocation() {
        return null;
    }

    public void setSpecificationLocation(IResourceLocation specificationLocation) {
    }

    public List getPropertyNames() {
        return null;
    }

    public void setProperty(String name, String value) {
    }

    public void removeProperty(String name) {
    }

    public String getProperty(String name) {
        return null;
    }

    public void setLocation(ILocation location) {
    }

    public ILocation getLocation() {
        return null;
    }

    public String getName() {
        return null;
    }

    public void setEngineClassName(String value) {
    }

    public String getEngineClassName() {
        return null;
    }

    public void setName(String name) {
    }
}
