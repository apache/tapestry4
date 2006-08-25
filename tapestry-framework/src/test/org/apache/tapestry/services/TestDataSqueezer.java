// Copyright May 12, 2006 The Apache Software Foundation
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
package org.apache.tapestry.services;

import org.apache.hivemind.Registry;
import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;


/**
 * 
 *
 */
@Test
public class TestDataSqueezer extends BaseComponentTestCase {

    public void testPipeline() 
    throws Exception 
    {
        final Registry reg = buildFrameworkRegistry( "squeezer-pipeline.xml" );
        final DataSqueezer squeezer = ( DataSqueezer )reg.getService( DataSqueezer.class );
        
        final String squeezedValue = squeezer.squeeze( "Hello, World!" );

        assertTrue( squeezedValue.startsWith( "dummy:" ) );
    }
}
