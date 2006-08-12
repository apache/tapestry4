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

import static org.testng.AssertJUnit.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.json.JSONObject;
import org.testng.annotations.Test;


/**
 * Tests functionality of the {@link IJSONWriter} class.
 * 
 * @author jkuhnert
 */
@Test
public class TestJSONWriter extends BaseComponentTestCase
{
    
    /**
     * Tests creating a list of key/value pairs.
     */
    public void testPropertyList() 
    {
        IJSONWriter writer = newJSONWriter();
        
        JSONObject json = writer.object();
        
        json.put("red", "ball");
        json.put("black", "cat");
        json.put("orange", "orange");
        
        assertEquals(json.get("red"), "ball");
        assertEquals(json.get("black"), "cat");
        assertEquals(json.get("orange"), "orange");
    }
    
    /* All writer content is written to this buffer */
    protected ByteArrayOutputStream outputBuffer;
    
    /**
     * Creates a writer instance that content can be asserted
     * against.
     * @return
     */
    protected JSONWriterImpl newJSONWriter()
    {
        outputBuffer = new ByteArrayOutputStream();
        PrintWriter pw = 
            new PrintWriter(outputBuffer);
        
        JSONWriterImpl writer = new JSONWriterImpl(pw);
        
        return writer;
    }
}
