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

package org.apache.tapestry.junit.mock.c27;

import java.util.Locale;

import org.apache.tapestry.BaseComponent;

public abstract class Good extends BaseComponent
{
    public String getDefaultPrefix()
    {
        return "Default";
    }

    public Locale getDefaultLocale()
    {
        return Locale.US;
    }
    
    public abstract boolean isBooleanValue1();
    
}
