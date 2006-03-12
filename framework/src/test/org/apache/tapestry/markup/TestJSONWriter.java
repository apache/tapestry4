// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry.markup;

import java.io.PrintWriter;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IJSONWriter;

import java.io.ByteArrayOutputStream;


/**
 * Tests functionality of the {@link IJSONWriter} class.
 * 
 * @author jkuhnert
 */
public class TestJSONWriter extends HiveMindTestCase
{
    
    /**
     * Tests creating a list of key/value pairs.
     */
    public void testPropertyList() 
    {
        String listGoal = "{\"red\":\"ball\",\"black\":\"cat\",\"orange\":\"orange\"}";
        
        IJSONWriter writer = newWriter();
        
        writer.write("red", "ball");
        writer.write("black", "cat");
        writer.write("orange", "orange");
        
        assertEquals(output(writer), listGoal);
    }
    
    /**
     * Gets the stored string output from the 
     * local <code>outputBuffer</code>, after first
     * closing the passed in {@link IJSONWriter}.
     * 
     * @param writer
     * @return The string output that was written.
     */
    public String output(IJSONWriter writer)
    {
        writer.close();
        return outputBuffer.toString();
    }
    
    /* All writer content is written to this buffer */
    protected ByteArrayOutputStream outputBuffer;
    
    /**
     * Creates a writer instance that content can be asserted
     * against.
     * @return
     */
    protected JSONWriterImpl newWriter()
    {
        outputBuffer = new ByteArrayOutputStream();
        PrintWriter pw = 
            new PrintWriter(outputBuffer);
        
        JSONWriterImpl writer = new JSONWriterImpl(pw);
        
        return writer;
    }
}
