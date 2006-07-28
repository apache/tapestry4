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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.services.DataSqueezer;

/**
 * The most complicated of the adaptors, this one takes an arbitrary serializable object, serializes
 * it to binary (possibly compressing the stream along the way), and encodes it in a Base64
 * encoding. The first character of the squeezed stream indicates whether it is or is not encoded.
 * 
 * @author Howard Lewis Ship
 */

public class SerializableAdaptor implements SqueezeAdaptor
{
    private static final char BYTESTREAM_PREFIX = 'O';

    private static final char GZIP_BYTESTREAM_PREFIX = 'Z';

    // O is for an object stream rendered as MIME
    // Z is for on object stream, compressed, rendered as MIME

    private static final String PREFIX = "OZ";

    private ClassResolver _resolver;
    
    public String getPrefix()
    {
        return PREFIX;
    }

    public Class getDataClass()
    {
        return Serializable.class;
    }

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        try
        {
            ByteArrayOutputStream bosPlain = new ByteArrayOutputStream();
            ByteArrayOutputStream bosCompressed = new ByteArrayOutputStream();

            GZIPOutputStream gos = new GZIPOutputStream(bosCompressed);

            TeeOutputStream tos = new TeeOutputStream(bosPlain, gos);

            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(tos));

            oos.writeObject(data);

            oos.close();

            boolean useCompressed = bosCompressed.size() < bosPlain.size();

            byte[] byteArray = useCompressed ? bosCompressed.toByteArray() : bosPlain.toByteArray();

            byte[] encoded = Base64.encodeBase64(byteArray);

            String prefix = Character.toString(useCompressed ? GZIP_BYTESTREAM_PREFIX
                    : BYTESTREAM_PREFIX);

            return prefix + new String(encoded);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(IoMessages.encodeFailure(data, ex), ex);
        }
    }

    public Object unsqueeze(DataSqueezer squeezer, String encoded)
    {
        char prefix = encoded.charAt(0);

        try
        {
            // Strip off the prefix, feed that in as a MIME stream.

            byte[] mimeData = encoded.substring(1).getBytes();

            byte[] decoded = Base64.decodeBase64(mimeData);

            InputStream is = new ByteArrayInputStream(decoded);

            if (prefix == GZIP_BYTESTREAM_PREFIX)
                is = new GZIPInputStream(is);

            is = new BufferedInputStream(is);

            ObjectInputStream ois = new ResolvingObjectInputStream(_resolver, is);

            Object result = ois.readObject();

            ois.close();

            return result;
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(IoMessages.decodeFailure(ex), ex);
        }
    }

    public void setResolver(ClassResolver resolver)
    {
        _resolver = resolver;
    }

}
