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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.engine.ServiceEncoder;

/**
 * A contribution to the tapestry.url.ServiceEncoders configuration point, identifying some number
 * of {@link org.apache.tapestry.engine.ServiceEncoder}s, as well as the ordering of those
 * encoders.
 * 
 * @author Howard M. Lewis Ship
 */
public class ServiceEncoderContribution extends BaseLocatable
{
    private String _id;

    private String _before;

    public String getAfter()
    {
        return _after;
    }

    public void setAfter(String after)
    {
        _after = after;
    }

    public String getBefore()
    {
        return _before;
    }

    public void setBefore(String before)
    {
        _before = before;
    }

    public ServiceEncoder getEncoder()
    {
        return _encoder;
    }

    public void setEncoder(ServiceEncoder encoder)
    {
        _encoder = encoder;
    }

    public String getId()
    {
        return _id;
    }

    public void setId(String id)
    {
        _id = id;
    }

    private String _after;

    private ServiceEncoder _encoder;
}