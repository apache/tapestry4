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

package org.apache.tapestry.workbench.palette;

import org.apache.tapestry.html.BasePage;

import java.util.List;

/**
 * @author Howard Lewis Ship
 * @since 1.0.7
 */

public abstract class PaletteResults extends BasePage
{

    public abstract void setSelectedColors(List value);

}
