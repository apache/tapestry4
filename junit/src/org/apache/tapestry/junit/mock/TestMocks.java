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
