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
		writer.attribute("href", 
			response.encodeURL("/view/" + book.getPrimaryKey()));
		writer.print(book.getTitle());
		writer.end();
		
		// Use writer.print() because we don't know what invalid
		// chars may be in the author, publisher, etc.
		
%></td>
		<td><%  writer.print(book.getAuthor()); %></td>
		<td><%  writer.print(book.getPublisherName()); %></td>
		<td><%
		writer.begin("a");
		writer.attribute("href", response.encodeURL("/person/" + ownerPK));
		writer.print(book.getOwnerName());
		writer.end();
%>
		</td>
		<td>
<%
		if (! ownerPK.equals(holderPK))
		{
			writer.begin("a");
			writer.attribute("href", response.encodeURL("/person/" + holderPK));
			writer.print(book.getHolderName());
			writer.end();
		}
%>
		</td>
		<td>
<% 
		boolean enableBorrow = delegate.getEnableBorrow(book);
	
		if (enableBorrow)
		{
			writer.begin("a");
			writer.attribute("href", 
				response.encodeURL("/home/borrow/" + book.getPrimaryKey()));
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
