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

package org.apache.tapestry.util.io;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.impl.DefaultClassResolver;

/**
 * Utility methods used when testing code that uses a
 * {@link org.apache.tapestry.services.DataSqueezer}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class DataSqueezerUtil
{

    /**
     * Returns a data squeezer with a set of basic adaptors, ready to be used by JUnit-tests.
     */
    public static DataSqueezerImpl createUnitTestSqueezer(ClassResolver resolver)
    {
        DataSqueezerImpl ds = new DataSqueezerImpl();

        ds.register(new BooleanAdaptor());
        ds.register(new ByteAdaptor());
        ds.register(new CharacterAdaptor());
        ds.register(new ComponentAddressAdaptor());
        ds.register(new DoubleAdaptor());
        ds.register(new FloatAdaptor());
        ds.register(new IntegerAdaptor());
        ds.register(new LongAdaptor());
        ds.register(new ShortAdaptor());
        ds.register(new StringAdaptor());

        SerializableAdaptor ser = new SerializableAdaptor();
        ser.setResolver(resolver);

        ds.register(ser);

        return ds;
    }

    /**
     * Returns a data squeezer with a set of basic adaptors, ready to be used by JUnit-tests.
     */

    public static DataSqueezerImpl createUnitTestSqueezer()
    {
        return createUnitTestSqueezer(new DefaultClassResolver());
    }

}
