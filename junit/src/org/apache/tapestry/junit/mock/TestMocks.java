//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.tapestry.junit.TapestryTestCase;

/**
 *  Test case for Mock Servlet API tests using the Simple application.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestMocks extends TapestryTestCase
{
    public static final String LOGS_DIR = "logs";

    public static final String SCRIPTS_DIR = "mock-scripts";

    private PrintStream _savedOut;
    private PrintStream _savedErr;

    protected void runTest() throws Throwable
    {
        String path = SCRIPTS_DIR + "/" + getName();

        MockTester tester = new MockTester(path);

        tester.execute();
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Mock Unit Test Suite");

        addScripts(suite);

        // Handy place to perform one-time 
        deleteDir(".private");

        return suite;
    }

    private static void addScripts(TestSuite suite)
    {
        File scriptsDir = new File(SCRIPTS_DIR);

        String[] names = scriptsDir.list();

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];

            if (name.endsWith(".xml"))
            {
                TestMocks test = new TestMocks();

                test.setName(name);

                suite.addTest(test);
            }
        }
    }

    private static void deleteDir(String path)
    {
        File file = new File(path);

        if (!file.exists())
            return;

        deleteRecursive(file);
    }

    private static void deleteRecursive(File file)
    {
        if (file.isFile())
        {
            file.delete();
            return;
        }

        String[] names = file.list();

        for (int i = 0; i < names.length; i++)
        {
            File f = new File(file, names[i]);
            deleteRecursive(f);
        }

        file.delete();
    }

    /**
     * Ensures that the log directory exists, then redirects System.out
     * and System.err to files within the log.
     */
    protected void setUp() throws Exception
    {
        File outDir = new File(LOGS_DIR);

        if (!outDir.isDirectory())
            outDir.mkdirs();

        _savedOut = System.out;
        _savedErr = System.err;

        System.setOut(createPrintStream(outDir, "out"));
        System.setErr(createPrintStream(outDir, "err"));
    }

    protected PrintStream createPrintStream(File directory, String extension) throws Exception
    {
        String name = getName() + "." + extension;

        File file = new File(directory, name);

        // Open and truncate file.

        FileOutputStream fos = new FileOutputStream(file);

        BufferedOutputStream bos = new BufferedOutputStream(fos);

        return new PrintStream(bos, true);
    }

    /**
     * Closes System.out and System.err, then restores them to their original values.
     */
    protected void tearDown() throws Exception
    {
        System.err.close();
        System.setErr(_savedErr);

        System.out.close();
        System.setOut(_savedOut);

    }
}
