<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- $Id$ --%>
<%@ page import="com.primix.servlet.*" %>
<%@ page import="com.primix.vlib.jsp.*" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="/jsp/Error.jsp" %>
<%
	RequestContext context = RequestContext.get(request);
	HTMLWriter writer = new HTMLWriter(out);
	
	VirtualLibraryApplication vlib = (VirtualLibraryApplication)context.getSessionAttribute("application");
	
	boolean isLoggedIn = vlib.isUserLoggedIn();
	
	// Scratch value used in a few places	
	String value;
%>
<html>
<head>
<title>Primix Virtual Library
<%
	value = (String)request.getAttribute("page.subtitle");
	if (value != null)
	{
		writer.print(": ");
		writer.print(value);
	}
%>
</title>
</head>
<body>

<table border=0 cellspacing=0 cellpadding=4>
	<tr valign=top>
		<td width=200><h2>
<%
	value = (String)request.getAttribute("page.title");
	writer.print(value);
%>
		</h2>
		</td>

		<td>
		<% HomeServlet.writeLink(context, writer); %>
		</td>

		<td>
<%
	MyBooksServlet.writeLink(context, writer);
%>		</td>

		<td align=center>
<%

	boolean compressed = writer.compress(true);
	
	if (isLoggedIn)
	{
		writer.print("Logged in as: ");
		writer.beginOrphan("br");
		writer.print(vlib.getFullUserName());
	}
	else
	{
		LoginServlet.writeLink(context, writer);
	}
%>
		<td>
<%
	if (request.getAttribute("omit-logout") == null)
	{
		writer.begin("a");
		writer.attribute("href",
			response.encodeURL("/logout"));
		writer.print("[Logout]");
		writer.end();
	}
	
	writer.setCompressed(compressed);
%>		</td>
	</tr>
</table>
<%-- Include page's content here.  Then include Border-trailer.jsp --%>

