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

package org.apache.tapestry.l10n;

import java.util.Locale;

import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.l10n.DefaultResourceLocalizer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DefaultResourceLocalizerTest extends HiveMindTestCase
{
    public void testLocalizer()
    {
        Resource root = newResource();
        Resource localized = newResource();
        Locale locale = Locale.getDefault();

        root.getLocalization(locale);
        setReturnValue(root, localized);

        replayControls();

        Resource actual = new DefaultResourceLocalizer().findLocalization(root, locale);

        assertSame(localized, actual);

        verifyControls();
    }

    private Resource newResource()
    {
        return (Resource) newMock(Resource.class);
    }
}
