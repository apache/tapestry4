// Copyright 2007 The Apache Software Foundation
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

package org.apache.tapestry.javascript;

import java.util.List;
import java.util.ArrayList;

import org.apache.hivemind.Location;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.util.DescribedLocation;

/**
 * @author Andreas Andreou
 * @since 4.1.4
 */
public class JavascriptManagerImpl implements JavascriptManager {

    public IAsset getMainJsAsset()
    {
        return findFirst(_files);
    }

    public IAsset getMainJsFormAsset()
    {
        return findFirst(_formFiles);
    }

    public IAsset getMainJsWidgetAsset()
    {
        return findFirst(_widgetFiles);
    }

    public List getJsAssets()
    {
        return _files;
    }

    public List getJsFormAssets()
    {
        return _formFiles;
    }

    public List getJsWidgetAssets()
    {
        return _widgetFiles;
    }

    public IAsset getJsPath()
    {
        return _path;
    }

    public IAsset getJsTapestryAsset()
    {
        return _tapestryFile;
    }

    public IAsset getJsTapestryPath()
    {
        return _tapestryPath;
    }

    public void setFiles(String files)
    {
        _files = buildAssetList(files, "files");
    }

    public void setFormFiles(String formFiles)
    {
        _formFiles = buildAssetList(formFiles, "formFiles");
    }

    public void setWidgetFiles(String widgetFiles)
    {
        _widgetFiles = buildAssetList(widgetFiles, "widgetFiles");
    }

    public void setPath(String path)
    {
        _path = findAsset(path, "path");
    }

    public void setTapestryFile(String tapestryFile)
    {
        _tapestryFile = findAsset(tapestryFile, "tapestryFile");
    }

    public void setTapestryPath(String tapestryPath)
    {
        _tapestryPath = findAsset(tapestryPath, "tapestryPath");
    }

    public void setAssetSource(AssetSource assetSource)
    {
        _assetSource = assetSource;
    }

    private List buildAssetList(String files, String name) {
        String[] js = TapestryUtils.split(files);
        List list = new ArrayList(js.length);
        for (int i=0; i<js.length; i++) {
            list.add(findAsset(js[i], name + i));
        }
        return list;
    }

    private IAsset findAsset(String path, String description)
    {
        IAsset asset = null;
        if (path!=null)
        {
            Location location = new DescribedLocation(new URLResource(path), description);
            asset = _assetSource.findAsset(null, path, null, location);
        }
        return asset;
    }

    private IAsset findFirst(List list) {
        if (list ==null || list.isEmpty())
            return null;
        else
            return (IAsset) list.get(0);
    }

    private List _files;
    private List _formFiles;
    private List _widgetFiles;
    private IAsset _path;
    private IAsset _tapestryFile;
    private IAsset _tapestryPath;

    private AssetSource _assetSource;
}
