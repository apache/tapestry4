<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- $Id$ --%>
<%@ page import="com.primix.servlet.*" %>
<%@ page import="com.primix.foundation.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page isErrorPage="true" %>
<%
	RequestContext context = RequestContext.get(request);
	HTMLWriter writer = new HTMLWriter(out);
	ExceptionDescription[] exceptions;
	ExceptionAnalyzer analyzer = new ExceptionAnalyzer();
	
	exceptions = analyzer.analyze(exception);
%>
<html>
<head>
<title>Primix Virtual Library: Error</title>
</head>
<body>
<h1>An Application Error has Occurred</h1>

<table>
<%
	for (int i = 0; i < exceptions.length; i++)
	{
		ExceptionDescription desc = exceptions[i];
%>
<tr>
	<th>Exception</th>
	<td><% writer.print(desc.getExceptionClassName()); %></td>
</tr>
<tr>
	<th>Message</th>
	<td><% writer.print(desc.getMessage()); %></td>
</tr>
<%
		ExceptionProperty[] properties = desc.getProperties();
		
		if (properties != null)
		{
			for (int j = 0; j < properties.length; j++)
			{
				ExceptionProperty prop = properties[i];
%>
<tr>
	<th><% writer.print(prop.getName()); %></th>
	<td><% writer.print(prop.getValue()); %></td>
</tr>
<%
			}  // for j
		} // if

		if (i + 1 == exceptions.length)
		{
			String[] trace = desc.getStackTrace();
%>
<tr> <th colspan=2>Stack Trace:</th> </tr>
<tr>
	<td colspan=2>
<%
			for (int j = 0; j < trace.length; j++)
			{
				writer.beginOrphan("li");
				writer.print(trace[j]);
			}
%>
	</td>
</tr>
<%
		}  // if
		else
		{
			// Only print stack trace on deepest (last) exception, on others
			// use a blank row as a seperator.
%>
<tr> <th colspan=2>&nbsp;</th> </tr>
<%
		} // else
		
	} // for i
%>
</table>

<p>

<% context.write(writer); %>

</body>
</html>


