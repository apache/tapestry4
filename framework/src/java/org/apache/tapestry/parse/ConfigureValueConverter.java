// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

import org.apache.hivemind.Location;

/**
 * Defines an interface used to convert a string input value
 * (obtained from a specification) into a particular type
 * (that can be assigned as a property).
 *
 * @author Howard Lewis Ship
 */
interface ConfigureValueConverter
{
    public Object convert(String value, Location location);
}
