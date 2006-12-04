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
package org.apache.tapestry.asset;


/**
 * Wrapper around cached asset resource.
 * 
 * @author jkuhnert
 */
public class CachedAsset
{
    
    /**
     * The raw data for this resource.
     */
    private byte[] _data;
    
    /**
     * The gzipped version of the raw data.
     */
    private byte[] _gzipData;
    
    /**
     * Path to the resource.
     */
    private String _path;
    
    /**
     * The last known modification time of the data this cached object
     * represents. Is used to invalidate cache entries.
     */
    private long _lastModified;
    
    /**
     * Creates a new cachable asset entry. 
     * 
     * @param path
     *          The path string of the resource.
     * @param lastModified
     *          The last known modification time of the data this cached object
     *          represents. Is used to invalidate cache entries.
     * @param data
     *          The data representation to cache.
     * @param gzipData
     *          The optional gzip'ed data.
     */
    public CachedAsset(String path, long lastModified, byte[] data, byte[] gzipData)
    {
        _path = path;
        _lastModified = lastModified;
        _data = data;
        _gzipData = gzipData;
    }
    
    /**
     * @return Returns the data.
     */
    public byte[] getData()
    {
        return _data;
    }
    
    /**
     * @param data The data to set.
     */
    public void setData(byte[] data)
    {
        _data = data;
    }

    
    /**
     * @return Returns the gzipData.
     */
    public byte[] getGzipData()
    {
        return _gzipData;
    }

    
    /**
     * @param gzipData The gzipData to set.
     */
    public void setGzipData(byte[] gzipData)
    {
        _gzipData = gzipData;
    }

    
    /**
     * @return Returns the path.
     */
    public String getPath()
    {
        return _path;
    }
    
    /**
     * @return Returns the lastModified.
     */
    public long getLastModified()
    {
        return _lastModified;
    }
    
    /**
     * Clears the currently cached data and resets the last modified time.
     * 
     * @param lastModified The lastModified to set.
     */
    public void clear(long lastModified)
    {
        _lastModified = lastModified;
        _data = null;
        _gzipData = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_path == null) ? 0 : _path.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CachedAsset other = (CachedAsset) obj;
        if (_path == null) {
            if (other._path != null) return false;
        } else if (!_path.equals(other._path)) return false;
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        String ret = "CachedAsset [path: " + _path;
        
        if (_data != null)
            ret += ", data size(bytes): " + _data.length;
        if (_gzipData != null)
            ret += ", gzip data size(bytes): " + _gzipData.length;
        
        ret += ", lastModified(ms): " + _lastModified + "]";
        
        return ret;
    }
}
