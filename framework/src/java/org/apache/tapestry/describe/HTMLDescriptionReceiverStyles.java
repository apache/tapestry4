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

package org.apache.tapestry.describe;

/**
 * Holds the style information used by {@link org.apache.tapestry.describe.HTMLDescriptionReceiver}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class HTMLDescriptionReceiverStyles
{
    private String _tableClass = "described-object";

    private String _headerClass = "described-object-title";

    private String _subheaderClass = "section";

    public String getHeaderClass()
    {
        return _headerClass;
    }

    public void setHeaderClass(String headerClass)
    {
        _headerClass = headerClass;
    }

    public String getSubheaderClass()
    {
        return _subheaderClass;
    }

    public void setSubheaderClass(String subheaderClass)
    {
        _subheaderClass = subheaderClass;
    }

    public String getTableClass()
    {
        return _tableClass;
    }

    public void setTableClass(String tableClass)
    {
        _tableClass = tableClass;
    }
}