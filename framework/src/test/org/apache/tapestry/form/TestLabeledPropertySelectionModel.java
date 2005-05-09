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

package org.apache.tapestry.form;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.LabeledPropertySelectionModel;
import org.apache.tapestry.junit.TapestryTestCase;

/**
 * Test case for {@link org.apache.tapestry.form.LabeledPropertySelectionModel}.
 * 
 * @author Paul Ferraro
 * @since  4.0
 */
public class TestLabeledPropertySelectionModel extends TapestryTestCase
{
    public void testEmptyModel()
    {
        LabeledPropertySelectionModel model = new LabeledPropertySelectionModel();
        
        validateLabel(model, "", null, "");
        
        assertEquals(model.getOptionCount(), 1);
    }
    
    public void testDefaultLabeledModel()
    {
        LabeledPropertySelectionModel model = new LabeledPropertySelectionModel(createInnerModel());
        
        validateLabel(model, "", null, "");
        
        validateModel(model);
    }

    public void testLabeledModel()
    {
        String label = "Select a value";
        Object option = null;
        String value = "-1";
        
        LabeledPropertySelectionModel model = new LabeledPropertySelectionModel(createInnerModel(), label, option, value);
        
        assertEquals(label, model.getLabel());
        assertEquals(option, model.getOption());
        assertEquals(value, model.getValue());
        
        validateLabel(model, label, option, value);
        
        validateModel(model);
    }
    
    private void validateLabel(IPropertySelectionModel model, String label, Object option, String value)
    {
        assertTrue(model.getOptionCount() > 0);
        
        assertEquals(model.getLabel(0), label);
        assertEquals(model.getOption(0), option);
        assertEquals(model.getValue(0), value);
        assertEquals(model.translateValue(value), option);
    }
    
    private void validateModel(IPropertySelectionModel model)
    {
        assertEquals(model.getOptionCount(), 3);
        
        assertEquals(model.getLabel(1), "true");
        assertEquals(model.getOption(1), Boolean.TRUE);
        assertEquals(model.getValue(1), "0");
        assertEquals(model.translateValue("0"), Boolean.TRUE);

        assertEquals(model.getLabel(2), "false");
        assertEquals(model.getOption(2), Boolean.FALSE);
        assertEquals(model.getValue(2), "1");
        assertEquals(model.translateValue("1"), Boolean.FALSE);
    }
    
    private IPropertySelectionModel createInnerModel()
    {
        return new IPropertySelectionModel()
        {
            private boolean[] values = new boolean[] { true, false };
            
            public int getOptionCount()
            {
                return values.length;
            }

            public Object getOption(int index)
            {
                return Boolean.valueOf(values[index]);
            }

            public String getLabel(int index)
            {
                return Boolean.toString(values[index]);
            }

            public String getValue(int index)
            {
                return Integer.toString(index);
            }

            public Object translateValue(String value)
            {
                return getOption(Integer.parseInt(value));
            }
        };
    }
}
