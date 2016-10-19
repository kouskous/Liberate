$( document ).ready(function() {
	
var regexname=/^([a-zA-Z]{3,50})$/;
var valid = true;

$('#nom').bind('blur',function(e){ 
	//$("#emptyform").html("");
  if (!$(this).val().match(regexname)) 
	{
		$(this).next(".error_name").html("<div class='alert alert-error' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Veuillez entrer un nom correct</div>");
		valid =false;
	}

	else {
        $(this).next('.error_name').html("");
        $('#nom').removeClass("bordure");
	}
});
$('#prenom').bind('blur',function(e){ 
	//$("#emptyform").html("");
	if (!$(this).val().match(regexname))
	{
	$(this).next(".error_prenom").html("<div class='alert alert-error' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Veuillez entrer un prénom correct</div>");
	valid =false;
	}
	else {
        $(this).next('.error_prenom').fadeOut();
	}	
});
$('#pseudo').bind('blur',function(e){ 
	//$("#emptyform").html("");
if (!$(this).val().match(regexname))
	{
	$(this).next(".error_pseudo").html("<div class='offset1 span10'><div class='alert alert-error' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Veuillez entrer un pseudo valide</div></div>");
	valid =false;
	}
	else {
        $(this).next('.error_pseudo').fadeOut();
	}	
});
$('#mail').bind('blur',function(e){ 
	//$("#emptyform").html("");
	var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
if (!$(this).val().match(expr))
	{
	$(this).next(".error_mail").html("<div class='offset1 span10'><div class='alert alert-error' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Veuillez entrer une adresse mail valide</div></div>");
	valid =false;
	}
	else {
        $(this).next('.error_mail').fadeOut();
	}	
});


$('#password').on('keyup',function(e)
	{
		$('#result').html(checkStrength($('#password').val()))
	
	});	
$('#passwordconfirm').on('keyup',function(e)
	{
		$('#resultconfirm').html(checkequals($('#password').val(),$('#passwordconfirm').val()))
	});	
	/*
		checkStrength is function which will do the 
		main password strength checking for us
	*/
	
	function checkStrength(password)
	{
		//initialiser strength
		var strength = 0
		
		//la taille du mot de passe est inferieur à 6, returner un  message.
		if (password.length < 6) { 
			$('#result').removeClass()
			$('#result').addClass('offset3 span6 alert alert-error')
			return 'Trop court' 
		}
		//taille est ok, on continue.
		
		//si la taille est supérieur à 7 on incremente strength 
		if (password.length > 7) strength += 1
		
		//if password contains both lower and uppercase characters, increase strength value
		if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/))  strength += 1
		
		//if it has numbers and characters, increase strength value
		if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/))  strength += 1 
		
		//if it has one special character, increase strength value
		if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/))  strength += 1
		
		//if it has two special characters, increase strength value
		if (password.match(/(.*[!,%,&,@,#,$,^,*,?,_,~].*[!,%,&,@,#,$,^,*,?,_,~])/)) strength += 1
		
		//now we have calculated strength value, we can return messages
		
		//if value is less than 2
		if (strength < 2 )
		{
			$('#result').removeClass()
			$('#result').addClass('offset3 span6 alert alert-warning')
			return 'Votre mot de passe est moyen'			
		}
		else if (strength == 2 )
		{
			$('#result').removeClass()
			$('#result').addClass('offset3 span6 alert alert-info')
			return 'Votre mot de passe est bien'		
		}
		else
		{
			$('#result').removeClass()
			$('#result').addClass('offset3 span6 alert alert-success')
			return 'Votre mot de passe est fort'
		}
	}
	function checkequals(password,passwordconfirm) {
		if(password!=passwordconfirm)
		{
			$('#resultconfirm').removeClass()
			$('#resultconfirm').addClass('offset3 span6 alert alert-danger')
			return 'Vos mots de passe ne correspendent pas';
			
		}
		else 
		{
			$('#resultconfirm').removeClass()
			$('#resultconfirm').addClass('offset3 span6 alert alert-success')
			return 'Vos mots de passe correspendent';
		}
	}
	$(".inscription .bouton").click(function(e){
		if($('#nom').val()==''){
			$('#nom').addClass("bordure");
			valid=false;
		$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");	
		}
		else {
			$('#nom').removeClass("bordure");
		}
		 if($('#prenom').val()=='') {
			$('#prenom').addClass("bordure");
			valid=false;
			$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");
		
		}
		else {
			$(this).css("border","1px solid #eee");
		}
		if($('#pseudo').val()=='') {
			$(this).css("border","1px solid f27b81");
			valid=false;
			$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");
		
		}
		else {
			$(this).css("border","1px solid #eee");
			valid=false;
			$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");
		
		}
		if( $('#mail').val()=='')
		{
			$(this).css("border","1px solid f27b81");
			valid=false;
			$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");
		
		}
		else {
			$(this).css("border","1px solid #eee");
		}
		if($('#password').val()=='') {
			$(this).css("border","1px solid f27b81");
			valid=false;
			$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");
		
		}
		else {
			$(this).css("border","1px solid #eee");
		}
		if($('#passwordconfirm').val()=='') {
			$(this).css("border","1px solid f27b81");
			valid=false;
			$("#emptyform").html("<div class='alert alert-error offset2 span8' role='alert'><i class='icon-exclamation-sign' aria-hidden='true'></i><span>Il y a des champs vides</div>");
		
		}
		else {
			$(this).css("border","1px solid #eee");
		}
		return valid;
	});

});
