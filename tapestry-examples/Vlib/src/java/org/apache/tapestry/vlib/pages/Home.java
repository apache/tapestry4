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

package org.apache.tapestry.vlib.pages;

import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.vlib.VlibPage;
import org.apache.tapestry.vlib.ejb.MasterQueryParameters;

/**
 * The home page for the application, it's primary purpose is to provide a book
 * search form.
 * 
 * @author Howard Lewis Ship
 */

@Meta( { "page-type=Search", "anonymous-access=true" })
public abstract class Home extends VlibPage
{

    public abstract String getTitle();

    public abstract String getAuthor();

    public abstract Integer getPublisherId();

    public abstract Integer getOwnerId();

    @InjectPage("BookMatches")
    public abstract BookMatches getBookMatches();

    /**
     * Listener method, invokes
     * {@link BookMatches#performQuery(MasterQueryParameters)}.
     */

    public void search()
    {
        BookMatches matches = getBookMatches();

        MasterQueryParameters parameters = new MasterQueryParameters(
                getTitle(), getAuthor(), getOwnerId(), getPublisherId());

        matches.performQuery(parameters);
    }

}
