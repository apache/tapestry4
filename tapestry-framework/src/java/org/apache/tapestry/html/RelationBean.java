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
    /** Title of the relation. */
    private String _title;
    
    public RelationBean()
    {        
    }
    
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
    
    public String getTitle()
    {
        return _title;
    }
    
    public void setTitle(String title)
    {
        _title = title;
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_href == null) ? 0 : _href.hashCode());
        result = prime * result + ((_media == null) ? 0 : _media.hashCode());
        result = prime * result + ((_rel == null) ? 0 : _rel.hashCode());
        result = prime * result + ((_rev == null) ? 0 : _rev.hashCode());
        result = prime * result + ((_title == null) ? 0 : _title.hashCode());
        result = prime * result + ((_type == null) ? 0 : _type.hashCode());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final RelationBean other = (RelationBean) obj;
        if (_href == null) {
            if (other._href != null) return false;
        } else if (!_href.equals(other._href)) return false;
        if (_media == null) {
            if (other._media != null) return false;
        } else if (!_media.equals(other._media)) return false;
        if (_rel == null) {
            if (other._rel != null) return false;
        } else if (!_rel.equals(other._rel)) return false;
        if (_rev == null) {
            if (other._rev != null) return false;
        } else if (!_rev.equals(other._rev)) return false;
        if (_title == null) {
            if (other._title != null) return false;
        } else if (!_title.equals(other._title)) return false;
        if (_type == null) {
            if (other._type != null) return false;
        } else if (!_type.equals(other._type)) return false;
        return true;
    }  
    
    
}
