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
package net.sf.tapestry.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

import net.sf.tapestry.junit.mock.MockTestCase;
import net.sf.tapestry.junit.parse.SpecificationParserTest;
import net.sf.tapestry.junit.parse.TemplateParserTest;
import net.sf.tapestry.junit.script.ScriptTest;
import net.sf.tapestry.junit.spec.TestApplicationSpecification;
import net.sf.tapestry.junit.spec.TestComponentSpecification;
import net.sf.tapestry.junit.utils.TestAdaptorRegistry;
import net.sf.tapestry.junit.utils.TestDataSqueezer;
import net.sf.tapestry.junit.utils.TestEnum;
import net.sf.tapestry.junit.utils.TestIdAllocator;
import net.sf.tapestry.junit.utils.TestLocalizedNameGenerator;
import net.sf.tapestry.junit.utils.TestPool;
import net.sf.tapestry.junit.utils.TestPropertyFinder;
import net.sf.tapestry.junit.valid.ValidSuite;

/**
 *  Master suite of Tapestry tests, combining all other test suites.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TapestrySuite extends TestSuite
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(TestAdaptorRegistry.class);
        suite.addTestSuite(TestTapestryCoerceToIterator.class);
        suite.addTestSuite(TestPool.class);
        suite.addTestSuite(TestLocalizedNameGenerator.class);
        suite.addTestSuite(TestResourceLocation.class);
        suite.addTestSuite(TestPropertyFinder.class);
        suite.addTestSuite(TestListenerMap.class);
        suite.addTestSuite(TestIdAllocator.class);
        suite.addTestSuite(ComponentStringsTest.class);
        suite.addTestSuite(TemplateParserTest.class);
        suite.addTestSuite(SpecificationParserTest.class);
        suite.addTestSuite(TestApplicationSpecification.class);
        suite.addTest(ValidSuite.suite());
        suite.addTestSuite(TestEnum.class);
        suite.addTestSuite(TestDataSqueezer.class);
        suite.addTestSuite(ScriptTest.class);
        suite.addTestSuite(TestComponentSpecification.class);
        suite.addTestSuite(BindingsTestCase.class);
        suite.addTestSuite(TestPropertySource.class);
        suite.addTestSuite(ComponentTest.class);
        suite.addTestSuite(MockTestCase.class);

        return suite;
    }

}