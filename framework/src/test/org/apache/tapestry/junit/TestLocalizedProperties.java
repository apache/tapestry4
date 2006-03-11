// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.junit;

import java.io.InputStream;
import java.util.Properties;

import org.apache.tapestry.util.text.LocalizedProperties;

/**
 * Tests to ensure that LocalizedProperties are fully backward compatible with java.util.Properties
 * and that non-latin characters are read correctly.
 * 
 * @author mb
 * @since 4.0
 */
public class TestLocalizedProperties extends TapestryTestCase
{
    private void ensureEquivalence(String fileName)
    {
        ensureEquivalence(fileName, fileName, "ISO-8859-1");
    }

    private void ensureEquivalence(String fileName1, String fileName2, String encoding)
    {
        InputStream standardIns = getClass().getResourceAsStream(fileName1);
        Properties standard = new Properties();
        Exception standardException = null;
        try
        {
            standard.load(standardIns);
        }
        catch (Exception e)
        {
            standardException = e;
        }

        InputStream localizedIns = getClass().getResourceAsStream(fileName2);
        Properties localized = new Properties();
        LocalizedProperties localizedProperties = new LocalizedProperties(localized);
        Exception localizedException = null;
        try
        {
            localizedProperties.load(localizedIns, encoding);
        }
        catch (Exception e)
        {
            localizedException = e;
        }

        if (standardException == null && localizedException == null)
            assertEquals("The property content does not match", standard, localized);
        else if (standardException == null && localizedException != null)
            fail("Properties did not throw an exception, but LocalizedProperties did: "
                    + localizedException);
        else if (standardException != null && localizedException == null)
            fail("LocalizedProperties did not throw an exception, but Properties did: "
                    + localizedException);
        // this test is disabled because in some cases Properties throws an incorrect exception
        // probably due to a bug
        //else if (standardException != null && localizedException != null)
        //	assertEquals("The exception types do not match", standardException.getClass(),
        // localizedException.getClass());
    }

    /**
     * Test for the equivalence between Properties and LocalizedProperties for latin properties
     */
    public void testEquivalence()
    {
        ensureEquivalence("StandardProperties.properties");
        ensureEquivalence("BadQuoting1.properties");
        ensureEquivalence("BadQuoting2.properties");
    }

    /**
     * Tests the reading of files using different encodings. Compare it with the reading of files
     * that have gone through native2ascii and read using Properties.
     */
    public void testEncodings()
    {
        ensureEquivalence("StandardUTFProperties.properties", "UTFProperties.properties", "utf-8");
        ensureEquivalence(
                "StandardCyrillicProperties.properties",
                "CyrillicProperties.properties",
                "ISO-8859-5");
    }
}
