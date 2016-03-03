<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<html>
<head>
    <title>lbyclds</title>
</head>
<body>
	<h3>MongoDB</h3>
	<form action="upload" method="POST" enctype="multipart/form-data">
            <input type="file" name="file" /><br>
            <input type="submit" class="btn" value="Upload" />
    </form>
    <form action="delete" method="POST">
            <input type="submit" value="Delete" />
    </form>

    <% if (request.getAttribute("msg") != null) { %>
       	<div><%= request.getAttribute("msg") %></div>
    <% } %>
    <% 
        List<String> result = (List<String>) request.getAttribute("result");
         if (result == null) {
        	 result = new ArrayList<String>();
         }

         for (String rs : result) {
      %>
        	<div><%= rs %></div>
      <% } %>
</body>
</html>