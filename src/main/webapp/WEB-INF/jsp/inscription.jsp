<%-- 
    Document   : inscription
    Created on : 18 oct. 2016, 17:06:40
    Author     : macair
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
	<!-- start: Meta -->
	<meta charset="utf-8">
	<title># L!BER8 Inscription</title>
	<!-- start: Mobile Specific -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- end: Mobile Specific -->
	<!-- start: CSS -->
	<link id="bootstrap-style" href="/Liber8/resources/metro/css/bootstrap.min.css" rel="stylesheet">
	<link href="/Liber8/resources/metro/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link id="base-style" href="/Liber8/resources/metro/css/style.css" rel="stylesheet">
	<link id="base-style-responsive" href="/Liber8/resources/metro/css/style-responsive.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext' rel='stylesheet' type='text/css'>
	<link id="inscription" href="/Liber8/resources/liber8/css/inscription.css" rel="stylesheet">
        <!-- end: CSS -->
	<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<link id="ie-style" href="css/ie.css" rel="stylesheet">
	<![endif]-->
	
	<!--[if IE 9]>
		<link id="ie9style" href="css/ie9.css" rel="stylesheet">
	<![endif]-->
			<style type="text/css">
			body { background: url(/Liber8/resources/metro/img/bg-login.jpg) !important; }
		</style>
    </head>
    <body>
        <div class="container-fluid">
            ${Erreur}
            <div class="row-fluid">
                <div class="span4 logo">
                    <div class="vertically">
                        <img src="/Liber8/resources/metro/img/logo_liber8.png"/>
                        <p>Description</p>
                    </div>
                </div>
                    
                <div class="span8">
                    <form class="form-horizontal login-box inscription" action="inscription" method="post">
                        <fieldset>
                            <div id="emptyform"></div>
                            <div class="input-prepend" title="Nom et prénom">
                                <p>Entrez votre nom et votre prénom</p>
                                    <div class="span5">
                                        <input class="input-large span12" name="nom" id="nom" type="text" placeholder=" Nom"/>
                                        <div class="error_name"></div></div>
                                    <div class="span5 offset1">
                                        <input class="input-large span12" name="prenom" id="prenom" type="text" placeholder=" Prénom"/>
                                            <div class="error_prenom"></div>
                                    </div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="input-prepend" title="Pseudo">
                                <p>Choisissez votre pseudo</p>
                                <input class="input-large span11" name="pseudo" id="pseudo" type="text" placeholder="Pseudo"/>
                                <div class="error_pseudo"></div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="input-prepend" title="Email">
                                <p>Entrer votre adresse mail</p>
                                <input class="input-large span11" name="mail" id="mail" type="text" placeholder="Mail"/>
                                <div class="error_mail"></div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="input-prepend" title="Password">
                                <p>Choisissez votre mot de passe</p>
				<input class="input-large span11" name="password" id="password" type="password" placeholder=" Mot de passe"/>
                                <div id="result"></div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="input-prepend" title="Passwordconfirm">
                                <p>Confirmer votre mot de passe</p>
				<input class="input-large span11" name="passwordconfirm" id="passwordconfirm" type="password" placeholder=" Mot de passe"/>
                                <div id="resultconfirm"></div>
                            </div>
                            <div class="clearfix"></div>
				<div class="button-login">	
                                    <button type="submit" class="btn btn-primary bouton">Créer un compte</button>
				</div>
				<div class="clearfix"></div>
                                
                    </form>
                </div>
            </div>
        </div> 
        <script src="/Liber8/resources/metro/js/jquery-1.9.1.min.js"></script>
         <script src="/Liber8/resources/metro/js/bootstrap.min.js"></script>
        <script src="/Liber8/resources/liber8/js/validationform.js"></script>
         <script src="/Liber8/resources/liber8/js/inscription.js"></script>
    </body>
</html>
