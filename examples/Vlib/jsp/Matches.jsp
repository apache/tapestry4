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
	
		writer.begin("a");
		writer.attribute("href", "/view/" + book.getPrimaryKey());
		writer.print(book.getTitle());
		writer.end();
%></td>
		<td><%= book.getAuthor() %></td>
		<td><%= book.getPublisherName() %></td>
		<td><%
		writer.begin("a");
		writer.attribute("href", "/person/" + ownerPK);
		writer.print(book.getOwnerName());
		writer.end();
%>
		</td>
		<td>
<%
		if (! ownerPK.equals(holderPK))
		{
			writer.begin("a");
			writer.attribute("href", "/person/" + holderPK);
			writer.print(book.getHolderName());
			writer.end();
		}
%>
		</td>
		<td>
<% 
		boolean enableBorrow = ! vlib.isLoggedInUser(holderPK);
	
		if (enableBorrow)
		{
			writer.begin("a");
			writer.attribute("href", "/home/borrow/" + book.getPrimaryKey());
			writer.closeTag();
		}
%>[ Borrow ]
<%
		if (enableBorrow)
			writer.end();
%></td>
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
