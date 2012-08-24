<%
	response.setStatus(301);

	response.setHeader( "Location", "login.jsf" );

	response.setHeader( "Connection", "close" );
%>