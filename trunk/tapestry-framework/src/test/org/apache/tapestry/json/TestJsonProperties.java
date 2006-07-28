package org.apache.tapestry.json;
// Copyright Jul 8, 2006 The Apache Software Foundation
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

import static org.testng.AssertJUnit.assertEquals;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests functionality of the core {@link JSONObject} .
 * 
 * @author jkuhnert
 */
@Test
public class TestJsonProperties extends BaseComponentTestCase
{
 
    public void testLiteralArrays()
    {
        JSONObject json = new JSONObject();
        
        json.accumulate("key", new JSONLiteral("[value1]"));
        json.accumulate("key", new JSONLiteral("[value2]"));
        json.accumulate("key", new JSONLiteral("[value3]"));
        
        assertEquals("{\"key\":[[value1],[value2],[value3]]}",
                json.toString());
        
        JSONObject container = new JSONObject();
        container.put("container", json);
        
        assertEquals("{\"container\":{\"key\":[[value1],[value2],[value3]]}}",
                container.toString());
    }
    
    public void testLiteralArrayContainer()
    {
        JSONObject profile = new JSONObject();
        profile.put("constraints", new JSONObject());
        
        JSONObject cons = profile.getJSONObject("constraints");
        
        if (!cons.has("key"))
            cons.put("key", new JSONArray());
        
        Locale locale = Locale.ENGLISH;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        
        cons.accumulate("key", 
                new JSONLiteral("[something,value1:{key:\"value\",key2:"
                        + JSONObject.quote(symbols.getGroupingSeparator()) + "}]"));
        cons.accumulate("key", new JSONLiteral("[value2]"));
        cons.accumulate("key", new JSONLiteral("[value3]"));
        
        assertEquals("{\"constraints\":{\"key\":[[something,value1:{key:\"value\",key2:\",\"}],[value2],[value3]]}}",
                profile.toString());
    }
    
}
