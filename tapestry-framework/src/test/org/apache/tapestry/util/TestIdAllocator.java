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

import org.apache.tapestry.util.io.CompressedDataEncoder;
import static org.testng.AssertJUnit.assertEquals;
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

    public void test_To_External_String()
    {
        IdAllocator ida = new IdAllocator();

        ida.allocateId("ext");
        
        assertEquals(",ext$0", ida.toExternalString());

        ida.allocateId("ext");
        ida.allocateId("ext2");
        
        assertEquals(",ext$1,ext2$0", ida.toExternalString());
    }

    public void test_Empty_To_External_String()
    {
        IdAllocator ida = new IdAllocator();

        assertEquals("", ida.toExternalString());
    }

    public void test_To_External_String_Namespace()
    {
        IdAllocator ida = new IdAllocator("NS");

        ida.allocateId("ext");

        assertEquals("NS,extNS$0", ida.toExternalString());
    }

    public void test_From_External_String()
    {
        String seed = "NS,extNS$3,testNS$2,simpleNS$0";

        assertEquals(seed, IdAllocator.fromExternalString(seed).toExternalString());

        seed = ",ext$0";
        assertEquals(seed, IdAllocator.fromExternalString(seed).toExternalString());

        seed = "";
        assertEquals(seed, IdAllocator.fromExternalString(seed).toExternalString());
    }

    public void test_From_External_String_State()
    {
        String seed = "NS,extNS$3,testNS$2,simpleNS$0,ext_0NS$0";
        IdAllocator ida = IdAllocator.fromExternalString(seed);

        assertEquals("NS", ida._namespace);
        assertEquals(9, ida._generatorMap.size());
        assertEquals(4, ida._uniqueGenerators.size());
        assertEquals("extNS_3", ida.allocateId("ext"));
    }

    public void test_Compressed_External_String()
    {
        String seed = "NS,extNS$3,testNS$2,simpleNS$0,ext_0NS$0";

        String compressed = CompressedDataEncoder.encodeString(seed);
        assertEquals(seed, CompressedDataEncoder.decodeString(compressed));

        IdAllocator ida = IdAllocator.fromExternalString(CompressedDataEncoder.decodeString(compressed));

        assertEquals("NS", ida._namespace);
        assertEquals(9, ida._generatorMap.size());
        assertEquals(4, ida._uniqueGenerators.size());
        assertEquals("extNS_3", ida.allocateId("ext"));
    }

    public void test_Clear()
    {
        String seed = "NS,extNS$3,testNS$2,simpleNS$0,ext_0NS$0";
        IdAllocator ida = IdAllocator.fromExternalString(seed);

        assertEquals("NS", ida._namespace);
        assertEquals(9, ida._generatorMap.size());
        assertEquals(4, ida._uniqueGenerators.size());

        ida.clear();

        assertEquals(0, ida._generatorMap.size());
        assertEquals(0, ida._uniqueGenerators.size());
    }

    public void test_To_External_String_Underscores()
    {
        IdAllocator ida = IdAllocator.fromExternalString(",service$0,page$0,component$0,container$0,session$0,sp$0,NestedIfBorder$0,Shell$0," +
                                                         "Body$0,TopComponent$0,testId$0,If_0$0,BorderComponent$0,If$2,NestedComponent$0,RenderBody$0,Form$0");

        assertEquals("If_0_0", ida.allocateId("If_0"));
    }
}
