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

package tutorial.pagetypes;

import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.StaleSessionException;
import net.sf.tapestry.StaleLinkException;
import org.apache.log4j.Logger;

import javax.swing.*;

/**
 *  @version $Id: Home.java,v 1.3 2003/01/13 03:33:40 hlship Exp $
 *  @author Neil Clayton
 *
 **/
public class Home extends BasePage {
    public void niceExceptionPage(IRequestCycle cycle) throws RequestCycleException {
        // This will cause the NewException page to be shown, due to the overrides
        // we have placed in the Engine used in this example
        throw new RequestCycleException("This exception is intentionally thrown by the link handler",
                this,
                new MyException("this is an intentional runtime exception"));
    }

    public void forceStaleSession(IRequestCycle cycle) throws RequestCycleException {
        log.warn("throwing stale session exception");
        throw new StaleSessionException("The session has expired", getPage());
    }

    public void forceStaleLink(IRequestCycle cycle) throws RequestCycleException {
        log.warn("throwing stale link exception");
        throw new StaleLinkException("This link is STALE!", this);
    }

    public void forceException(IRequestCycle cycle) throws RequestCycleException {
        log.warn("throwing some other exception");
        throw new RequestCycleException("This exception is intentionally thrown by the link handler",
                this,
                new RuntimeException("this is an intentional runtime exception"));
    }

    private static final Logger log = Logger.getLogger(Home.class);
}