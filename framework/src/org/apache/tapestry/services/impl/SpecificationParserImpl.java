// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.services.impl;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.tapestry.services.SpecificationParser;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Note: must use service model pooled or threaded because the underlying
 * implementation is not threadsafe.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class SpecificationParserImpl implements SpecificationParser
{
    private org.apache.tapestry.parse.SpecificationParser _trueParser;
    private ClassResolver _classResolver;

    public void initializeService()
    {
        _trueParser = new org.apache.tapestry.parse.SpecificationParser(_classResolver);
    }

    public void setClassResolver(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

    public IApplicationSpecification parseApplicationSpecification(Resource resource)
    {
        return _trueParser.parseApplicationSpecification(resource);
    }

}
