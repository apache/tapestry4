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

import org.apache.tapestry.ILocationHolder;

/**
 *  Defines a formal parameter to a component.  An <code>IParameterSpecification</code>
 *  is contained by a {@link IComponentSpecification}.
 *
 *  <p>TBD: Identify arrays in some way.
 * 
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IParameterSpecification extends ILocationHolder
{
    /**
     *  Returns the class name of the expected type of the parameter.  The default value
     *  is <code>java.lang.Object</code> which matches anything.
     *
     **/
    public abstract String getType();
    /**
     *  Returns true if the parameter is required by the component.
     *  The default is false, meaning the parameter is optional.
     *
     **/
    public abstract boolean isRequired();
    public abstract void setRequired(boolean value);
    /**
     *  Sets the type of value expected for the parameter.  This can be
     *  left blank to indicate any type.
     * 
     **/
    public abstract void setType(String value);
    /**
     *  Returns the documentation for this parameter.
     * 
     *  @since 1.0.9
     * 
     **/
    public abstract String getDescription();
    /**
     *  Sets the documentation for this parameter.
     * 
     *  @since 1.0.9
     *    	 
     **/
    public abstract void setDescription(String description);
    /**
     *  Sets the property name (of the component class)
     *  to connect the parameter to.
     * 
     **/
    public abstract void setPropertyName(String propertyName);
    /**
     *  Returns the name of the JavaBeans property to connect the
     *  parameter to.
     * 
     **/
    public abstract String getPropertyName();
    /**
     *  Returns the parameter value direction, defaulting to {@link Direction#CUSTOM}
     *  if not otherwise specified.
     * 
     **/
    public abstract Direction getDirection();
    public abstract void setDirection(Direction direction);
    /**
     *  Returns the default value of the JavaBeans property if no binding is provided
     *  or null if it has not been specified
     **/
    public abstract String getDefaultValue();
    /**
     *  Sets the default value of the JavaBeans property if no binding is provided
     * 
     **/
    public abstract void setDefaultValue(String defaultValue);
    
}