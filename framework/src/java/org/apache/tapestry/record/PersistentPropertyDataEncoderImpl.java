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

package org.apache.tapestry.record;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.Defense;

/**
 * Responsible for converting lists of {@link org.apache.tapestry.record.PropertyChange}s back and
 * forth to a URL safe encoded string.
 * <p>
 * A possible improvement would be to encode the binary data with encryption both on and off, and
 * select the shortest (prefixing with a character that identifies whether encryption should be used
 * to decode).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PersistentPropertyDataEncoderImpl implements PersistentPropertyDataEncoder
{

    public String encodePageChanges(List changes)
    {
        Defense.notNull(changes, "changes");

        if (changes.isEmpty())
            return "";

        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(bos);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(gos));

            writeChangesToStream(changes, oos);

            oos.close();

            byte[] encoded = Base64.encodeBase64(bos.toByteArray());

            return new String(encoded);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    public List decodePageChanges(String encoded)
    {
        if (HiveMind.isBlank(encoded))
            return Collections.EMPTY_LIST;

        try
        {
            byte[] decoded = Base64.decodeBase64(encoded.getBytes());

            ByteArrayInputStream bis = new ByteArrayInputStream(decoded);
            GZIPInputStream gis = new GZIPInputStream(bis);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(gis));

            List result = readChangesFromStream(ois);

            ois.close();

            return result;
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private void writeChangesToStream(List changes, ObjectOutputStream oos) throws IOException
    {
        oos.writeInt(changes.size());

        Iterator i = changes.iterator();
        while (i.hasNext())
        {
            PropertyChange pc = (PropertyChange) i.next();

            String componentPath = pc.getComponentPath();
            String propertyName = pc.getPropertyName();
            Object value = pc.getNewValue();

            oos.writeBoolean(componentPath != null);

            if (componentPath != null)
                oos.writeUTF(componentPath);

            oos.writeUTF(propertyName);
            oos.writeObject(value);
        }
    }

    private List readChangesFromStream(ObjectInputStream ois) throws IOException,
            ClassNotFoundException
    {
        List result = new ArrayList();

        int count = ois.readInt();

        for (int i = 0; i < count; i++)
        {
            boolean hasPath = ois.readBoolean();
            String componentPath = hasPath ? ois.readUTF() : null;
            String propertyName = ois.readUTF();
            Object value = ois.readObject();

            PropertyChangeImpl pc = new PropertyChangeImpl(componentPath, propertyName, value);

            result.add(pc);
        }

        return result;
    }
}