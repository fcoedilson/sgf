<%@ page isErrorPage="true"%>

<br />

<center>
<h1><font color="red"> Oops! Falha nossa!</font></h1>
<br />
<br />

Erro: <a href="#"
	onclick="javascript:(document.all.divErro.style.display=='none')?document.all.divErro.style.display='block':document.all.divErro.style.display='none'">
<%=exception.getMessage()%></a><br />
<br />

<div id="divErro" style="display: none;">
<div align="left" class="form">
<%--
	while (exception != null) {
		out
				.println("<hr><br><b>" + exception.getMessage()
						+ "</b><br>");
		StackTraceElement ste[] = exception.getStackTrace();
		for (int i = 0; i < ste.length; i++) {
			out.println(ste[i] + "<br/>");
		}
		exception = exception.getCause();
	}
--%>
</div>
</div>
</center>