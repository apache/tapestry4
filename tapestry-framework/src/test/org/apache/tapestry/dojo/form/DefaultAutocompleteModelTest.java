// Copyright Jul 30, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link DefaultAutocompleteModel}.
 * 
 * @author jkuhnert
 */
@Test
public class DefaultAutocompleteModelTest extends BaseComponentTestCase
{

    public void test_Basic_Model()
    {
        List values = new ArrayList();
        
        SimpleBean s1 = new SimpleBean(new Integer(1), "Simple 1", 100);
        SimpleBean s2 = new SimpleBean(new Integer(2), "Simple 2", 200);
        SimpleBean s3 = new SimpleBean(new Integer(3), "Simple 3", 300);
        
        values.add(s1);
        values.add(s2);
        values.add(s3);
        
        IAutocompleteModel model = new DefaultAutocompleteModel(values, "id", "label");
        
        // basic checks
        assert s2.getLabel().equals(model.getLabelFor(s2));
        assert model.getPrimaryKey(s3).equals(3);
        assert model.getValue(1) == s1;
    }
    
    public void test_Filtering_Match()
    {
        List values = new ArrayList();
        
        SimpleBean s1 = new SimpleBean(new Integer(1), "Simple 1", 100);
        SimpleBean s2 = new SimpleBean(new Integer(2), "Simple 2", 200);
        SimpleBean s3 = new SimpleBean(new Integer(3), "Simple 3", 300);
        
        values.add(s1);
        values.add(s2);
        values.add(s3);
        
        IAutocompleteModel model = new DefaultAutocompleteModel(values, "id", "label");
        
        Map results = model.filterValues("sim");
        
        assert results != null;
        assert results.size() == 3;
        
        assert results.containsKey(2);
        assert results.get(2).equals(s2.getLabel());
        
        results = model.filterValues("simple 1");
        
        assert results.size() == 1;
        assert results.get(1) == s1.getLabel();
    }
    
    public void test_Filtering_Null()
    {
        List values = new ArrayList();
        
        SimpleBean s1 = new SimpleBean(new Integer(1), "Simple 1", 100);
        SimpleBean s2 = new SimpleBean(new Integer(2), "Simple 2", 200);
        SimpleBean s3 = new SimpleBean(new Integer(3), "Simple 3", 300);
        
        values.add(s1);
        values.add(s2);
        values.add(s3);
        
        IAutocompleteModel model = new DefaultAutocompleteModel(values, "id", "label");
        
        Map results = model.filterValues(null);
        
        assert results != null;
        assert results.size() == 0;
    }
}
