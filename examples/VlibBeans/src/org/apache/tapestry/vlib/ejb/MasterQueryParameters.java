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

package org.apache.tapestry.vlib.ejb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Bundles up the parameters for the
 *  {@link org.apache.tapestry.vlib.ejb.IBookQuery#masterQuery(MasterQueryParameters, SortOrdering)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class MasterQueryParameters implements Serializable
{
	private static final long serialVersionUID = 557672936915184047L;
	
	private String _title;
	private String _author;
	private Integer _ownerId;
	private Integer _publisherId;
	
	public MasterQueryParameters(String title, String author, Integer ownerId, Integer publisherId)
	{
		_title = title;
		_author = author;
		_ownerId = ownerId;
		_publisherId = publisherId;
	}

	/** 
	 *  Returns a substring to match (caselessly) against
	 *  {@link IBook#getAuthor()}.
	 * 
	 **/
	
    public String getAuthor()
    {
        return _author;
    }

	/**
	 *  Returns a publisher Id to match, or null
	 *  for no publisher constraint.
	 * 
	 **/

    public Integer getPublisherId()
    {
        return _publisherId;
    }

	/**
	 *  Returns a substring to match
	 *  caselessly against the book title.
     *
     **/
	 
    public String getTitle()
    {
        return _title;
    }
    
    public Integer getOwnerId()
    {
    	return _ownerId;
    }

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("title", _title);
		builder.append("author", _author);
		builder.append("ownerId", _ownerId);
		builder.append("publisherId", _publisherId);
		
		return builder.toString();
	}
}
