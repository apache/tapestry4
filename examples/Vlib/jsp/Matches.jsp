<%-- $Id$ --%>
<%@ page import="com.primix.vlib.ejb.*" %>
<%@ include file="Border.jsp"%>
<%
	HomeDelegate delegate = HomeDelegate.get(context);

	Book[] matches = delegate.getMatches();
	
	writer.print(matches.length);
%> matches.
<%
	if (matches.length > 0)
	{
%>
<table>
	<tr>
		<th>Title</th>
		<th>Author</th>
		<th>Publisher</th>
		<th>Owner</th>
		<th>Borrowed By</th>
	</tr>
<%
	} // if 
	
	for (int i = 0; i < matches.length; i++)
	{
		Book book = matches[i];

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
				book.getOwnerName());
%>
		</td>
		<td>
<%
			if (vlib.getShowHolder(book))
				PersonServlet.writeLink(context, writer, 
					book.getHolderPrimaryKey(),
					book.getHolderName());
%>
		</td>
		<td>
<% 
		HomeServlet.writeBorrowLink(context, writer, book); 
%>
</td>
	</tr>

<%
	}  // for i
	
	if (matches.length > 0)
	{
%>
</table>
<%
	}
%>
<%@ include file="Border-trailer.jsp" %>
