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

package org.apache.tapestry.junit;

import ognl.DefaultTypeConverter;

import java.util.Map;
import java.util.Date;
import java.sql.Timestamp;

public class MockTypeConverter extends DefaultTypeConverter {
    public Object convertValue(Map context, Object value, Class toType) {
        Object result = null;

        if ((toType == Timestamp.class) && (value instanceof Date)) {
            return new Timestamp(((Date) value).getTime());
        } else {
            result = super.convertValue(context, value, toType);
        }
        return result;
    }
}
