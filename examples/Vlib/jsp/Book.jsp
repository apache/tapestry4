<%-- $Id --%>
<%@ page import="com.primix.vlib.ejb.*" %>
<%@ include file="Border.jsp" %>
<%
	BookDelegate delegate = BookDelegate.get(context);
	Book book = delegate.getBook();
%>
<table>
	<tr>
		<th>Title</th>
		<td><% writer.print(book.getTitle()); %></td>
	</tr>

	<tr>
		<th>Author</th>
		<td><% writer.print(book.getAuthor()); %></td>
	</tr>

	<tr>
		<th>Publisher</th>
		<td><% writer.print(book.getPublisherName()); %></td>
	</tr>

	<tr>
		<th>ISBN</th>
		<td><% writer.print(book.getISBN()); %></td>
	</tr>

	<tr>
		<th>Owner</th>
		<td>
<%
	PersonServlet.writeLink(context, writer,
				book.getOwnerPrimaryKey(),
				book.getOwnerName());
%>
		</td>
	</tr>

	<tr valign=top>
		<th>Description</th>
		<td><% writer.print(book.getDescription()); %></td>
	</tr>
<%
	boolean showHolder = delegate.getShowHolder();
	
	if (showHolder)
	{
%>
	<tr>
		<th>Borrowed by</th>
		<td>
<%
	PersonServlet.writeLink(context, writer,
				book.getHolderPrimaryKey(),
				book.getHolderName());
%>
		</td>
	</tr>
<%
	}  // if
%>
	<tr>
		<td></td>
		<td><% HomeServlet.writeBorrowLink(context, writer, book); %></td>
	</tr>
</table>

<%@ include file="Border-trailer.jsp" %>
