<%-- $Id$ --%>
<%@ include file="Border.jsp"%>
<%

	// Remove unecessary whitespace, which looks ugly when <a> tags
	// are generated.
	
	writer.setCompressed(true);
	
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

<form method=post action="/home/search">

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

<%
	if (isLoggedIn)
	{
		writer.begin("a");
		writer.attribute("href", "/add");
		writer.closeTag();
	}
	
	writer.print("[Add new Book]");

	if (isLoggedIn)
		writer.end();
%>
<%@ include file="Border-trailer.jsp" %>

