<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>
<title>Spring Boot</title>
</head>
<body>
  <h1>Anonymization application Homepage</h1>
  <hr>

  <h2>${message}, ${name}.</h2><br>
  There's nothing here atm, but hopefully this will have<br>
  the components needed to do my first anonymization at some point!
  <div id="data">
  	<div id="import">
  		<input type="file" id="dataFile" title="This is where the data will go" value="get your data here" onmouseover="displayBasic()">
  	</div>
  This is where the data should go eventually
  </div>
  <div id="hierarchy">
  	<div id="import">
  		<input type="file" id="hierFile" title="This is where hierarchies will go" value="get your hierarchy here" onmouseover="displayBasic1()">
  	</div>
  This is where hierarchies should go eventually
  </div>
  <div class="form">
    <form action="home" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Continue to skeleton Anonymization Screen</td>
          <td><input type="button" value="button" onclick="fooledYou();"></td>
        </tr>
      </table>
    </form>
  </div>
<script>
function displayBasic() {
    document.getElementById("dataFile").style.color = "green";
}
function displayBasic1() {
    document.getElementById("hierFile").style.color = "green";
}
function fooledYou() {
	alert("It doesn't actually exist yet");
}

</script>
</body>
</html>