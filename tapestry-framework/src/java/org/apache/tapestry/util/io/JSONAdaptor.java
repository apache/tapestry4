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

package org.apache.tapestry.util.io;

import java.text.ParseException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.DataSqueezer;

/**
 * Squeezes a JSONObject
 */

public class JSONAdaptor implements SqueezeAdaptor
{

    private static final String PREFIX = "J";

    public String getPrefix()
    {
        return PREFIX;
    }

    public Class getDataClass()
    {
        return JSONObject.class;
    }

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        JSONObject o = (JSONObject) data;

        return PREFIX + o.toString();
    }

    /**
     * Build a JSONObject from the String
     */

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        if (string.length() == 1) return "";

        try {
            return new JSONObject(string.substring(1));
        } catch (ParseException ex) {
            throw new ApplicationRuntimeException(ex);
        }
    }
}
