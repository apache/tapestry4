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

package org.apache.tapestry.workbench.fields;

import java.math.BigDecimal;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.workbench.Visit;

/**
 * @author Howard Lewis Ship
 * @since 1.0.7
 */

public abstract class Fields extends BasePage
{

    public static final int INT_MIN = 5;

    public static final int INT_MAX = 20;

    public static final double DOUBLE_MIN = 3.14;

    public static final double DOUBLE_MAX = 27.5;

    public static final BigDecimal DECIMAL_MIN = new BigDecimal("2");

    public static final BigDecimal DECIMAL_MAX = new BigDecimal(
            "100.123456234563456734563456356734567456784567456784567845675678456785678");

    public static final long LONG_MIN = 6;

    public static final long LONG_MAX = 21;

    public static final int STRING_MIN_LENGTH = 3;

    @InjectPage("FieldsResults")
    public abstract FieldsResults getResultsPage();

    public IPage doSubmit()
    {
        return getResultsPage();
    }
    
    public void doByLink()
    {
        getResultsPage().setByLink(true);
    }
    
    @InjectState("session-data")
    public abstract Visit getVisit();
}
