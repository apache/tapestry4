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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.bean.IBeanInitializer;

/**
 *  A specification of a helper bean for a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public class BeanSpecification extends LocatablePropertyHolder implements IBeanSpecification
{
    protected String className;
    protected BeanLifecycle lifecycle;

    /** @since 1.0.9 **/
    private String description;

    /**
     *  A List of {@link IBeanInitializer}.
     *
     **/

    protected List initializers;

    public String getClassName()
    {
        return className;
    }

    public BeanLifecycle getLifecycle()
    {
        return lifecycle;
    }

    /**
     *  @since 1.0.5
     *
     **/

    public void addInitializer(IBeanInitializer initializer)
    {
        if (initializers == null)
            initializers = new ArrayList();

        initializers.add(initializer);
    }

    /**
     *  Returns the {@link List} of {@link IBeanInitializer}s.  The caller
     *  should not modify this value!.  May return null if there
     *  are no initializers.
     *
     *  @since 1.0.5
     *
     **/

    public List getInitializers()
    {
        return initializers;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("BeanSpecification[");

        buffer.append(className);
        buffer.append(", lifecycle ");
        buffer.append(lifecycle.getName());

        if (initializers != null && initializers.size() > 0)
        {
            buffer.append(", ");
            buffer.append(initializers.size());
            buffer.append(" initializers");
        }

        buffer.append(']');

        return buffer.toString();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String desc)
    {
        description = desc;
    }

    /** @since 2.4 **/

    public void setClassName(String className)
    {
        this.className = className;
    }
    
    /** @since 2.4 **/
    
    public void setLifecycle(BeanLifecycle lifecycle)
    {
        this.lifecycle = lifecycle;
    }

}