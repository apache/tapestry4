<%-- $Id$ --%>
<%@ page import="com.primix.vlib.ejb.*" %>
<%@ include file="Border.jsp"%>
<%
	HomeDelegate delegate = HomeDelegate.get(context);

	Book[] matches = delegate.getMatches();
	
	writer.print(matches.length);
%> matches.
<%
	for (int i = 0; i < matches.length; i++)
	{
		if (i == 0)
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
		} %>
	<tr>
		<td><%
		
		Book book = matches[i];
		Integer ownerPK = book.getOwnerPrimaryKey();
		Integer holderPK = book.getHolderPrimaryKey();
	
		BookServlet.writeLink(context, writer, book); %>
		</td>
		<td><%  writer.print(book.getAuthor()); %></td>
		<td><%  writer.print(book.getPublisherName()); %></td>
		<td><%
		PersonServlet.writeLink(context, writer, ownerPK,
				book.getOwnerName());
%>
		</td>
		<td>
<%
		if (! ownerPK.equals(holderPK))
			PersonServlet.writeLink(context, writer, holderPK, book.getHolderName());
%>
		</td>
		<td>
<% 
		HomeServlet.writeBorrowLink(context, writer, book); 
%>
</td>
	</tr>

<%
		if (i + 1 == matches.length)
		{
%>
</table>
<%
		}
	}
%>
<%@ include file="Border-trailer.jsp" %>
