// Copyright Aug 2, 2006 The Apache Software Foundation
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
package org.apache.tapestry.html;

/**
 * Defines a relationship between two documents.
 * 
 * @author andyhot
 */
public class RelationBean
{
    /** The target URL of the resource. */
    private String _href;
    /** Specifies on what device the document will be displayed. */
    private String _media;
    /** Defines the relationship between the current document and the targeted document. */
    private String _rel;
    /** Defines the relationship between the targeted document and the current document. */
    private String _rev;
    /** Specifies the MIME type of the target URL. */
    private String _type;
    
    public String getHref()
    {
        return _href;
    }
    
    public void setHref(String href)
    {
        _href = href;
    }
    
    public String getMedia()
    {
        return _media;
    }
    
    public void setMedia(String media)
    {
        _media = media;
    }
    
    public String getRel()
    {
        return _rel;
    }
    
    public void setRel(String rel)
    {
        _rel = rel;
    }
    
    public String getRev()
    {
        return _rev;
    }
    
    public void setRev(String rev)
    {
        _rev = rev;
    }
    
    public String getType()
    {
        return _type;
    }
    
    public void setType(String type)
    {
        _type = type;
    }
}
