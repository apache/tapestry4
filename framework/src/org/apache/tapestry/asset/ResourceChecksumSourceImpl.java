//Copyright 2005 The Apache Software Foundation
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
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.BinaryEncoder;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;

/**
 * Implementation of {@link org.apache.tapestry.asset.ResourceDigestSource} that calculates an
 * checksum using a message digest and configured encoder.
 * 
 * This code is based on code from Howard Lewis Ship from the upcoming 3.1 release.
 * 
 * @author Paul Ferraro
 * @since 3.0.3
 */
public class ResourceChecksumSourceImpl implements ResourceChecksumSource
{
    private static final int BUFFER_SIZE = 4096;

    private Map _cache = new HashMap();
    
    private String _digestAlgorithm;

    private BinaryEncoder _encoder;
    
    public ResourceChecksumSourceImpl(String digestAlgorithm, BinaryEncoder encoder)
    {
        _digestAlgorithm = digestAlgorithm;
        _encoder = encoder;
    }
    
    /**
     * Checksum is obtained from cache if possible.
     * If not, checksum is computed using {@link #computeChecksum(URL)}
     * @see org.apache.tapestry.asset.ResourceDigestSource#getChecksum(java.net.URL)
     */
    public String getChecksum(URL resourceURL)
    {
        synchronized (_cache)
        {
            String checksum = (String) _cache.get(resourceURL);
            
            if (checksum == null)
            {
                checksum = computeChecksum(resourceURL);
                
                _cache.put(resourceURL, checksum);
            }
            
            return checksum;
        }
    }
    
    /**
     * @see org.apache.tapestry.asset.ResourceDigestSource#reset()
     */
    public void reset()
    {
        synchronized (_cache)
        {
            _cache.clear();
        }
    }
    
    /**
     * Computes a message digest of the specified resource and encodes it into a string.
     * @param resourceURL the url of a resource
     * @return the checksum value of the specified resource
     */
    protected String computeChecksum(URL resourceURL)
    {
        try
        {
	        MessageDigest digest = MessageDigest.getInstance(_digestAlgorithm);
	        
	        InputStream inputStream = new BufferedInputStream(resourceURL.openStream(), BUFFER_SIZE);
	        
	        byte[] block = new byte[BUFFER_SIZE];
	        
	        int read = inputStream.read(block);
	        
	        while (read >= 0)
	        {
	            digest.update(block, 0, read);
	            
	            read = inputStream.read(block);
	        }
	        
	        inputStream.close();
	        
	        return new String(_encoder.encode(digest.digest()));
        }
        catch (Exception e)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("AssetService.checksum-compute-failure", resourceURL), e);
        }
    }
}