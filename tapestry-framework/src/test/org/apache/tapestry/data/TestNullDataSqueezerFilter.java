// Copyright May 31, 2006 The Apache Software Foundation
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
package org.apache.tapestry.data;

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.services.DataSqueezer;
import org.testng.annotations.Test;


/**
 * 
 * @author jcarman
 */
@Test
public class TestNullDataSqueezerFilter extends BaseComponentTestCase
{
    public void testSqueezeNull()
    {
        final NullDataSqueezerFilter filter = new NullDataSqueezerFilter();
        
        assertNull( filter.unsqueeze( filter.squeeze(( Object )null,null), null ) );
    }
    
    public void testSqueezeNonNull()
    {
        final NullDataSqueezerFilter filter = new NullDataSqueezerFilter();
        DataSqueezer mockSqueezer = newMock(DataSqueezer.class);
        
        expect(mockSqueezer.squeeze("Hello")).andReturn("World");
        
        replay();
        
        assertEquals("World", filter.squeeze("Hello", mockSqueezer));
        
        verify();
    }
}
