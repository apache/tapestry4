<%-- $Id$ --%>
<%@ include file="Border.jsp" %>
<%
	HomeDelegate delegate = HomeDelegate.get(context);

	value = (String)request.getAttribute("error");
	if (value != null)
	{
%>
<font color=red><%= value %></font>
<%
	}

	value = (String)request.getAttribute("message");
	if (value != null)
	{
%>
<p><font size=+1><%= value %></font>
<% 
	}
%>

<p>Search for books:

<form method=post action="<%= HomeServlet.getSearchURL(context) %>">
<table>

<tr>
<th>Title</th>
<td><input type="text" name="<%= delegate.TITLE_NAME %>" size="30" maxlength="100"></td>
</tr>

<tr>
<th>Author</th>
<td> <input type="text" name="<%= delegate.AUTHOR_NAME %>" size="30" maxlength="100"></td>
</tr>

<tr>
<th>Publisher</th>
<td><% delegate.writePublisherSelect(writer); %></td>
</tr>

<tr>
<td></td>
<td><input type=submit value=Search></td>
</tr>
</table>
</form>

<%@ include file="Border-trailer.jsp" %>

