<%-- $Id$ --%>
<%@ include file="Border.jsp" %>

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

	Book[] books = delegate.getOwnedBooks();

	if (books != null)
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

		for (int i = 0; i < books.length; i++)
		{
			Book book = books[i];
			
%>
  <tr>
     <td><% writer.print(book.getTitle()); %></td>
	 <td><% writer.print(book.getAuthor()); %></td>
	 <td><% writer.print(book.getPublisherName()); %></td>
	 <td>
<%
		if (vlib.getShowHolder(book))
			PersonServlet.writeLink(context, writer,
					book.getHolderPrimaryKey(),
					book.getHolderName());
%>
	 </td>
	 <td>
	 <% VlibServlet.writeNYILink(context, writer, "[Edit]"); %>
	 </td>
	 <td>
	 <% VlibServlet.writeNYILink(context, writer, "[Delete]"); %>
	 </td>
  </tr>

<%
		}  // for i
%>
</table>
<%
	} // if books != null
	
	books = delegate.getBorrowedBooks();
	
	if (books != null)
	{
%>
<hr>

<p>Borrowed books:

<table>
  <tr>
    <th>Title</th>
	<th>Author</th>
	<th>Publisher</th>
	<th>Owner</th>
	<th></th>
<%
		for (int i = 0; i < books.length; i++)
		{
			Book book = books[i];
%>
	<tr>
		<td><%
		BookServlet.writeLink(context, writer, book); %>
		</td>
		<td><%  writer.print(book.getAuthor()); %></td>
		<td><%  writer.print(book.getPublisherName()); %></td>
		<td><%
		PersonServlet.writeLink(context, writer, 
				book.getOwnerPrimaryKey(),
				book.getOwnerName()); %>
		</td>
		<td><% MyBooksServlet.writeReturnLink(context, writer, book); %>
		</td>
	</tr>
<%
		} // for i
%>
</table>
<%
	}  // if books != null
%>

<p>
<% VlibServlet.writeNYILink(context, writer, "[Add new Book]"); %>
<% VlibServlet.writeNYILink(context, writer, "[Edit User Profile]"); %>

<%@ include file="Border-trailer.jsp" %>
