/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.junit.spec;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.AssetSpecification;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;

/**
 *  Test cases for page and component specifications.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestComponentSpecification extends TapestryTestCase
{

    public TestComponentSpecification(String name)
    {
        super(name);
    }

    public void testBeanProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("BeanProperty.jwc");
        BeanSpecification fred = s.getBeanSpecification("fred");

        checkList("propertyNames", new String[] { "bruce", "nicole", "zeta" }, fred.getPropertyNames());

        checkProperty(fred, "bruce", "wayne");
        checkProperty(fred, "nicole", "kidman");
        checkProperty(fred, "zeta", "jones");

    }

    public void testComponentProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("ComponentProperty.jwc");
        ContainedComponent c = s.getComponent("barney");

        checkList("propertyNames", new String[] { "apple", "chocolate", "frozen" }, c.getPropertyNames());

        checkProperty(c, "apple", "pie");
        checkProperty(c, "chocolate", "cake");
        checkProperty(c, "frozen", "yogurt");

    }
    
    public void testAssetProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("AssetProperty.jwc");
        
        checkAsset(s, "private", "hugh", "grant");
        checkAsset(s, "external", "joan", "rivers");
        checkAsset(s, "context", "john", "cusak");
    }
    
    private void checkAsset(ComponentSpecification s, String assetName, String propertyName,
    String expectedValue)
    {
        AssetSpecification a = s.getAsset(assetName);
        
        assertEquals("Property " + propertyName + ".",
        expectedValue,
        a.getProperty(propertyName));
    }
}
