//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.junit.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.enum.Enum;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.AssetType;
import net.sf.tapestry.spec.Direction;

/**
 *  Tests the ability of an {@link net.sf.tapestry.util.Enum} 
 *  to be serialized and deserialized properly.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TestEnum extends TapestryTestCase
{
    public TestEnum(String name)
    {
        super(name);
    }

    public void testSerialization() throws Exception
    {
        AssetType start = AssetType.EXTERNAL;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(start);

        oos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        AssetType result = (AssetType) ois.readObject();

        assertEquals(start, result);
        assertSame(start, result);
    }

    public void testToString()
    {
        assertEquals("AssetType[EXTERNAL]", AssetType.EXTERNAL.toString());
        assertEquals("Direction[CUSTOM]", Direction.CUSTOM.toString());
    }

}