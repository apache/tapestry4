//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.workbench.fields;

import java.util.Date;

import org.apache.tapestry.html.BasePage;

/**
 *  Page to demonstrate DateEdit form component.
 *
 *  @author Malcolm Edgar
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class Dates extends BasePage
{
    private Date _startDate;
    private Date _endDate;
 
    public void detach()
    {
        _startDate = null;
        _endDate = null;       
        super.detach();
    }   
    
    public Date getStartDate()
    {
        return _startDate;
    }

    public void setStartDate(Date date)
    {
        _startDate = date;
    }
    
    public Date getEndDate()
    {
        return _endDate;
    }

    public void setEndDate(Date date)
    {
        _endDate = date;
    }   
}
