<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>
<title>Spring Boot</title>
</head>
<body>
  <img src="src/main/resources/AARMRD-v2.png">
  <h1>Anonymization application Implementation</h1>
  <hr>

  <h2>Your name is ${name}.</h2><br>
  This is an example of spring finally working on<br>
  a local Tomcat 8.5 server, built with Maven
  <br><br>
  Upload the file and hierarchies you want to use here
  <br><br>
  <div class="form">
    <form action="home" method="POST" onsubmit="return validate()">
      <table>
        <tr>
          <td>Continue to skeleton Homepage</td>
          <td><input type="submit" value="Submit"></td>
        </tr>
      </table>
    </form>
  </div>
</body>
<script>
</script>
</html>