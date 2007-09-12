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

import org.testng.annotations.Test;


/**
 * Tests functionaliy of {@link EnumPropertySelectionModel}.
 * 
 */
@Test
public class TestEnumPropertySelectionModel
{
    enum AbstractFormat
    {
        FreePaperOnly("Free Paper Only"),
        ScientificPosterOnly("Scientific Poster Only"),
        DemonstrationPosterOnly("Demonstration Poster Only"),
        FreePaperorScientificPoster("Free Paper or Scientific Poster"),
        FreePaperorDemonstrationPoster("Free Paper or Demonstration Poster"),
        InstructionalCourse("Instructional Course");
        
        private final String category;
        
        AbstractFormat(String category){
            this.category = category;
        }
        
        public String toString()
        {
            return this.category;
        }
    }
    
    public void test_Simple_Enum()
    {
        EnumPropertySelectionModel ep = new EnumPropertySelectionModel(AbstractFormat.values());
        
        assert "None".equals(ep.getLabel(0));
        
        assert AbstractFormat.FreePaperOnly.toString().equals(ep.getLabel(1));
        
        assert AbstractFormat.FreePaperorDemonstrationPoster.equals(ep.getOption(5));
        
        assert AbstractFormat.values().length + 1 == ep.getOptionCount();
        
        assert "None".equals(ep.getValue(0));
        
        assert ep.getOption(0) == null;
        
        assert AbstractFormat.ScientificPosterOnly.toString().equals(ep.getValue(2));
        
        assert AbstractFormat.FreePaperOnly == ep.translateValue(AbstractFormat.FreePaperOnly.toString());
    }
}
