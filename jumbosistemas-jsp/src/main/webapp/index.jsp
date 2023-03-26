<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
	

<title>jumbo sistemas - jsp</title>

<style type="text/css">

form{
 position: absolute;
 top: 40%;
 left: 33%;
 right: 33%;
}


h5{
 position: absolute;
 top: 30%;
 left: 33%;
}

.msg{
 position: absolute;
 top: 10%;
 left: 33%;
 font-size: 15px;
   color: #664d03;
    background-color: #fff3cd;
    border-color: #ffecb5;

}
</style>	
	
</head>
<body>

<form action="<%=request.getContextPath() %>/ServletLogin" method="post" class="row g-3 needs-validation" novalidate>

<input type="hidden" value="<%= request.getParameter("url") %>" name="url">

<div class="mb-3">
  <label class="form-label" for="login">Login</label>
  <input class="form-control" id="login" name="login" type="text" required>
   <div class="invalid-feedback">
      Obrigatório
    </div>
     <div class="valid-feedback">
      ok
    </div>
 </div> 
 
 <div class="mb-3">
  <label class="form-label" for="senha">Senha</label> 
  <input class="form-control" id="senha" name="senha" type="password" required>
   <div class="invalid-feedback">
      Obrigatório
    </div>
    <div class="valid-feedback">
      ok
    </div>
 </div>

  <input type="submit" value="Acessar" class="btn btn-primary">

</form>
<h5 class="msg">${msg}</h5>

   <!-- Option 1: Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>


<script type="text/javascript">
//Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
  'use strict'

  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  var forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.prototype.slice.call(forms)
    .forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        }

        form.classList.add('was-validated')
      }, false)
    })
})()

</script>

</body>
</html>