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
