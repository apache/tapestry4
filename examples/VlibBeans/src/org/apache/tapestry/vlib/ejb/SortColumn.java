package org.apache.tapestry.vlib.ejb;

import org.apache.commons.lang.enum.Enum;

/**
 *  Represents the different columns which may be sorted.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class SortColumn extends Enum
{
	/**
	 *  Sort by book title.
	 * 
	 **/
	
	public static final SortColumn TITLE = new SortColumn("TITLE");
	
	/**
	 *  Sort by author name.
	 *  
	 **/
	
	public static final SortColumn AUTHOR = new SortColumn("AUTHOR");

	/**
	 *  Sort by publisher name.
	 * 
	 **/
	
	public static final SortColumn PUBLISHER = new SortColumn("PUBLISHER");
	
	/**
	 *  Sort by holder name (last name, then first).  Not applicable
	 *  to all queries.
	 * 
	 **/
	
	public static final SortColumn HOLDER = new SortColumn("HOLDER");

	/**
	 *  Sort by book owner (last name, then first).  Not applicable
	 *  to all queries.
	 * 
	 **/
	
	public static final SortColumn OWNER = new SortColumn("OWNER");
	
    private SortColumn(String name)
    {
        super(name);
    }

}
