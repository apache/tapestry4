<%-- $Id$ --%>
<%@ page import="com.primix.vlib.ejb.*" %>
<%@ include file="Border.jsp"%>

<%
	MyBooksDelegate delegate = MyBooksDelegate.get(context);
	
	value = delegate.getError();
	
	if (value != null)
	{
		writer.begin("font");
		writer.attribute("color", "red");
		writer.print(value);
		writer.end();
	}
	
	value = delegate.getMessage();
	
	if (value != null)
	{
%>
<p><font size=+1><% writer.print(value); %></font>
<% 
	}
	
	Book[] books = delegate.getBooks();
	for (int i = 0; i < books.length; i++)
	{
		Book book = books[i];
		
		if (i == 0)
		{
%>
<table>
  <tr>
    <th>Title</th>
	<th>Author</th>
	<th>Publisher</th>
	<th>Borrowed By</th>
	<th></th>
	<th></th>
  </tr>
<%
		}
%>
  <tr>
     <td><% writer.print(book.getTitle()); %></td>
	 <td><% writer.print(book.getAuthor()); %></td>
	 <td><% writer.print(book.getPublisherName()); %></td>
	 <td>
<%
	if (application.showHolder(book))
		PersonServlet.writeLink(context, writer,
				book.getHolderPrimaryKey(),
				book.getHolderName());
%>
	 </td>
	 <td><a href="<%= response.encodeURL("/edit/" + book.getPrimaryKey()) %>">[Edit]</a></td>
	 <td><a href="<%= response.encodeURL("/delete/" + book.getPrimaryKey()) %>">[Delete]</a></td>
  </tr>
<%
	if (i + 1 == books.length)
	{
%>
</table>
<%
	}
	}
%>

<a href="<%= response.encodeURL("/add-book") %>">[Add new Book]</a>
<a href="<%= response.encodeURL("/edit-profile") %>">[Edit User Profile]</a>
<%@ include file="Border-trailer.jsp" %>
