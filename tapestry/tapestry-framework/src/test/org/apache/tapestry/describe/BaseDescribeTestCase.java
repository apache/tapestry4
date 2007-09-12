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

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Base class for testing implementations of
 * {@link org.apache.tapestry.describe.DescribableStrategy} and
 * {@link org.apache.tapestry.describe.RenderStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class BaseDescribeTestCase extends BaseComponentTestCase
{

    protected IMarkupWriter newWriter()
    {
        return newMock(IMarkupWriter.class);
    }

    protected IRequestCycle newCycle()
    {
        return newMock(IRequestCycle.class);
    }

    protected HTMLDescriber newDescriber()
    {
        return newMock(HTMLDescriber.class);
    }

    protected DescriptionReceiver newReceiver()
    {
        return newMock(DescriptionReceiver.class);
    }

}
