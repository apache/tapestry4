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

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.util.DescribedLocation;

/**
 * An implementation that accepts a comma separated String for
 * files, formFiles and widgetFiles. 
 *
 * @author Andreas Andreou
 * @since 4.1.4
 */
public class JavascriptManagerImpl implements JavascriptManager
{
    private AssetSource _assetSource;
    private List _files;
    private List _formFiles;
    private List _widgetFiles;
    private IAsset _path;
    private IAsset _tapestryFile;
    private IAsset _tapestryPath;

    public JavascriptManagerImpl()
    {
        _files = new ArrayList();
        _formFiles = new ArrayList();
        _widgetFiles = new ArrayList();
    }

    public IAsset getFirstAsset()
    {
        return findFirst(_files);
    }

    public IAsset getFirstFormAsset()
    {
        return findFirst(_formFiles);
    }

    public IAsset getFirstWidgetAsset()
    {
        return findFirst(_widgetFiles);
    }

    public List getAssets()
    {
        return _files;
    }

    public List getFormAssets()
    {
        return _formFiles;
    }

    public List getWidgetAssets()
    {
        return _widgetFiles;
    }

    public IAsset getPath()
    {
        return _path;
    }

    public IAsset getTapestryAsset()
    {
        return _tapestryFile;
    }

    public IAsset getTapestryPath()
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

    public void setFolder(String path)
    {
        _path = findAsset(path, "folder");
    }

    public void setTapestryFile(String tapestryFile)
    {
        _tapestryFile = findAsset(tapestryFile, "tapestryFile");
    }

    public void setTapestryFolder(String tapestryPath)
    {
        _tapestryPath = findAsset(tapestryPath, "tapestryFolder");
    }

    public void setAssetSource(AssetSource assetSource)
    {
        _assetSource = assetSource;
    }

	public void renderLibraryResources(IMarkupWriter writer,
			IRequestCycle cycle, boolean hasForm, boolean hasWidget) 
	{
        // include all the main js packages		
        appendAssetsAsJavascript(writer, cycle, getAssets());        
        
        if (hasForm)
        {
            appendAssetsAsJavascript(writer, cycle, getFormAssets());
        }
        if (hasWidget)
        {
            appendAssetsAsJavascript(writer, cycle, getWidgetAssets());
        }		
		
	}    
    
    public void renderLibraryAdaptor(IMarkupWriter writer, IRequestCycle cycle) 
    {
    	// include the tapestry js
        IAsset tapestryAsset = getTapestryAsset();
        if (tapestryAsset!=null)
        {
            appendAssetAsJavascript(writer, cycle, tapestryAsset);
        }    	
	}


	
    /**
     * Appends a script tag to include the given asset. 
     * @param writer
     * @param cycle
     * @param asset
     */
    protected void appendAssetAsJavascript(IMarkupWriter writer, IRequestCycle cycle, IAsset asset)
    {
        final String url = asset.buildURL();
        
        writer.begin("script");
        writer.attribute("type", "text/javascript");
        writer.attribute("src", url);
        writer.end();
        writer.println();
    }        

    protected void appendAssetsAsJavascript(IMarkupWriter writer, IRequestCycle cycle, List jsAssets)
    {
        for (int i = 0; i < jsAssets.size(); i++)
        {
            appendAssetAsJavascript(writer, cycle, (IAsset) jsAssets.get(i));
        }
    }	

	/**
     * Builds a {@link List} of {@link IAsset} from a comma
     * separated input string.
     * 
     * @param files A comma separated string.
     * @param name Description of assets.
     * @return
     */
    protected List buildAssetList(String files, String name)
    {
        String[] js = TapestryUtils.split(files);
        
        List list = new ArrayList(js.length);
        for (int i=0; i<js.length; i++) {
            list.add(findAsset(js[i], name + i));
        }

        return list;
    }

    /**
     * Finds the given asset (in classpath, context, e.t.c.).
     * 
     * @param path
     * @param description
     * @return
     */
    protected IAsset findAsset(String path, String description)
    {
        IAsset asset = null;
        if ( !HiveMind.isBlank(path) )
        {
            Location location = new DescribedLocation(new URLResource(path), description);
            asset = _assetSource.findAsset(null, path, null, location);
        }
        return asset;
    }

    private IAsset findFirst(List list)
    {
        if (list == null || list.isEmpty())
            return null;
        else
            return (IAsset) list.get(0);
    }
}
