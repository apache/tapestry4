<%@ taglib uri="http://jakarta.apache.org/tapestry/tld/tapestry_1_0.tld" prefix="tapestry" %>
	
		<html>
<head>
<title>Tapestry JSP Tags</title>
<link rel="stylesheet" type="text/css" href="css/workbench.css">
</head>
<body>

<!-- This is where the tabs would go.  Alas, used private assets for
     the tabs. -->
     
<table class="tabs" cellspacing="0" border="0">
<tr height="20">
<td>&nbsp;</td>
</tr>
</table>

<table class="content" width="100%">
<tr>
<td>

<p>This page demostrates the use of the Tapestry JSP tag library.  It uses JSP tags to call into the Tapestry application.</p>


<h3>page tag</h3>

<blockquote>
	&lt;tapestry:page page="JSP"&gt;View Result&lt;/tapestry:page&gt;
</blockquote>

<tapestry:page page="JSP">View Result</tapestry:page>


<h3>page-url tag</h3>

<blockquote>
&lt;a href="&lt;tapestry:page-url page="JSP"/&gt;"&gt;View Result&lt;/a&gt;	
</blockquote>


<a href="<tapestry:page-url page="JSP"/>">View Result</a>



<h3>external tag (string parameter)</h3>

<blockquote>
&lt;tapestry:external page="JSPResults" parameters="literal string"&gt;View Result&lt;/tapestry:external&gt;
</blockquote>

<tapestry:external page="JSPResults" parameters="literal string">View Result</tapestry:external>

<h3>external tag (expression parameter)</h3>

<blockquote>
&lt;tapestry:external page="JSPResults" parameters="ognl: { new java.util.Date(), 21239495L, \"foo!\" }"&gt;View Result&lt;/tapestry:external&gt;
</blockquote>

<tapestry:external page="JSPResults" parameters="ognl: { new java.util.Date(), 21239495L, \"foo!\" }">View Result</tapestry:external>

<h3>external-url tag</h3>


<blockquote>
&lt;form method="post" action="&lt;tapestry:external-url page="JSPResults"/&gt;"&gt;	
</blockquote>

<form method="post" action="<tapestry:external-url page="JSPResults"/>">
<p>Enter your name:
<input type="text" name="userName" size="40"/>
</p>
</form>

<p>
You may <a href="app">return to the Workbench</a>.

</td>
</tr>
</table>
<p>


</body>
</html>
