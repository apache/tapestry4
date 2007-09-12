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
package org.apache.tapestry.util;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.Test;


/**
 * Tests functionality of {@link IdAllocator}.
 */
@Test
public class TestIdAllocator
{
    public void test_Simple_Allocation()
    {
        IdAllocator ida = new IdAllocator();
        
        assertEquals("name", ida.allocateId("name"));
        assertEquals("name_0", ida.allocateId("name"));
        assertEquals("name_1", ida.allocateId("name"));
    }
    
    public void test_Namespace_Allocation()
    {
        IdAllocator ida = new IdAllocator("_test");
        
        assertEquals("name_test",ida.allocateId("name"));
        assertEquals("name_test_0", ida.allocateId("name"));
        assertEquals("name_test_1", ida.allocateId("name"));
    }
    
    public void test_Peek_Allocation()
    {
        IdAllocator ida = new IdAllocator();
        
        assertEquals("name", ida.allocateId("name"));
        
        assertEquals("name_0", ida.peekNextId("name"));
        assertEquals("name_0", ida.allocateId("name"));
        
        assertEquals("name_1", ida.peekNextId("name"));
        assertEquals("name_1", ida.peekNextId("name"));
    }
    
    public void test_Peek_Allocation_With_PriorId()
    {
        IdAllocator ida = new IdAllocator();
        
        assertEquals("name", ida.allocateId("name"));
        
        assertEquals("name_0", ida.peekNextId("name_0"));
        assertEquals("name_0", ida.allocateId("name"));
        
        assertEquals("name_1", ida.peekNextId("name"));
        assertEquals("name_1", ida.peekNextId("name"));
        
        assertEquals("name_1", ida.peekNextId("name_0"));
    }
    
    public void test_Ignore_Case()
    {
        IdAllocator ida = new IdAllocator();
        
        assertEquals("name", ida.allocateId("name"));
        assertEquals("name_0", ida.allocateId("Name"));
    }
}
