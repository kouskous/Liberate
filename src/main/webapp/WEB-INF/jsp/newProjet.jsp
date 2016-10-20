<%-- 
    Document   : newProjet
    Created on : Oct 20, 2016, 3:20:10 PM
    Author     : Luc Di Sanza
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
        <head>
	
	<!-- start: Meta -->
	<meta charset="utf-8">
	<title># L!BER8</title>

	
	<!-- start: Mobile Specific -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- end: Mobile Specific -->
	
	<!-- start: CSS -->
        <link id="bootstrap-style" href="/Liber8/resources/metro/css/bootstrap.min.css" rel="stylesheet">
	<link href="/Liber8/resources/metro/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link id="base-style" href="/Liber8/resources/metro/css/style.css" rel="stylesheet">
	<link id="base-style-responsive" href="/Liber8/resources/metro/css/style-responsive.css" rel="stylesheet">
        <link id="base-style-responsive" href="/Liber8/resources/liber8/css/newProjet.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext' rel='stylesheet' type='text/css'>
	<!-- end: CSS -->
	

	<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	  	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<link id="ie-style" href="resources/metro/css/ie.css" rel="stylesheet">
	<![endif]-->
	
	<!--[if IE 9]>
		<link id="ie9style" href="resources/metro/css/ie9.css" rel="stylesheet">
	<![endif]-->
		
	<!-- start: Favicon -->
	<link rel="shortcut icon" href="/Liber8/resources/metro/img/favicon.ico">
	<!-- end: Favicon -->
	
</head>
    <body>        
        <div class="container-fluid">
            <div class="row-fluid">
                <h2> Nouveau Projet </h2>
                <h2>${Erreur}</h2>
                    <form action = "newProjet" method="post">
                        <label>
                            Nom du Projet
                        </label>
                        <input name="nomProjet" id="nomProjet" type="text" placeholder=" Nom du Projet"/>
                        <label>
                          Langage
                        </label>
                        <select name="langageProjet">
                            <option value="C">C</option>
                            <option value="C++">C++</option>
                            <option value="Java">Java</option>
                            <option value="JavaScript">JavaScript</option>
                        </select>                 
                        <button type="submit">Créer le projet</button>
                    </form>
    </body>
</html>
