//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.workbench.tree.examples.fsmodel;

import org.apache.tapestry.asset.PrivateAsset;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * Created on Oct 3, 2002
 *
 * @author ceco
 *
 */
public class AssetsHolder {

	private String m_strOpenAssetsURL;
	private String m_strCloseAssetsURL;
	private PrivateAsset m_objOpenAsset = null;
	private PrivateAsset m_objCloseAsset = null;
	/**
	 * Constructor for AssetsHolder.
	 */
	public AssetsHolder(String strOpenAssetsURL, String strCloseAssetsURL) {
		super();
		m_strOpenAssetsURL = strOpenAssetsURL;
		m_strCloseAssetsURL = strCloseAssetsURL;
	}
    public PrivateAsset getAssetForOpenNode(){
        if(m_objOpenAsset == null){
            //m_objOpenAsset = new PrivateAsset(m_strOpenAssetsURL);
            m_objOpenAsset =
                new PrivateAsset
                (new ClasspathResourceLocation
                 (new DefaultResourceResolver(), m_strOpenAssetsURL), null);
        }
        return m_objOpenAsset;
	}
	public PrivateAsset getAssetForCloseNode(){
		if(m_objCloseAsset == null){
                    //m_objCloseAsset = new PrivateAsset(m_strCloseAssetsURL);
                    m_objCloseAsset =
                        new PrivateAsset
                        (new ClasspathResourceLocation
                         (new DefaultResourceResolver(), m_strCloseAssetsURL),
                         null);
		}
		return m_objCloseAsset;
	}

}
