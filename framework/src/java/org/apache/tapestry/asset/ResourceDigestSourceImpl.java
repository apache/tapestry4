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

package org.apache.tapestry.asset;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.IOUtils;
import org.apache.tapestry.event.ResetEventListener;

/**
 * Implementation of {@link org.apache.tapestry.asset.ResourceDigestSource}&nbsp;that calculates an
 * DIGEST checksum digest and converts it to a string of hex digits.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ResourceDigestSourceImpl implements ResourceDigestSource, ResetEventListener
{
    private ClassResolver _classResolver;

    private static final int BUFFER_SIZE = 5000;

    /**
     * Map keyed on resource path of DIGEST checksum (as a string).
     */

    private final Map _cache = new HashMap();

    public synchronized String getDigestForResource(String resourcePath)
    {
        String result = (String) _cache.get(resourcePath);

        if (result == null)
        {
            result = computeMD5(resourcePath);
            _cache.put(resourcePath, result);
        }

        return result;
    }

    public synchronized void resetEventDidOccur()
    {
        _cache.clear();
    }

    private String computeMD5(String resourcePath)
    {
        URL url = _classResolver.getResource(resourcePath);

        if (url == null)
            throw new ApplicationRuntimeException(AssetMessages.noSuchResource(resourcePath));

        InputStream stream = null;

        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            stream = new BufferedInputStream(url.openStream());

            digestStream(digest, stream);

            stream.close();
            stream = null;

            byte[] bytes = digest.digest();
            char[] encoded = Hex.encodeHex(bytes);

            return new String(encoded);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(AssetMessages.unableToReadResource(
                    resourcePath,
                    ex));
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        finally
        {
            IOUtils.close(stream);
        }
    }

    private void digestStream(MessageDigest digest, InputStream stream) throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true)
        {
            int length = stream.read(buffer);

            if (length < 0)
                return;

            digest.update(buffer, 0, length);
        }
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }
}