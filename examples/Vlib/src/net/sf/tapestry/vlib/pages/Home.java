package net.sf.tapestry.vlib.pages;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.vlib.IErrorProperty;

/**
 *  The home page for the application, it's primary purpose is
 *  to provide a book search form.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class Home extends BasePage implements IErrorProperty
{
	private String searchTitle;
	private Object searchPublisherPK;
	private String error;
	private String message;
	private String searchAuthor;

	public void detach()
	{
		searchTitle = null;
		searchPublisherPK = null;
		error = null;
		message = null;
		searchAuthor = null;

		super.detach();
	}

	public String getSearchTitle()
	{
		return searchTitle;
	}

	public void setSearchTitle(String value)
	{
		searchTitle = value;
	}

	public String getSearchAuthor()
	{
		return searchAuthor;
	}

	public void setSearchAuthor(String value)
	{
		searchAuthor = value;
	}

	public Object getSearchPublisherPK()
	{
		return searchPublisherPK;
	}

	public void setSearchPublisherPK(Object value)
	{
		searchPublisherPK = value;
	}

	public void setError(String value)
	{
		error = value;
	}

	public String getError()
	{
		return error;
	}

	public void setMessage(String value)
	{
		message = value;
	}

	public String getMessage()
	{
		return message;
	}

	/**
	 *  Invokes {@link Matches#performQuery(String,String,Object,IRequestCycle)}.
	 *
	 **/

	public void search(IRequestCycle cycle)
	{
		Matches matches;

		matches = (Matches) cycle.getPage("Matches");

		matches.performQuery(searchTitle, searchAuthor, searchPublisherPK, cycle);
	}

}