package net.sf.tapestry.junit.mock.c6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.tapestry.engine.BaseEngine;

/**
 *  Provides a sorted list of active pages.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class C6Engine extends BaseEngine
{
    public List getSortedActivePageNames()
    {
        List result = new ArrayList(getActivePageNames());

        Collections.sort(result);

        return result;
    }
}
