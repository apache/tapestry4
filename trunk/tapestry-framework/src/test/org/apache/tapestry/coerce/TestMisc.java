// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.coerce;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests for random, simple converters.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestMisc extends BaseComponentTestCase
{
    public void testCharArrayToIteratorConverter()
    {
        char[] characters = "Good Morning, Tapestry!".toCharArray();

        CharArrayToIteratorConverter c = new CharArrayToIteratorConverter();

        Iterator i = (Iterator) c.convertValue(characters);

        for (int j = 0; j < characters.length; j++)
        {
            Character expected = new Character(characters[j]);
            assertEquals(expected, i.next());
        }
        
        assertEquals(false, i.hasNext());
    }
    
    public void test_Simple_String_To_List_Converter()
    {
        String input = "simple";
        
        StringToListConverter c = new StringToListConverter();
        
        List conv = (List)c.convertValue(input);
        
        assert conv != null;
        assert conv.size() == 1;
        assert conv.get(0).equals(input);
    }
    
    public void test_Null_String_To_List_Converter()
    {
        StringToListConverter c = new StringToListConverter();
        
        List conv = (List)c.convertValue(null);
        
        assert conv != null;
        assert conv.size() == 0;
    }
    
    public void test_String_To_List_Converter()
    {
        String input = "val1,val2,val3";
        
        StringToListConverter c = new StringToListConverter();
        
        List conv = (List)c.convertValue(input);
        
        assert conv != null;
        assert conv.size() == 3;
        assert conv.get(1).equals("val2");
    }
}
