<%-- $Id$ --%>
<%@ include file="Border.jsp" %>
<%
	LoginDelegate delegate = LoginDelegate.get(context);
%>
<form method="post" action="<%= LoginServlet.getLoginFormURL(context) %>">

<table>
	<tr>
		<td>&nbsp;</td>
		<td><font color=red><%
	writer.print(delegate.getError());
%></font></td>
	</tr>

	<tr>
		<th>Email Address:</th>
		<td> <input type="text" name="<%= delegate.EMAIL_NAME %>" size="30" maxlength="60" 
			value="<% writer.print(delegate.getEmail()); %>"></td>
	</tr>

	<tr>
		<th>Password:</th>
		<td><input type="password" name="<%= delegate.PASSWORD_NAME %>" size="10" maxlength="20"></td>
	</tr>

	<tr>
		<td></td>
		<td><input type=submit value="Login"></td>
	</tr>
</table>

</form>

<p>Not registered?  <% VlibServlet.writeNYILink(context, writer, "Register now"); %>.

<%@ include file="Border-trailer.jsp" %>
