<!-- $Id$ -->
<%@ page import="java.util.*" %>
<html>
<head>
<title>Launch Vlib</title>
</head>
<body>

<h1>Primix Virtual Library</h1>

<p>There are two versions of the Virtual Library; the first uses the
<a href="http://sourceforge.net/projects/tapestry">Tapestry</a> framework.
<p>The second implementation is built using traditional JSPs and servlets.
<p>Both use the same set of Enterprise JavaBeans to provide access to the
backend database.  Currently, the database is in Cloudscape and the application server is
WebLogic.  <a href="http://sourceforge.net/projects/jetty">Jetty 3</a> is the
servlet container. 

<ul>
<li><a href="app">Start Tapestry Application</a>
<li><a href="home">Start JSP Application</a>
</ul>

<p>Current time: <%= new Date() %>

</body>
</html>