<%-- $Id$ --%>
<%@ page import="com.primix.vlib.ejb.*" %>
<%@ include file="Border.jsp" %>
<%
	PersonDelegate delegate = PersonDelegate.get(context);
	Person person = delegate.getPerson();
	Book[] books = delegate.getBooks();
%>

<table>
	<tr>
		<th>Name</th>
		<td><% writer.print(person.getName()); %></td>
	</tr>
	<tr>
		<th>Email</th>
		<td><% delegate.writeEmail(writer); %></td>
	</tr>
</table>

<p><%= books.length %> books.
<%
	for (int i = 0; i < books.length; i++)
	{
		if (i == 0)
		{
%>
<table>
	<tr>
		<th>Title</th>
		<th>Author</th>
		<th>Publisher</th>
		<th>Borrowed By</th>
	</tr>
<%
		}
%>
	<tr>
		<td><% BookServlet.writeLink(context, writer, book); %></td>
		<td><% writer.print(book.getAuthor()); %></td>
		<td><% writer.print(book.getPublisherName()); %></td>
		<td><%
		
		if (application.showHolder(book))
			PersonServlet.writeLink(context, writer,
				book.getHolderPrimaryKey(),
				book.getHolderName());
%>
		</td>
		<td><% HomeServlet.writeBorrowLink(context, writer, book); %></td>
	</tr>
<%
	} // for i

	if (books.length > 0)
	{
%>
</table>
<%
	} // if
%>
<%@ include file="Border-trailer.jsp" %>
