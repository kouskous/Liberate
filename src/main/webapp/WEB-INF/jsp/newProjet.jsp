<%-- 
    Document   : newProjet
    Created on : Oct 20, 2016, 3:20:10 PM
    Author     : Luc Di Sanza faddi sofiaa
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
                <div class="box" span12>
                <div class="box-header" data-original-title>
						<h2><i class="icon-folder-close"></i></i><span class="break"></span>Nouveau projet</h2>
						
					</div>
                <h2>${Erreur}</h2>
                    <form action = "newProjet" method="post" id="newprojet">
                        <div class="box span6">
                            <div class="box-header" data-original-title="">
						<h2><i class="halflings-icon edit"></i><span class="break"></span>Général</h2>
					</div>
                           <div class="newproject">
                            <div class="projetinput">
                               
                               <label class="span4">Nom du Projet</label>
                                <input name="nomProjet" id="nomProjet" type="text" placeholder=" Nom du Projet"/>
                            </div>
                            <div class="control-group">
								<label class="control-label userline span4" for="selectErrortype">Langage</label>
								<div class="controls userline">
								  <select id="selectErrortype" data-rel="chosen" name="langage">
									<option>Java</option>
									<option>C++</option>
									<option>C</option>
									<option>Javascript</option>
								  </select>
								</div>
							  </div>
                            <div class="control-group">
                                <label class="control-label"><h3>Sources</h3></label>
                                    <div class="controls">
                                	<label class="radio">
				<div class="radio inputradio" id="uniform-optionsRadios1"><input type="radio" name="sourcesprojet" id="vide" value="vide"></div>
                                        Vide
			</label><br/>
			<label class="radio">
				<div class="radio inputradio" id="uniform-optionsRadios2"><input type="radio" name="sourcesprojet" id="existant" value="existant"></div>
                                        A partir des fichiers existant
			</label>
		</div>
</div>
                           </div></div>
                        <div class="box span6">
                            <div class="box-header" data-original-title="">
						<h2><i class="icon-user"></i><span class="break"></span>Collaboration et partage</h2>
					</div>
                            <ul class='usernew'>
                                <li><div class="control-group">
								<label class="control-label userline" for="selectError1">@user1</label>
								<div class="controls userline">
								  <select id="selectError1" data-rel="chosen" name="userprojet1">
									<option>Modification</option>
									<option>Lecture</option>
								  </select>
								</div>
							  </div></li>
                                <li><div class="control-group">
								<label class="control-label userline" for="selectError2">@user2</label>
								<div class="controls userline">
								  <select id="selectError2" data-rel="chosen" name="userprojet2">
									<option>Modification</option>
									<option>Lecture</option>
								  </select>
								</div>
							  </div></li><li><div class="control-group">
								<label class="control-label userline" for="selectError3">@user3</label>
								<div class="controls userline">
								  <select id="selectError3" data-rel="chosen" name="userprojet3">
									<option>Modification</option>
									<option>Lecture</option>
								  </select>
								</div>
							  </div></li>
                            </ul>
                            <a href="#" class="addbouton"><i class="icon-plus"></i></a>
                           
                        </div>
                         <div class="form-actions">
								<button type="submit" class="btn btn-primary">Terminer</button>
								<button class="btn">Annuler</button>
							  </div>
                       
                    </form>
                </div>
            </div>
        </div>
                    <script src="/Liber8/resources/metro/js/jquery-1.9.1.min.js"></script>
	<script src="/Liber8/resources/metro/js/jquery-migrate-1.0.0.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery-ui-1.10.0.custom.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.ui.touch-punch.js"></script>
	
		<script src="/Liber8/resources/metro/js/modernizr.js"></script>
	
		<script src="/Liber8/resources/metro/js/bootstrap.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.cookie.js"></script>
	
		<script src='/Liber8/resources/metro/js/fullcalendar.min.js'></script>
	
		<script src='/Liber8/resources/metro/js/jquery.dataTables.min.js'></script>

		<script src="/Liber8/resources/metro/js/excanvas.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.pie.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.stack.js"></script>
	<script src="/Liber8/resources/metro/js/jquery.flot.resize.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.chosen.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.uniform.min.js"></script>
		
		<script src="/Liber8/resources/metro/js/jquery.cleditor.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.noty.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.elfinder.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.raty.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.iphone.toggle.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.uploadify-3.1.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.gritter.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.imagesloaded.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.masonry.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.knob.modified.js"></script>
	
		<script src="/Liber8/resources/metro/js/jquery.sparkline.min.js"></script>
	
		<script src="/Liber8/resources/metro/js/counter.js"></script>
	
		<script src="/Liber8/resources/metro/js/retina.js"></script>

		<script src="/Liber8/resources/metro/js/custom.js"></script>
	<!-- end: JavaScript-->
	
    </body>
</html>
